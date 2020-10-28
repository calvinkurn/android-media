package com.tokopedia.play.broadcaster.socket

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.config.GlobalConfig
import com.tokopedia.play.broadcaster.domain.model.*
import com.tokopedia.play.broadcaster.util.extension.sendCrashlyticsLog
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.*
import okhttp3.WebSocket
import rx.subscriptions.CompositeSubscription
import java.lang.reflect.Type


/**
 * Created by mzennis on 24/05/20.
 * https://tokopedia.atlassian.net/wiki/spaces/CN/pages/601065014/New+Chat+Room+Socket+Contract
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

    private val newMetricListType: Type = object: TypeToken<List<NewMetricList.NewMetric>>(){}.type

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
                        data = convertToModel(webSocketResponse.jsonObject, TotalView::class.java)
                    }
                    PlaySocketEnum.TotalLike.value -> {
                        data = convertToModel(webSocketResponse.jsonObject, TotalLike::class.java)
                    }
                    PlaySocketEnum.LiveDuration.value -> {
                        data = convertToModel(webSocketResponse.jsonObject, LiveDuration::class.java)
                    }
                    PlaySocketEnum.LiveStats.value -> {
                        data = convertToModel(webSocketResponse.jsonObject, LiveStats::class.java)
                    }
                    PlaySocketEnum.NewMetric.value -> {
                        data = NewMetricList(convertToModel(webSocketResponse.jsonArray, newMetricListType) ?: emptyList())
                    }
                    PlaySocketEnum.ProductTag.value -> {
                        data = convertToModel(webSocketResponse.jsonObject, ProductTagging::class.java)
                    }
                    PlaySocketEnum.Chat.value -> {
                        data = convertToModel(webSocketResponse.jsonObject, Chat::class.java)
                    }
                    PlaySocketEnum.Freeze.value -> {
                        data = convertToModel(webSocketResponse.jsonObject, Freeze::class.java)
                    }
                    PlaySocketEnum.Banned.value -> {
                        data = convertToModel(webSocketResponse.jsonObject, Banned::class.java)
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

    private fun <T> convertToModel(jsonElement: JsonElement?, classOfT: Class<T>): T? {
        try {
            return gson.fromJson(jsonElement, classOfT)
        } catch (e: Exception) {
            sendCrashlyticsLog(e)
        }
        return null
    }

    private fun <T> convertToModel(jsonElement: JsonElement?, typeOfT: Type): T? {
        try {
            return gson.fromJson(jsonElement, typeOfT)
        } catch (e: Exception) {
            if (!GlobalConfig.DEBUG) {
                FirebaseCrashlytics.getInstance().log("E/${TAG}: ${e.localizedMessage}")
            }
        }
        return null
    }

    companion object {
        private const val TAG = "PlayBroadcastSocketImpl"
    }
}