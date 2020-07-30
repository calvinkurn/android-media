package com.tokopedia.play.broadcaster.socket

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.play.broadcaster.domain.model.*
import com.tokopedia.play.broadcaster.util.extension.sendCrashlyticsLog
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.*
import okhttp3.WebSocket
import rx.subscriptions.CompositeSubscription


/**
 * Created by mzennis on 24/05/20.
 */
class PlayBroadcastSocketImpl constructor(
        private val userSession: UserSessionInterface,
        private val cacheHandler: LocalCacheHandler
): PlayBroadcastSocket {

    private var socketInfoListener: PlaySocketInfoListener? = null

    private lateinit var webSocketUtil: RxWebSocketUtil
    private lateinit var compositeSubscription: CompositeSubscription

    private val gson = Gson()
    private var config: SocketConfiguration = SocketConfiguration(
            minReconnectDelay = DEFAULT_DELAY,
            maxRetries = DEFAULT_MAX_RETRIES,
            pingInterval = DEFAULT_PING
    )

    override fun config(minReconnectDelay: Int, maxRetries: Int, pingInterval: Long) {
        this.config = SocketConfiguration(minReconnectDelay, maxRetries, pingInterval)
    }

    override fun socketInfoListener(listener: PlaySocketInfoListener) {
        this.socketInfoListener = listener
    }

    override fun connect(channelId: String, groupChatToken: String) {
        if (channelId.isEmpty()) {
            socketInfoListener?.onError(Throwable("channelId must not be empty"))
            return
        }

        val wsBaseUrl: String = cacheHandler.getString(PlayBroadcastSocket.KEY_GROUP_CHAT_PREFERENCES, TokopediaUrl.getInstance().WS_PLAY)
        var wsConnectUrl = "$wsBaseUrl${PlayBroadcastSocket.KEY_GROUP_CHAT_PATH}$channelId"
        if (groupChatToken.isNotEmpty())
            wsConnectUrl += "&token=$groupChatToken"

        compositeSubscription = CompositeSubscription()

        val subscriber = object : WebSocketSubscriber() {

            override fun onOpen(webSocket: WebSocket) {
                socketInfoListener?.onActive()
            }

            override fun onClose() {
                socketInfoListener?.onClose()
            }

            override fun onMessage(webSocketResponse: WebSocketResponse) {
                if (webSocketResponse.type.isEmpty() || webSocketResponse.jsonElement == null)
                    return

                var data: PlaySocketType? = null
                when(webSocketResponse.type) {
                    PlaySocketEnum.TotalView.value -> {
                        data = mapTotalView(webSocketResponse)
                    }
                    PlaySocketEnum.TotalLike.value -> {
                        data = mapTotalLike(webSocketResponse)
                    }
                    PlaySocketEnum.LiveDuration.value -> {
                        data = mapLiveDuration(webSocketResponse)
                    }
                    PlaySocketEnum.LiveStats.value -> {
                        data = mapLiveStats(webSocketResponse)
                    }
                    PlaySocketEnum.Metric.value -> {
                        data = mapMetric(webSocketResponse)
                    }
                    PlaySocketEnum.ProductTag.value -> {
                        data = mapProductTag(webSocketResponse)
                    }
                    PlaySocketEnum.Chat.value -> {
                        data = mapChat(webSocketResponse)
                    }
                }

                if (data != null) {
                    socketInfoListener?.onReceive(data)
                }
            }

            override fun onError(e: Throwable) {
                socketInfoListener?.onError(e)
            }

            override fun onReconnect() {
                socketInfoListener?.onReconnect()
            }
        }

        webSocketUtil = RxWebSocketUtil.getInstance(
                null,
                delay = config.minReconnectDelay,
                maxRetries = config.maxRetries,
                pingInterval = config.pingInterval
        )
        val subscription = webSocketUtil.getWebSocketInfo(wsConnectUrl, userSession.accessToken)?.
        subscribe(subscriber)
        compositeSubscription.add(subscription)
    }

    override fun close() {
        if (::webSocketUtil.isInitialized)
            webSocketUtil.close()
    }

    override fun destroy() {
        close()
        if (::compositeSubscription.isInitialized)
            compositeSubscription.clear()
    }

    data class SocketConfiguration(
            val minReconnectDelay: Int,
            val maxRetries: Int,
            val pingInterval: Long
    )

    /**
     * Mapping
     */
    private fun mapLiveDuration(response: WebSocketResponse): LiveDuration? {
        return convertToModel(response.jsonObject, LiveDuration::class.java)
    }

    private fun mapLiveStats(response: WebSocketResponse): LiveStats? {
        return convertToModel(response.jsonObject, LiveStats::class.java)
    }

    private fun mapMetric(response: WebSocketResponse): Metric? {
        return convertToModel(response.jsonObject, Metric::class.java)
    }

    private fun mapTotalView(response: WebSocketResponse): TotalView? {
        return convertToModel(response.jsonObject, TotalView::class.java)
    }

    private fun mapTotalLike(response: WebSocketResponse): TotalLike? {
        return convertToModel(response.jsonObject, TotalLike::class.java)
    }

    private fun mapProductTag(response: WebSocketResponse): ProductTagging? {
        return convertToModel(response.jsonObject, ProductTagging::class.java)
    }

    private fun mapChat(response: WebSocketResponse): Chat? {
        return convertToModel(response.jsonObject, Chat::class.java)
    }

    private fun <T> convertToModel(jsonElement: JsonElement?, classOfT: Class<T>): T? {
        try {
            return gson.fromJson(jsonElement, classOfT)
        } catch (e: Exception) {
            sendCrashlyticsLog(0, e.localizedMessage)
        }
        return null
    }
}