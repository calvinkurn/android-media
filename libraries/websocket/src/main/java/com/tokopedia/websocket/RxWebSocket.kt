package com.tokopedia.websocket

import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import rx.Observable

/**
 * Created by dhh on 2018/3/29.
 *
 * @author dhh
 */
object RxWebSocket {

    operator fun get(url: String, accessToken: String, tkpdAuthInterceptor: TkpdAuthInterceptor,
                     fingerprintInterceptor: FingerprintInterceptor):
            Observable<WebSocketInfo>? {
        return RxWebSocketUtil.getInstance(tkpdAuthInterceptor, fingerprintInterceptor)?.getWebSocketInfo(url,
                accessToken)
    }

    fun send(msg: String, tkpdAuthInterceptor: TkpdAuthInterceptor,
             fingerprintInterceptor: FingerprintInterceptor) {
        RxWebSocketUtil.getInstance(tkpdAuthInterceptor, fingerprintInterceptor)?.send(msg)
    }

//    fun asyncSend(url: String, msg: String, groupChatToken: String) {
//        RxWebSocketUtil.getInstance()?.asyncSend(url, msg, groupChatToken)
//    }

}
