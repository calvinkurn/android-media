package com.tokopedia.topchat.chatroom.domain;

import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.topchat.chatroom.data.mapper.WebSocketMapper;
import com.tokopedia.topchat.chatroom.domain.messages.RxEventConnected;
import com.tokopedia.topchat.chatroom.domain.messages.RxEventDisconnected;
import com.tokopedia.topchat.chatroom.domain.messages.RxEventStringMessage;
import com.tokopedia.topchat.chatroom.domain.pojo.reply.WebSocketResponse;
import com.tokopedia.topchat.chatroom.view.viewmodel.BaseChatViewModel;

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
    private WebSocketMapper webSocketMapper;
    private boolean isChatList = false;
    public GroupChatWebSocketListenerImpl(WebSocketMapper webSocketMapper) {
        subject = PublishSubject.create();
        this.webSocketMapper = webSocketMapper;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        subject.onNext(new RxEventConnected(webSocket));
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        BaseChatViewModel message = webSocketMapper.map(text);
        subject.onNext(new RxEventStringMessage(webSocket, text));
    }

    private WebSocketResponse process(String text) {
        WebSocketResponse data = new GsonBuilder().create().fromJson(text, WebSocketResponse.class);
        return data;
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