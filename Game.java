/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2048;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Dell
 */
public class Game extends JPanel implements KeyListener,Runnable{

    public static final long serialVersionUID=1L;
    public static final int WIDTH=400;
    public static final int HEIGHT=380;
    public static final Font main=new Font("Times New Roman",Font.PLAIN,28);
    private Thread game;
    private boolean running;
    private final BufferedImage Image=new BufferedImage(WIDTH,HEIGHT,(BufferedImage.TYPE_INT_RGB));
    private GameBoard board;
    
    private long startTime;
    private long elapsed;
    private boolean set;
    
    public Game()
    {
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        addKeyListener(this);
        
        board=new GameBoard(WIDTH/2-GameBoard.BOARD_WIDTH/2,HEIGHT-GameBoard.BOARD_HEIGHT-10);
    }
    private void update()
    {
        board.update();
        Keyboard.update();    
        
    }
    private void render()
    {
        Graphics2D g=(Graphics2D)Image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0,0,WIDTH,HEIGHT);
        //renderboard
        board.render(g);
        g.dispose();
        
        Graphics2D g2d=(Graphics2D)getGraphics();
        g2d.drawImage(Image,0,0,null);
        g2d.dispose();
        
    }
    @Override
    public void run() {
     int fps=0,updates=0;  
     long fpsTimer= System.currentTimeMillis();
     double nsPerUpdate =1000000000.0/ 60;
     
     //last update time in nanoseconds
     double then =System.nanoTime();
     double unprocessed =0;
     
     while(running)
     {
         
         boolean shoudRender =false;
         double now =System.nanoTime();
         unprocessed +=(now-then)/nsPerUpdate;
         then=now;
         
         //update queue
         
         while(unprocessed >=1)
         {
             updates++;
             update();
             unprocessed--;
             shoudRender=true;
             
         }
         
         //render
         if(shoudRender)
         {
             fps++;
             render();
             shoudRender =false;
         }
         else
         {
             try{
                 Thread.sleep(1);
             }
             catch(Exception e)
             {
                 e.printStackTrace();
             }
         }
     }
     //Fps Timer
     if(System.currentTimeMillis()-fpsTimer >1000)
     {
         System.out.printf("%d fps %d updates",fps,updates);
         System.out.println("");
         fps=0;
         updates=0;
         fpsTimer +=1000;
         
          
     }
    }
    
    public synchronized void start()
    {
        if(running)return;
        running=true;
        game=new Thread(this,"game");
        game.start();
    }
    public synchronized void stop()
    {
        if(!running)return;
        running=false;
        System.exit(0);
    }
    @Override
    public void keyTyped(KeyEvent e) {
        Keyboard.typed(e.getKeyCode());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("ss");
        Keyboard.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("aa");
        Keyboard.keyReleased(e);
    }

    
    
}
