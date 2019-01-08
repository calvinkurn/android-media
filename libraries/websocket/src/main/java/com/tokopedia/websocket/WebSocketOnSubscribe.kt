package com.tokopedia.websocket

import android.util.Log
import com.tokopedia.network.constant.TkpdBaseURL
import okhttp3.*
import okio.ByteString
import rx.Observable
import rx.Subscriber

/**
 * @author : Steven 07/10/18
 */
class WebSocketOnSubscribe internal constructor(private val client: OkHttpClient, private val url: String, private val accessToken: String) : Observable.OnSubscribe<WebSocketInfo> {

    private var webSocket: WebSocket? = null

    private val showLog = true
    private val logTag = "RxWebSocket"

    override fun call(subscriber: Subscriber<in WebSocketInfo>) {
        initWebSocket(subscriber, accessToken)
    }

    private fun getRequest(url: String, accessToken: String): Request {
        return Request.Builder().get().url(url)
                .header("Origin", TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL)
                .header("Accounts-Authorization",
                        "Bearer $accessToken")
                .build()
    }

    private fun initWebSocket(subscriber: Subscriber<in WebSocketInfo>, accessToken: String) {
        webSocket = client.newWebSocket(getRequest(url, accessToken), object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response?) {
                if (showLog) {
                    Log.d(logTag, "$url --> onOpen")
                }
                if (!subscriber.isUnsubscribed) {
                    subscriber.onNext(WebSocketInfo(webSocket!!, true))
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                if (!subscriber.isUnsubscribed) {
                    subscriber.onNext(WebSocketInfo(webSocket, text))
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                if (!subscriber.isUnsubscribed) {
                    subscriber.onNext(WebSocketInfo(webSocket, bytes))
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) { if (showLog) {
                    Log.e(logTag, t!!.toString() + webSocket.request().url().uri().path)
                }
                if (!subscriber.isUnsubscribed) {
                    subscriber.onNext(WebSocketInfo.createReconnect())
                    subscriber.onError(t)
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(1000, "onClosing")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                if (showLog) {
                    Log.d(logTag, "$url --> onClosed:code = $code, reason = $reason")
                    subscriber.onNext(WebSocketInfo.createReconnect())
                }
            }

        })

        subscriber.add(object : MainThreadSubscription() {
            override fun onUnsubscribe() {
                webSocket?.close(3000, "close WebSocket")
                if (showLog) {
                    Log.d(logTag, "$url --> onUnsubscribe ")
                }
            }
        })
    }
}