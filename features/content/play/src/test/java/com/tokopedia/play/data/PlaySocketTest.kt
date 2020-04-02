package com.tokopedia.play.data

import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.play.data.mapper.PlaySocketType
import com.tokopedia.play.data.websocket.PlaySocket
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.WebSocketInfo
import com.tokopedia.websocket.WebSocketResponse
import com.tokopedia.websocket.WebSocketSubscriber
import okhttp3.WebSocket
import rx.Observable
import rx.subscriptions.CompositeSubscription


/**
 * Created by mzennis on 02/04/20.
 */
class PlaySocketTest(userSessionInterface: UserSessionInterface,
                     localCacheHandler: LocalCacheHandler) : PlaySocket(userSessionInterface, localCacheHandler) {

    lateinit var webSocket: WebSocket
    private lateinit var compositeSubscription: CompositeSubscription

    lateinit var webSocketSubscriber: WebSocketSubscriber

    private fun start() {
        val webSocketSubscription = Observable.just(dummyWebSocketInfo())
                .subscribe(webSocketSubscriber)
        compositeSubscription.add(webSocketSubscription)
    }

    private fun dummyWebSocketInfo(): WebSocketInfo {
        val webSocketInfo = WebSocketInfo(webSocket, "")
        webSocketInfo.isOnOpen = true
        webSocketInfo.isOnReconnect = false
        webSocketInfo.response = totalLike()
        return webSocketInfo
    }

    private fun totalLike(): WebSocketResponse {
        val jsonObject = JsonObject()
        jsonObject.addProperty("total_like", 48)
        jsonObject.addProperty("total_like_formatted", "48")

        return WebSocketResponse(
                type = PlaySocketType.TotalLike.value,
                code = 200,
                jsonElement = jsonObject)
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
        start()
    }
}