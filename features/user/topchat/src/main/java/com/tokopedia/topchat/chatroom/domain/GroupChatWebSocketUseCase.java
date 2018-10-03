package com.tokopedia.topchat.chatroom.domain;

import android.os.CountDownTimer;

import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.topchat.chatroom.data.ChatWebSocketConstant;
import com.tokopedia.topchat.chatroom.view.presenter.ChatWebSocketListenerImpl;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

/**
 * @author by StevenFredian on 24/04/18.
 */

public class GroupChatWebSocketUseCase {

    private ChatWebSocketListenerImpl listener;
    private CountDownTimer countDownTimer;
    private OkHttpClient client;
    private String url;
    private WebSocket ws;
    private UserSession userSession;


    public GroupChatWebSocketUseCase(String magicString, UserSession session, ChatWebSocketListenerImpl
            listener) {
        this.client = new OkHttpClient();
        this.url = magicString;
        this.userSession = session;
        this.listener = listener;
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
                .header("Origin", TkpdBaseURL.WEB_DOMAIN)
                .header("Accounts-Authorization",
                        "Bearer " + userSession.getAccessToken())
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


    public JsonObject getParamStartTyping(String messageId) {
        JsonObject json = new JsonObject();
        json.addProperty("code", ChatWebSocketConstant.EVENT_TOPCHAT_TYPING);
        JsonObject data = new JsonObject();
        data.addProperty("msg_id", Integer.valueOf(messageId));
        json.add("data", data);
        return json;
    }

    public JsonObject getParamStopTyping(String messageId) {
        JsonObject json = new JsonObject();
        json.addProperty("code", ChatWebSocketConstant.EVENT_TOPCHAT_END_TYPING);
        JsonObject data = new JsonObject();
        data.addProperty("msg_id", Integer.valueOf(messageId));
        json.add("data", data);
        return json;
    }
}
