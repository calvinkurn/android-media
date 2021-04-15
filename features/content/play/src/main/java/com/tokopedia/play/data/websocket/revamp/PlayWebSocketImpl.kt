package com.tokopedia.play.data.websocket.revamp

import com.google.gson.Gson
import com.tokopedia.authentication.HEADER_RELEASE_TRACK
import com.tokopedia.config.GlobalConfig
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.trackingoptimizer.gson.GsonSingleton
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.DEFAULT_PING
import com.tokopedia.websocket.WebSocketResponse
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
) : PlayWebSocket {

    private val client: OkHttpClient

    init {
        clientBuilder.pingInterval(DEFAULT_PING, TimeUnit.MILLISECONDS)
        client = clientBuilder.build()
    }

    private val gson: Gson
        get() = GsonSingleton.instance

    private val webSocketFlow: MutableSharedFlow<WebSocketAction?> = MutableSharedFlow(
            extraBufferCapacity = 50
    )

    private var mWebSocket: WebSocket? = null

    private val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            mWebSocket = webSocket
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            webSocketFlow.tryEmit(WebSocketAction.NewMessage(gson.fromJson(text, WebSocketResponse::class.java)))
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, "Closing Socket")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            mWebSocket = null
            webSocketFlow.tryEmit(WebSocketAction.Closed(WebSocketClosedReason.Intended))
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            mWebSocket = null
            webSocketFlow.tryEmit(WebSocketAction.Closed(WebSocketClosedReason.Error))
        }
    }

    override fun connect(url: String) {
        close()
        client.newWebSocket(getRequest(url, userSession.accessToken), webSocketListener)
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

    private fun getRequest(url: String, accessToken: String): Request {
        return Request.Builder().get().url(url)
                .header("Origin", TokopediaUrl.getInstance().WEB)
                .header("Accounts-Authorization", "Bearer $accessToken")
                .header("X-Device", "android-" + GlobalConfig.VERSION_NAME)
                .header(HEADER_RELEASE_TRACK, GlobalConfig.VERSION_NAME_SUFFIX)
                .build()
    }
}