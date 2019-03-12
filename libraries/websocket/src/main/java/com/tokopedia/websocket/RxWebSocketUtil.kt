package com.tokopedia.websocket

import android.util.Log
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
class RxWebSocketUtil private constructor(interceptors: List<Interceptor>?,
                                          private val delay: Int,
                                          private val maxRetries: Int,
                                          private val pingInterval: Long) {

    private val client: OkHttpClient

    private var observableMap: Observable<WebSocketInfo>? = null
    private var webSocketMap: WebSocket? = null
    private var showLog = true
    private val logTag = "MainActRxWebSocket"

    init {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        builder.pingInterval(pingInterval, TimeUnit.MILLISECONDS)
        interceptors?.let {
            for (i in 0 until interceptors.size) {
                builder.addInterceptor(interceptors[i])
            }
        }
        client = builder.build()
    }

    fun getWebSocketInfo(url: String, accessToken: String): Observable<WebSocketInfo>? {
        if (observableMap == null) {
            val retryObservable = RetryObservable(maxRetries, delay.toLong())
            observableMap = Observable.create(WebSocketOnSubscribe(client, url, accessToken))
                    .retryWhen(retryObservable)
                    .doOnUnsubscribe {
                        observableMap = null
                        webSocketMap = null
                        if (showLog) {
                            Log.d(logTag, "unsubscribe")
                        }
                    }
                    .doOnNext { webSocketInfo ->
                        if (webSocketInfo.isOnOpen!!) {
                            retryObservable.resetMaxRetries()
                            webSocketMap = webSocketInfo.webSocket
                        }
                    }
                    .share()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        } else {
            if (webSocketMap != null) {
                observableMap = observableMap!!.startWith(WebSocketInfo(webSocketMap, true))
            }
        }
        return observableMap
    }

    fun send(msg: String) {
        val webSocket = webSocketMap
        webSocket?.send(msg) ?: throw WebSocketException("The WebSokcet not open")
    }

    companion object {
        private val DEFAULT_PING = 10000L
        private val DEFAULT_MAX_RETRIES = 3
        private val DEFAULT_DELAY = 5000
        private var instance: RxWebSocketUtil? = null

        @JvmOverloads
        fun getInstance(interceptors: List<Interceptor>?, delay: Int =
                                DEFAULT_DELAY, maxRetries: Int = DEFAULT_MAX_RETRIES, pingInterval: Long = DEFAULT_PING): RxWebSocketUtil? {
            if (instance == null) {
                instance = RxWebSocketUtil(interceptors, delay, maxRetries, pingInterval)
            }
            return instance
        }

    }
}
