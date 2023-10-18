package com.tokopedia.play_common.websocket

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.WebSocketLogger
import com.tokopedia.analyticsdebugger.debugger.ws.PlayWebSocketLogger
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.network.authentication.HEADER_RELEASE_TRACK
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.*
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

/**
 * Created by jegul on 09/03/21
 */
class PlayWebSocketImpl(
    clientBuilder: OkHttpClient.Builder,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers,
    @ApplicationContext private val context: Context,
    private val localCacheHandler: LocalCacheHandler
) : PlayWebSocket {

    private val client: OkHttpClient

    private val webSocketLogger: WebSocketLogger by lazy {
        if (GlobalConfig.isAllowDebuggingTools()) {
            PlayWebSocketLogger(context)
        } else {
            object : WebSocketLogger {
                override fun init(data: String) {}
                override fun send(event: String) {}
                override fun send(event: String, message: String) {}
            }
        }
    }

    private val deviceVersion: String
        get() = "android-${GlobalConfig.VERSION_NAME}"

    private val appName: String
        get() = GlobalConfig.getPackageApplicationName()

    init {
        clientBuilder.pingInterval(DEFAULT_PING, TimeUnit.MILLISECONDS)
        client = clientBuilder
            .addInterceptor {
                val request = it.request().newBuilder()
                    .addHeader(HEADER_USER_AGENT, AuthHelper.getUserAgent())
                    .addHeader(HEADER_ACCEPT, HEADER_VALUE_CONTENT_TYPE_JSON)
                    .build()
                it.proceed(request)
            }
            .build()
    }

    private val gson: Gson = Gson()

    private val webSocketFlow: MutableSharedFlow<WebSocketAction?> = MutableSharedFlow(
        extraBufferCapacity = 100
    )

    private var mWebSocket: WebSocket? = null

    private val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            mWebSocket = webSocket
            webSocketLogger.send("Web Socket Open")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val newMessage = WebSocketAction.NewMessage(gson.fromJson(text, WebSocketResponse::class.java))
            webSocketFlow.tryEmit(newMessage)

            /**
             * Send tracking Id
             */
            val trackingParam = sendTracking(newMessage.message.tracking.id)
            webSocketLogger.send(newMessage.message.type, newMessage.message.jsonElement.toString() + trackingParam)

            if (!newMessage.message.isTrackingIdAvailable) return
            webSocket.send(trackingParam)
            webSocketLogger.send(PARAM_SEND_TYPE_TRACKING, trackingParam)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, "Closing Socket")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            mWebSocket = null
            webSocketFlow.tryEmit(WebSocketAction.Closed(WebSocketClosedReason.Intended))
            webSocketLogger.send("Web Socket Close (Intended)")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            mWebSocket = null
            webSocketFlow.tryEmit(WebSocketAction.Closed(WebSocketClosedReason.Error(t)))
            webSocketLogger.send("Web Socket Close (Error)")
        }
    }

    override fun connect(channelId: String, warehouseId: String, gcToken: String, source: String) {
        close()
        val url = generateUrl(channelId, warehouseId, gcToken)
        mWebSocket = client.newWebSocket(getRequest(url, userSession.accessToken), webSocketListener)
        webSocketLogger.init(buildGeneralInfo(channelId, warehouseId, gcToken, source).toString())
    }

    override fun close() {
        try {
            mWebSocket?.close(1000, "Closing Socket")
        } catch (e: Throwable) { }
    }

    override fun listenAsFlow(): Flow<WebSocketAction> {
        return webSocketFlow.filterNotNull().buffer().flowOn(dispatchers.io)
    }

    override fun send(message: String) {
        mWebSocket?.send(message)
    }

    private fun generateUrl(channelId: String, warehouseId: String, gcToken: String): String {
        val wsBaseUrl = localCacheHandler.getString(
            KEY_GROUPCHAT_DEVELOPER_OPTION_PREFERENCES,
            TokopediaUrl.getInstance().WS_PLAY
        )

        return buildString {
            append("$wsBaseUrl$PLAY_WEB_SOCKET_GROUP_CHAT$channelId$PLAY_WEB_SOCKET_GROUP_CHAT_WH$warehouseId$PLAY_WEB_SOCKET_X_DEVICE$deviceVersion$PLAY_WEB_SOCKET_X_APP_NAME$appName")
            if (gcToken.isNotEmpty()) append("&token=$gcToken")
        }
    }

    private fun buildGeneralInfo(channelId: String, warehouseId: String, gcToken: String, source: String): Map<String, String> {
        return mapOf(
            "source" to source.ifEmpty { "\"\"" },
            "channelId" to channelId.ifEmpty { "\"\"" },
            "warehouseId" to warehouseId.ifEmpty { "\"\"" },
            "gcToken" to gcToken.ifEmpty { "\"\"" },
            "header" to deviceVersion.ifEmpty { "\"\"" }
        )
    }

    private fun getRequest(url: String, accessToken: String): Request {
        return Request.Builder().get().url(url)
            .header("Origin", TokopediaUrl.getInstance().WEB)
            .header("Accounts-Authorization", "Bearer $accessToken")
            .header("X-Device", "android-" + GlobalConfig.VERSION_NAME)
            .header(HEADER_RELEASE_TRACK, GlobalConfig.VERSION_NAME_SUFFIX)
            .build()
    }

    private fun sendTracking(trackingId: String): String {
        val param = JsonObject()
        param.addProperty(PARAM_SEND_TYPE_TRACKING_ID, trackingId)

        val bundle = JsonObject()
        bundle.addProperty(PARAM_SEND_TYPE, PARAM_SEND_TYPE_TRACKING)
        bundle.add(PARAM_SEND_TYPE_TRACKING_FIELD, param)

        return bundle.toString()
    }

    companion object {
        private const val PLAY_WEB_SOCKET_GROUP_CHAT = "/ws/groupchat?channel_id="
        private const val PLAY_WEB_SOCKET_GROUP_CHAT_WH = "&warehouse_id="
        private const val PLAY_WEB_SOCKET_X_DEVICE = "&x_device="
        private const val PLAY_WEB_SOCKET_X_APP_NAME = "&x_app_name="

        private const val KEY_GROUPCHAT_DEVELOPER_OPTION_PREFERENCES = "ip_groupchat"

        private const val HEADER_USER_AGENT = "User-Agent"
        private const val HEADER_ACCEPT = "Accept"

        private const val HEADER_VALUE_CONTENT_TYPE_JSON = "application/json"

        private const val PARAM_SEND_TYPE = "type"
        private const val PARAM_SEND_TYPE_TRACKING = "TRACKING"
        private const val PARAM_SEND_TYPE_TRACKING_ID = "id"
        private const val PARAM_SEND_TYPE_TRACKING_FIELD = "tracking"
    }
}
