package com.tokopedia.websocket

import com.google.gson.JsonObject
import okhttp3.Interceptor
import rx.Observable

/**
 * Created by dhh on 2018/3/29.
 *
 * @author dhh
 */
object RxWebSocket {

    private fun getInstance(interceptors: List<Interceptor>?): RxWebSocketUtil? {
        return RxWebSocketUtil.getInstance(interceptors)
    }

    operator fun get(url: String, accessToken: String): Observable<WebSocketInfo>?{
        return get(url, accessToken, null)
    }

    operator fun get(url: String, accessToken: String, interceptors: List<Interceptor>?):
            Observable<WebSocketInfo>? {
        return getInstance(interceptors)?.getWebSocketInfo(url,
                accessToken)
    }

    operator fun get(url: String, accessToken: String, delay: Int, pingInterval: Long, maxRetries: Int):
            Observable<WebSocketInfo>? {
        return RxWebSocketUtil.getInstance(null, delay, maxRetries, pingInterval)?.getWebSocketInfo(url, accessToken)
    }

    fun send(msg: String, interceptors: List<Interceptor>?) {
        try {
            getInstance(interceptors)?.send(msg)
        }catch(ignore : WebSocketException){
        }
    }

    fun send(json: JsonObject, interceptors: List<Interceptor>?) {
        send(json.toString(), interceptors)
    }
}
