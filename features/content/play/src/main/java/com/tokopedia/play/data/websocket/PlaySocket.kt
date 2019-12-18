package com.tokopedia.play.data.websocket

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.play.KEY_GROUPCHAT_DEVELOPER_OPTION_PREFERENCES
import com.tokopedia.play.PLAY_WEB_SOCKET_GROUP_CHAT
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocket
import com.tokopedia.websocket.WebSocketResponse
import com.tokopedia.websocket.WebSocketSubscriber
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

    private var compositeSubscription: CompositeSubscription? = null

    fun connect(onOpen: () -> Unit, onClose: () -> Unit, onMessageReceived: (WebSocketResponse)-> Unit, onError: (error: Throwable) -> Unit) {
        val wsBaseUrl: String = localCacheHandler.
                getString(KEY_GROUPCHAT_DEVELOPER_OPTION_PREFERENCES,
                        TokopediaUrl.getInstance().WS_GROUPCHAT)
        val wsConnectUrl = "$wsBaseUrl$PLAY_WEB_SOCKET_GROUP_CHAT$channelId&token=$gcToken"

        compositeSubscription?.clear()
        if (compositeSubscription == null) {
            compositeSubscription = CompositeSubscription()
        }

        val webSocketSubscriber = object : WebSocketSubscriber() {

            override fun onOpen(webSocket: WebSocket) {
                onOpen()
            }

            override fun onClose() {
                onClose()
            }

            override fun onMessage(webSocketResponse: WebSocketResponse) {
                onMessageReceived(webSocketResponse)
            }

            override fun onError(e: Throwable) {
                onError(e)
            }

            override fun onReconnect() {
                super.onReconnect()

            }
        }

        val webSocketSubscription = RxWebSocket[wsConnectUrl, userSessionInterface.accessToken]?.subscribe(webSocketSubscriber)
        compositeSubscription?.add(webSocketSubscription)
    }

    fun destroy() {
        compositeSubscription?.clear()
    }
}