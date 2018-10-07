package com.tokopedia.groupchat.chatroom.websocket;

import rx.Observable;

/**
 * Created by dhh on 2018/3/29.
 *
 * @author dhh
 */
public final class RxWebSocket {

    public static Observable<WebSocketInfo> get(String url, String accessToken) {
        return RxWebSocketUtil.getInstance().getWebSocketInfo(url, accessToken);
    }

    public static void send(String url, String msg) {
        RxWebSocketUtil.getInstance().send(url, msg);
    }

    public static void asyncSend(String url, String msg) {
        RxWebSocketUtil.getInstance().asyncSend(url, msg);
    }
}
