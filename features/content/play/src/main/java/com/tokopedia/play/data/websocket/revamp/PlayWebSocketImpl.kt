package com.tokopedia.play.data.websocket.revamp

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.authentication.HEADER_RELEASE_TRACK
import com.tokopedia.config.GlobalConfig
import com.tokopedia.trackingoptimizer.gson.GsonSingleton
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.websocket.WebSocketResponse
import kotlinx.coroutines.flow.*
import okhttp3.*
import okio.ByteString

/**
 * Created by jegul on 09/03/21
 */
class PlayWebSocketImpl(
        private val client: OkHttpClient,
        private val userSession: UserSessionInterface
) : PlayWebSocket {

    private val gson: Gson
        get() = GsonSingleton.instance

    private val webSocketFlow: MutableStateFlow<WebSocketResponse?> = MutableStateFlow(null)

    private var mWebSocket: WebSocket? = null

    private val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            mWebSocket = webSocket
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            webSocketFlow.value = gson.fromJson(text, WebSocketResponse::class.java)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, "Closing Socket")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            webSocketFlow.value = null
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        }
    }

    override fun connect(url: String) {
        client.newWebSocket(getRequest(url, userSession.accessToken), webSocketListener)
    }

    override fun close() {
        mWebSocket?.close(1000, "Closing Socket")
    }

    override fun listenAsFlow(): Flow<WebSocketResponse> {
        return webSocketFlow.filterNotNull()
    }

    override fun send(message: String) {
        //TODO()
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