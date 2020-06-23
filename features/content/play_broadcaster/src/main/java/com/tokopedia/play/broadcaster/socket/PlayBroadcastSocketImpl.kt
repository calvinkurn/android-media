package com.tokopedia.play.broadcaster.socket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.crashlytics.android.Crashlytics
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.config.GlobalConfig
import com.tokopedia.play.broadcaster.domain.model.ConcurrentUser
import com.tokopedia.play.broadcaster.domain.model.LiveDuration
import com.tokopedia.play.broadcaster.domain.model.LiveStats
import com.tokopedia.play.broadcaster.domain.model.Metric
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.RxWebSocketUtil
import com.tokopedia.websocket.WebSocketResponse
import com.tokopedia.websocket.WebSocketSubscriber
import okhttp3.WebSocket
import rx.subscriptions.CompositeSubscription
import java.lang.reflect.Type


/**
 * Created by mzennis on 24/05/20.
 */
class PlayBroadcastSocketImpl constructor(
        private val userSession: UserSessionInterface,
        private val cacheHandler: LocalCacheHandler
): PlayBroadcastSocket {

    var socketInfoListener: PlaySocketInfoListener? = null

    private lateinit var compositeSubscription: CompositeSubscription
    private val gson = Gson()

    private val _observableResponseMessage = MutableLiveData<PlaySocketType>()

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
                    PlaySocketEnum.ConcurrentUser.value -> {
                        data = mapConcurrentUser(webSocketResponse)
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
                }

                if (data != null) {
                    _observableResponseMessage.postValue(data)
                }
            }

            override fun onError(e: Throwable) {
                socketInfoListener?.onError(e)
            }

            override fun onReconnect() {
                socketInfoListener?.onReconnect()
            }
        }

        val config = configuration()
        val webSocketUtil = RxWebSocketUtil.getInstance(null, config.minReconnectDelay, config.maxRetries, config.pingInterval)

        val subscription = webSocketUtil.getWebSocketInfo(wsConnectUrl, userSession.accessToken)?.
        subscribe(subscriber)
        compositeSubscription.add(subscription)
    }

    override fun destroy() {
        if (::compositeSubscription.isInitialized)
            compositeSubscription.clear()
    }

    override fun getObservablePlaySocketMessage(): LiveData<out PlaySocketType> = _observableResponseMessage

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

    private fun mapConcurrentUser(response: WebSocketResponse): ConcurrentUser? {
        return convertToModel(response.jsonObject, ConcurrentUser::class.java)
    }

    private fun <T> convertToModel(jsonElement: JsonElement?, classOfT: Class<T>): T? {
        try {
            return gson.fromJson(jsonElement, classOfT)
        } catch (e: Exception) {
            if (!GlobalConfig.DEBUG) {
                Crashlytics.log(0, PlayBroadcastSocket.TAG, e.localizedMessage)
            }
        }
        return null
    }

    private fun <T> convertToModel(jsonElement: JsonElement?, typeOfT: Type): T? {
        try {
            return gson.fromJson(jsonElement, typeOfT)
        } catch (e: Exception) {
            if (!GlobalConfig.DEBUG) {
                Crashlytics.log(0, PlayBroadcastSocket.TAG, e.localizedMessage)
            }
        }
        return null
    }
}