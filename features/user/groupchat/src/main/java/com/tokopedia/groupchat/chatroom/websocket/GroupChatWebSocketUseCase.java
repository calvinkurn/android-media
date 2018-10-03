package com.tokopedia.groupchat.chatroom.websocket;

import android.os.CountDownTimer;

import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.data.model.session.UserSession;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

/**
 * @author by StevenFredian on 24/04/18.
 */

public class GroupChatWebSocketUseCase {

    private GroupChatWebSocketListenerImpl listener;
    private CountDownTimer countDownTimer;
    private OkHttpClient client;
    private String url;
    private WebSocket ws;
    private UserSession userSession;


    public GroupChatWebSocketUseCase(String magicString, UserSession session, GroupChatWebSocketListenerImpl
            listener) {
        this.client = new OkHttpClient();
        this.url = magicString;
        this.userSession = session;
        this.listener = new GroupChatWebSocketListenerImpl();
        this.countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                createWebSocket();
            }
        };
        createWebSocket();
    }

    private void createWebSocket() {
        Request request = new Request.Builder().url(url)
                .header("Origin", "172.31.5.228:8000")
                .header("Accounts-Authorization", "Bearer " + userSession.getAccessToken())
                .build();
        ws = client.newWebSocket(request, listener);
    }

    public void recreateWebSocket() {
        countDownTimer.start();
    }

    public void closeConnection() {
        try {
            client.dispatcher().executorService().shutdown();
            ws.close(1000, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unsubscribe() {
        countDownTimer.cancel();
    }

    public void execute(JsonObject json) {
        ws.send(json.toString());
    }


}
