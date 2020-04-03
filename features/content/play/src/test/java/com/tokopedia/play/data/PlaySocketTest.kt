package com.tokopedia.play.data

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.play.data.websocket.PlaySocket
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.WebSocketInfo
import com.tokopedia.websocket.WebSocketResponse
import com.tokopedia.websocket.WebSocketSubscriber
import io.mockk.mockk
import okhttp3.WebSocket
import rx.Observable
import rx.subscriptions.CompositeSubscription


/**
 * Created by mzennis on 02/04/20.
 */
class PlaySocketTest(userSessionInterface: UserSessionInterface,
                     localCacheHandler: LocalCacheHandler) : PlaySocket(userSessionInterface, localCacheHandler) {

    private var webSocket: WebSocket = mockk(relaxed = true)
    private var compositeSubscription: CompositeSubscription = CompositeSubscription()

    lateinit var webSocketSubscriber: WebSocketSubscriber

    var webSocketResponse: WebSocketResponse? = null

    private fun webSocketInfo(webSocketResponse: WebSocketResponse): WebSocketInfo {
        val webSocketInfo = WebSocketInfo(webSocket, "")
        webSocketInfo.isOnOpen = true
        webSocketInfo.isOnReconnect = false
        webSocketInfo.response = webSocketResponse
        return webSocketInfo
    }

    override fun connect(onMessageReceived: (WebSocketResponse) -> Unit, onReconnect: () -> Unit, onError: (error: Throwable) -> Unit) {
        webSocketSubscriber = object : WebSocketSubscriber() {
            override fun onMessage(webSocketResponse: WebSocketResponse) {
                onMessageReceived(webSocketResponse)
            }

            override fun onError(e: Throwable) {
                onError(e)
            }

            override fun onReconnect() {
                onReconnect()
            }
        }

        webSocketResponse?.let { dummyResponse ->
            val webSocketSubscription = Observable.just(webSocketInfo(dummyResponse))
                    .subscribe(webSocketSubscriber)
            compositeSubscription.add(webSocketSubscription)
        }
    }
}