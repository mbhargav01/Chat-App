import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.*;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.io.*;

public class Server extends JFrame
{
    //variables..
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //Declare Components
    private JLabel heading=new JLabel("Server Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Courier",Font.ITALIC,20);

   //Constructor..
   public Server()
   {
     try {   
             server=new ServerSocket(7777);
             System.out.println("server is ready to accept connection");
             System.out.println("waiting...");
             socket=server.accept();

             br=new BufferedReader(new InputStreamReader(socket.getInputStream()));//get_socket se data lega..
             out=new PrintWriter(socket.getOutputStream());//get_socket ko data dega..

             createGUI();
             handleEvents();
             startReading();
             startWriting();
         } catch (Exception e)
               {
                  System.out.println("end");
               }
    } 
 
        private void handleEvents() {
        messageInput.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }
            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }
           @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub

               // System.out.println("key released "+e.getKeyCode());//press ki hui key ke code print krega
                if(e.getKeyCode()==10){     //enter key code is 10
              //  System.out.println("you have pressed enter button");
                    String contentToSend=messageInput.getText();   //type kia hua msg nikalna
                    messageArea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);                    //msg ko send krna
                    out.flush();
                    messageInput.setText("");                      //clear hoker isme text ho jayga
                    messageInput.requestFocus();
                }
                
            }

        });
}    private void createGUI() {
         //GUI code..
        this.setTitle("Server Messager[END]");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);//Screen center kar dega
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         //Coding for component
         heading.setFont(font);
         messageArea.setFont(font);
         messageInput.setFont(font);
         heading.setIcon(new ImageIcon("clogo(2).png"));
         heading.setHorizontalTextPosition(SwingConstants.CENTER);
         heading.setVerticalTextPosition(SwingConstants.BOTTOM);
         heading.setHorizontalAlignment(SwingConstants.CENTER);//heading ko center krne ke liye
         heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
         messageArea.setEditable(false);
         messageInput.setHorizontalAlignment(SwingConstants.CENTER);
 
         //Frame Layout Setting
         this.setLayout(new BorderLayout());
 
         //adding the components to frame
         this.add(heading,BorderLayout.NORTH);
         JScrollPane jScrollPane=new JScrollPane(messageArea);
         this.add(jScrollPane,BorderLayout.CENTER);
         this.add(messageInput,BorderLayout.SOUTH);
 
          this.setVisible(true);
}

    public void startReading()
       {
           //thread-read karke deta rahega

           Runnable r1=()->{
               System.out.println("reader started..");
               try{
               while(true)
               {
                String msg=br.readLine();
                if(msg.equals("bye"))
                {
                    System.out.println("Milte Hai Ek Break Ke Baad..!!");
                    JOptionPane.showMessageDialog(this,"Milte Hai Ek Break Ke Baad..!!");
                    messageInput.setEnabled(false);

                    socket.close();
                    break;
                }

                //System.out.println("You : "+msg);
                messageArea.append("You : "+msg+"\n");
            }
        }
            catch(Exception e){
                System.out.println("");
           
          }
       };

       new Thread(r1).start();

     }
    
    public void startWriting()
       {
           //thread-data user se lega and send krega client ko
           Runnable r2=()->{
               System.out.println("writer started..");
               try {
               while(!socket.isClosed())
               {
                       BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                       String content=br1.readLine();
                       out.println(content);
                       out.flush();
                       if(content.equals("bye"))
                       {
                        messageInput.setEnabled(false);
                         socket.close();
                         break;
                       }

                   } 
                   }catch (Exception e) {
                    System.out.println("end");
               }
           };

          new Thread(r2).start();

       }
        public static void main(String[] args) {
        System.out.println("this is server..going to start server");
        new Server();
    }
}