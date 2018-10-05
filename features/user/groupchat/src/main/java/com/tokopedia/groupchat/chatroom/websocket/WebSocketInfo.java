package com.tokopedia.groupchat.chatroom.websocket;

import android.support.annotation.Nullable;

import com.google.gson.GsonBuilder;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.BaseChatViewModel;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.WebSocketResponse;

import okhttp3.WebSocket;
import okio.ByteString;

/**
 * Created by dhh on 2017/9/21.
 */

public class WebSocketInfo {
    private WebSocket mWebSocket;
    private BaseChatViewModel baseChatViewModel;
    private WebSocketResponse response;
    private String mString;
    private ByteString mByteString;
    private boolean onOpen;
    private boolean onReconnect;

    private WebSocketInfo() {
    }

    WebSocketInfo(WebSocket webSocket, boolean onOpen) {
        mWebSocket = webSocket;
        this.onOpen = onOpen;
    }

    WebSocketInfo(WebSocket webSocket, String mString) {
        mWebSocket = webSocket;
        this.mString = mString;
        this.response = new GsonBuilder().create().fromJson(mString, WebSocketResponse.class);
    }

    WebSocketInfo(WebSocket webSocket, ByteString byteString) {
        mWebSocket = webSocket;
        mByteString = byteString;
    }

    static WebSocketInfo createReconnect() {
        WebSocketInfo socketInfo = new WebSocketInfo();
        socketInfo.onReconnect = true;
        return socketInfo;
    }

    public WebSocket getWebSocket() {
        return mWebSocket;
    }

    public void setWebSocket(WebSocket webSocket) {
        mWebSocket = webSocket;
    }

    @Nullable
    public String getString() {
        return mString;
    }

    public void setString(String string) {
        this.mString = string;
    }

    public WebSocketResponse getResponse() {
        return response;
    }

    public void setResponse(WebSocketResponse response) {
        this.response = response;
    }

    @Nullable
    public ByteString getByteString() {
        return mByteString;
    }

    public void setByteString(ByteString byteString) {
        mByteString = byteString;
    }

    public boolean isOnOpen() {
        return onOpen;
    }

    public boolean isOnReconnect() {
        return onReconnect;
    }
}
