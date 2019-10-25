package com.tokopedia.groupchat.chatroom.websocket;

import androidx.annotation.NonNull;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.groupchat.chatroom.view.viewmodel.chatroom.WebSocketResponse;

import okhttp3.WebSocket;
import okio.ByteString;
import rx.Subscriber;

/**
 * Created by dhh on 2017/11/2.
 * override the method of you want to use
 */

public abstract class WebSocketSubscriber extends Subscriber<WebSocketInfo> {
    private boolean hasOpened;

    @Override
    public final void onNext(@NonNull WebSocketInfo webSocketInfo) {
        if (webSocketInfo.isOnOpen()) {
            hasOpened = true;
            onOpen(webSocketInfo.getWebSocket());
        } else if (webSocketInfo.getString() != null) {
            onMessage(webSocketInfo.getString());
        } else if (webSocketInfo.getByteString() != null) {
            onMessage(webSocketInfo.getByteString());
        } else if (webSocketInfo.isOnReconnect()) {
            onReconnect();
        }

        if (webSocketInfo.getItem() != null) {
            onMessage(webSocketInfo.getItem(), webSocketInfo.shouldHideMessage(webSocketInfo.getItem()));
        }
    }

    protected void onOpen(@NonNull WebSocket webSocket) {
    }

    protected void onMessage(@NonNull String text) {
    }

    protected void onMessage(@NonNull Visitable item, boolean b) {
    }

    protected void onMessage(@NonNull WebSocketResponse text) {
    }

    protected void onMessage(@NonNull ByteString byteString) {
    }

    protected void onReconnect() {
    }

    protected void onClose() {

    }

    @Override
    public final void onCompleted() {
        if (hasOpened) {
            onClose();
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

}
