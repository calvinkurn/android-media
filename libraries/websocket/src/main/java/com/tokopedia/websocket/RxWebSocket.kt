package com.tokopedia.websocket

import rx.Observable

/**
 * Created by dhh on 2018/3/29.
 *
 * @author dhh
 */
object RxWebSocket {

    operator fun get(url: String, accessToken: String): Observable<WebSocketInfo>?{
        return RxWebSocketUtil.getInstance()?.getWebSocketInfo(url, accessToken)
    }

    fun send(url: String, msg: String) {
        RxWebSocketUtil.getInstance()?.send(url, msg)
    }

    fun asyncSend(url: String, msg: String, groupChatToken: String) {
        RxWebSocketUtil.getInstance()?.asyncSend(url, msg, groupChatToken)
    }

}
