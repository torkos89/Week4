
package version_A;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/serverA")
public class Server {
 static Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();
 @OnOpen public void onOpen(Session ses){
   sessions.put(ses.getId(), ses);
 }
 
 @OnMessage public void onMessage(String mes, Session ses){
   if(mes.startsWith("*()JG")){
     ses.getUserProperties().put("name", mes.substring(5));
     broadcast(ses.getUserProperties().get("name").toString()+" logged in","SERVER");
   }
   else broadcast(mes , ses.getUserProperties().get("name").toString());
 }
 
 @OnClose public void onClose(Session ses){
   sessions.remove(ses.getId());
   broadcast(ses.getUserProperties().get("name").toString()+" logged out","SERVER");
 }
 public void broadcast(String mes, String owner){
   for(Session s : sessions.values()){
     s.getAsyncRemote().sendText(owner+": "+mes);
   }
 }
}
