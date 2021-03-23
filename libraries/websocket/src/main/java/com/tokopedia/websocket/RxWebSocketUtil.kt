package com.tokopedia.websocket

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.WebSocket
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit


/**
 * Created by dhh on 2017/9/21.
 * WebSocketUtil based on okhttp and RxJava
 * Core Feature : WebSocket will be auto reconnection onFailed.
 */
open class RxWebSocketUtil protected constructor(
        interceptors: List<Interceptor>?,
        private val delay: Int,
        private val maxRetries: Int,
        pingInterval: Long
) {

    private val client: OkHttpClient

    private var observableMap: Observable<WebSocketInfo>? = null
    private var webSocketMap: WebSocket? = null

    init {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        builder.pingInterval(pingInterval, TimeUnit.MILLISECONDS)
        interceptors?.let {
            for (element in interceptors) {
                builder.addInterceptor(element)
            }
        }
        client = builder.build()
    }

    open fun getWebSocketInfo(url: String, accessToken: String): Observable<WebSocketInfo>? {
        if (observableMap == null) {
            val retryObservable = RetryObservable(maxRetries, delay.toLong())
            observableMap = Observable.create(WebSocketOnSubscribe(client, url, accessToken))
                    .retryWhen(retryObservable)
                    .doOnUnsubscribe {
                        observableMap = null
                        webSocketMap = null
                    }
                    .doOnNext { webSocketInfo ->
                        if (webSocketInfo.isOnOpen) {
                            retryObservable.resetMaxRetries()
                            webSocketMap = webSocketInfo.webSocket
                        }
                    }
                    .share()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        } else {
            webSocketMap?.let {
                observableMap = observableMap!!.startWith(WebSocketInfo(it, true))
            }
        }
        return observableMap
    }

    open fun send(msg: String) {
        val webSocket = webSocketMap
        webSocket?.send(msg) ?: throw WebSocketException("websocket not open")
    }

    fun close() {
        val webSocket = webSocketMap
        webSocket?.close(1001, "disconnected by client")
    }

    companion object {

        private var instance: RxWebSocketUtil? = null

        @JvmOverloads
        fun getInstance(interceptors: List<Interceptor>?,
                        delay: Int = DEFAULT_DELAY,
                        maxRetries: Int = DEFAULT_MAX_RETRIES,
                        pingInterval: Long = DEFAULT_PING): RxWebSocketUtil {
            if (instance == null) {
                instance = RxWebSocketUtil(interceptors, delay, maxRetries, pingInterval)
            }
            return instance!!
        }

    }
}
