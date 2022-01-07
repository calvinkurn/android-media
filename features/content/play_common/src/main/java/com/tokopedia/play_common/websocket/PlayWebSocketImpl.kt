package com.tokopedia.play_common.websocket

import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.WebSocketLogger
import com.tokopedia.authentication.HEADER_RELEASE_TRACK
import com.tokopedia.config.GlobalConfig
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
        private val context: Context,
        private val localCacheHandler: LocalCacheHandler,
) : PlayWebSocket {

    private val client: OkHttpClient

    init {
        clientBuilder.pingInterval(DEFAULT_PING, TimeUnit.MILLISECONDS)
        client = clientBuilder.build()
    }

    private val gson: Gson = Gson()

    private val webSocketFlow: MutableSharedFlow<WebSocketAction?> = MutableSharedFlow(
            extraBufferCapacity = 100
    )

    private var mWebSocket: WebSocket? = null

    private val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            mWebSocket = webSocket
            WebSocketLogger.getInstance(context).send("Web Socket Open")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val newMessage = WebSocketAction.NewMessage(gson.fromJson(text, WebSocketResponse::class.java))
            webSocketFlow.tryEmit(newMessage)
            WebSocketLogger.getInstance(context).send(newMessage.message.type, newMessage.message.jsonElement.toString())
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, "Closing Socket")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            mWebSocket = null
            webSocketFlow.tryEmit(WebSocketAction.Closed(WebSocketClosedReason.Intended))
            WebSocketLogger.getInstance(context).send("Web Socket Close (Intended)")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            mWebSocket = null
            webSocketFlow.tryEmit(WebSocketAction.Closed(WebSocketClosedReason.Error(t)))
            WebSocketLogger.getInstance(context).send("Web Socket Close (Error)")
        }
    }

    override fun connect(channelId: String, gcToken: String, source: String) {
        close()
        val url = generateUrl(channelId, gcToken)
        mWebSocket = client.newWebSocket(getRequest(url, userSession.accessToken), webSocketListener)
        WebSocketLogger.getInstance(context).init(buildGeneralInfo(channelId, gcToken, source).toString())
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

    private fun generateUrl(channelId: String, gcToken: String): String {
        val wsBaseUrl = localCacheHandler.getString(
            KEY_GROUPCHAT_DEVELOPER_OPTION_PREFERENCES,
            TokopediaUrl.getInstance().WS_PLAY
        )

        return buildString {
            append("$wsBaseUrl$PLAY_WEB_SOCKET_GROUP_CHAT$channelId")
            if (gcToken.isNotEmpty()) append("&token=$gcToken")
        }
    }

    private fun getRequest(url: String, accessToken: String): Request {
        return Request.Builder().get().url(url)
                .header("Origin", TokopediaUrl.getInstance().WEB)
                .header("Accounts-Authorization", "Bearer $accessToken")
                .header("X-Device", "android-" + GlobalConfig.VERSION_NAME)
                .header(HEADER_RELEASE_TRACK, GlobalConfig.VERSION_NAME_SUFFIX)
                .build()
    }

    private fun buildGeneralInfo(channelId: String, gcToken: String, source: String): Map<String, String> {
        return mapOf(
            "source" to if(source.isEmpty()) "\"\"" else source,
            "channelId" to if(channelId.isEmpty()) "\"\"" else channelId,
            "gcToken" to if(gcToken.isEmpty()) "\"\"" else gcToken,
        )
    }

    companion object {
        private const val PLAY_WEB_SOCKET_GROUP_CHAT = "/ws/groupchat?channel_id="

        private const val KEY_GROUPCHAT_DEVELOPER_OPTION_PREFERENCES = "ip_groupchat"
    }
}