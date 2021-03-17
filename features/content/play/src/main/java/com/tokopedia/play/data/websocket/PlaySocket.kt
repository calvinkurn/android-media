package com.tokopedia.play.data.websocket

import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.play.data.SocketCredential
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.*
import okhttp3.WebSocket
import rx.Observable
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject


/**
 * Created by mzennis on 2019-12-11.
 */
open class PlaySocket @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
        private val localCacheHandler: LocalCacheHandler
) {

    var channelId: String = ""
    var gcToken: String = ""
    var settings: SocketCredential.Setting = SocketCredential.Setting(DEFAULT_PING, 0, DEFAULT_MAX_RETRIES, DEFAULT_DELAY)

    private val playSocketCache: PlaySocketCache = PlaySocketCache()

    private var compositeSubscription: CompositeSubscription? = null
    private var rxWebSocketUtil: RxWebSocketUtil? = null

    open fun connect(onMessageReceived: (WebSocketResponse)-> Unit, onReconnect: () -> Unit,  onError: (error: Throwable) -> Unit) {
        val wsBaseUrl: String = localCacheHandler.
                getString(KEY_GROUPCHAT_DEVELOPER_OPTION_PREFERENCES,
                        TokopediaUrl.getInstance().WS_PLAY)
        var wsConnectUrl = "$wsBaseUrl$PLAY_WEB_SOCKET_GROUP_CHAT$channelId"
        if (gcToken.isNotEmpty())
            wsConnectUrl += "&token=$gcToken"

        compositeSubscription?.clear()
        if (compositeSubscription == null) {
            compositeSubscription = CompositeSubscription()
        }

        val webSocketSubscriber = object : WebSocketSubscriber() {

            override fun onOpen(webSocket: WebSocket) {
                sendCachedMessages()
            }

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

    private fun sendCachedMessages() {
        if (playSocketCache.chatList.isNotEmpty()) {
            Observable.just(playSocketCache.chatList)
                    .flatMapIterable { items -> items }
                    .map { item ->
                        rxWebSocketUtil?.send(item.message)
                        item
                    }
                    .subscribe { item ->
                        item.isDeleted = true
                    }
            playSocketCache.clear()
        }
    }

    fun send(message: String) {
        val chat = parseMessage(message)
        try { rxWebSocketUtil?.send(chat) }
        catch (exception: WebSocketException) {
            playSocketCache.putChat(PlaySocketCache.Chat(chat))
        }
    }

    private fun parseMessage(message: String): String {
        val param = JsonObject()
        param.addProperty(PARAM_SEND_CHANNEL_ID, channelId.toIntOrZero())
        param.addProperty(PARAM_SEND_MESSAGE, message)

        val bundle = JsonObject()
        bundle.addProperty(PARAM_SEND_TYPE, PARAM_SEND_TYPE_SEND)
        bundle.add(PARAM_SEND_DATA, param)

        return bundle.toString()
    }

    fun destroy() {
        compositeSubscription?.clear()
    }

    companion object {

        const val KEY_GROUPCHAT_PREFERENCES = "com.tokopedia.groupchat.chatroom.view.presenter.GroupChatPresenter"

        private const val PLAY_WEB_SOCKET_GROUP_CHAT = "/ws/groupchat?channel_id="

        private const val KEY_GROUPCHAT_DEVELOPER_OPTION_PREFERENCES = "ip_groupchat"

        private const val PARAM_SEND_TYPE = "type"
        private const val PARAM_SEND_DATA = "data"
        private const val PARAM_SEND_TYPE_SEND = "SEND_MESG"
        private const val PARAM_SEND_CHANNEL_ID = "channel_id"
        private const val PARAM_SEND_MESSAGE = "message"
    }
}