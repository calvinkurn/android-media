package com.tokopedia.websocket

import com.google.gson.JsonObject
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import rx.Observable

/**
 * Created by dhh on 2018/3/29.
 *
 * @author dhh
 */
object RxWebSocket {

    operator fun get(url: String, accessToken: String, tkpdAuthInterceptor: TkpdAuthInterceptor?,
                     fingerprintInterceptor: FingerprintInterceptor?):
            Observable<WebSocketInfo>? {
        return RxWebSocketUtil.getInstance(tkpdAuthInterceptor, fingerprintInterceptor)?.getWebSocketInfo(url,
                accessToken)
    }

    fun send(msg: String,
             tkpdAuthInterceptor: TkpdAuthInterceptor?,
             fingerprintInterceptor: FingerprintInterceptor?) {
        try {
            RxWebSocketUtil.getInstance(tkpdAuthInterceptor, fingerprintInterceptor)?.send(msg)
        }catch(ignore : WebSocketException){
            //TODO HANDLE ERROR WEBSOCKET CLOSED
        }
    }

    fun send(json: JsonObject, tkpdAuthInterceptor: TkpdAuthInterceptor,
             fingerprintInterceptor: FingerprintInterceptor) {
        send(json.toString(), tkpdAuthInterceptor, fingerprintInterceptor)
    }

//    fun asyncSend(url: String, msg: String, groupChatToken: String) {
//        RxWebSocketUtil.getInstance()?.asyncSend(url, msg, groupChatToken)
//    }

}
