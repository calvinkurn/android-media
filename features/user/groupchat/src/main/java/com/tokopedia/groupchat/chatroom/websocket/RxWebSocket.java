package com.tokopedia.groupchat.chatroom.websocket;

import rx.Observable;

/**
 * Created by dhh on 2018/3/29.
 *
 * @author dhh
 */
public final class RxWebSocket {

    public static Observable<WebSocketInfo> get(String channelUrl, String accessToken
            , int delay, int maxRetries, int pingInterval, String groupChatToken) {
        return RxWebSocketUtil.getInstance(delay, maxRetries, pingInterval).getWebSocketInfo
                (channelUrl, accessToken, groupChatToken);
    }

    public static void send(String url, String msg) {
        RxWebSocketUtil.getInstance().send(url, msg);
    }

}
