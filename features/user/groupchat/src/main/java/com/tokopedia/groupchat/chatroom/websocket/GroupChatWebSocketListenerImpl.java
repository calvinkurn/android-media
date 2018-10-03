package com.tokopedia.groupchat.chatroom.websocket;

import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.groupchat.chatroom.websocket.messages.RxEventConnected;
import com.tokopedia.groupchat.chatroom.websocket.messages.RxEventDisconnected;
import com.tokopedia.groupchat.chatroom.websocket.messages.RxEventStringMessage;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import rx.subjects.PublishSubject;

/**
 * Created by stevenfredian on 9/20/17.
 */

public class GroupChatWebSocketListenerImpl extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    final PublishSubject<Object> subject;
    private boolean isChatList = false;
    public GroupChatWebSocketListenerImpl() {
        subject = PublishSubject.create();
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        subject.onNext(new RxEventConnected(webSocket));
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        subject.onNext(new RxEventStringMessage(webSocket, text));
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        CommonUtils.dumper("WS Message: " + bytes.hex());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(NORMAL_CLOSURE_STATUS, null);
        webSocket.request();
        CommonUtils.dumper("WS Closing : " + code + " / " + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        subject.onNext(new RxEventDisconnected(t));
    }
}