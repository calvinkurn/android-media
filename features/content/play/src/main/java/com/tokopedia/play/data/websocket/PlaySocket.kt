package com.tokopedia.play.data.websocket

import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.play.*
import com.tokopedia.play.data.Channel
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.*
import okhttp3.WebSocket
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject


/**
 * Created by mzennis on 2019-12-11.
 */
class PlaySocket @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val localCacheHandler: LocalCacheHandler
) {

    var channelId: String = ""
    var gcToken: String = ""
    var settings: Channel.Settings = Channel.Settings(DEFAULT_PING, 0, DEFAULT_MAX_RETRIES, DEFAULT_DELAY)

    private var compositeSubscription: CompositeSubscription? = null
    private var rxWebSocketUtil: RxWebSocketUtil? = null

    fun connect(onMessageReceived: (WebSocketResponse)-> Unit, onReconnect: () -> Unit, onError: (error: Throwable) -> Unit) {
        val wsBaseUrl: String = localCacheHandler.
                getString(KEY_GROUPCHAT_DEVELOPER_OPTION_PREFERENCES,
                        TokopediaUrl.getInstance().WS_GROUPCHAT)
        val wsConnectUrl = "$wsBaseUrl$PLAY_WEB_SOCKET_GROUP_CHAT$channelId&token=$gcToken"

        compositeSubscription?.clear()
        if (compositeSubscription == null) {
            compositeSubscription = CompositeSubscription()
        }

        val webSocketSubscriber = object : WebSocketSubscriber() {

            override fun onOpen(webSocket: WebSocket) {}

            override fun onClose() {
                destroy()
            }

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

        rxWebSocketUtil = RxWebSocketUtil.getInstance(null, settings.minReconnectDelay, settings.maxRetries, settings.pingInterval)

        val webSocketSubscription = rxWebSocketUtil?.
                getWebSocketInfo(wsConnectUrl, userSessionInterface.accessToken)?.
                subscribe(webSocketSubscriber)

        compositeSubscription?.add(webSocketSubscription)
    }

    fun send(message: String, onSuccess: () -> Unit) {
        val param = JsonObject()
        param.addProperty(PARAM_SEND_CHANNEL_ID, channelId.toIntOrZero())
        param.addProperty(PARAM_SEND_MESSAGE, message)

        val bundle = JsonObject()
        bundle.addProperty(PARAM_SEND_TYPE, PARAM_SEND_TYPE_SEND)
        bundle.add(PARAM_SEND_DATA, param)

        try {
            rxWebSocketUtil?.send(bundle.toString())
            onSuccess()
        } catch (throwable: WebSocketException) { }
    }

    fun destroy() {
        compositeSubscription?.clear()
    }
}