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


    private fun getInstance(tkpdAuthInterceptor: TkpdAuthInterceptor?, fingerprintInterceptor: FingerprintInterceptor?): RxWebSocketUtil? {
        return RxWebSocketUtil.getInstance(tkpdAuthInterceptor, fingerprintInterceptor)
    }

    operator fun get(url: String, accessToken: String, tkpdAuthInterceptor: TkpdAuthInterceptor?,
                     fingerprintInterceptor: FingerprintInterceptor?):
            Observable<WebSocketInfo>? {
        return getInstance(tkpdAuthInterceptor, fingerprintInterceptor)?.getWebSocketInfo(url,
                accessToken)
    }

    fun send(msg: String,
             tkpdAuthInterceptor: TkpdAuthInterceptor?,
             fingerprintInterceptor: FingerprintInterceptor?) {
        try {
            getInstance(tkpdAuthInterceptor, fingerprintInterceptor)?.send(msg)
        }catch(ignore : WebSocketException){
            //TODO HANDLE ERROR WEBSOCKET CLOSED
        }
    }

    fun send(json: JsonObject, tkpdAuthInterceptor: TkpdAuthInterceptor,
             fingerprintInterceptor: FingerprintInterceptor) {
        send(json.toString(), tkpdAuthInterceptor, fingerprintInterceptor)
    }
}
