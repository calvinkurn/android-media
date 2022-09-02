package com.tokopedia.chatbot.websocket

import com.tokopedia.applink.teleporter.Teleporter.gson
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.network.authentication.HEADER_RELEASE_TRACK
import com.tokopedia.url.TokopediaUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ChatbotWebSocketImpl(
    interceptors: List<Interceptor>?,
    private val accessToken : String,

    ) : ChatbotWebSocket {

    private val client : OkHttpClient

    init {

        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
        builder.pingInterval(DEFAULT_PING, TimeUnit.MILLISECONDS)
        interceptors?.let {
            for (element in interceptors) {
                builder.addInterceptor(element)
            }
        }
        client = builder.build()

    }

    private fun getRequest(url: String, accessToken: String): Request {
        return Request.Builder().get().url(url)
            .header("Origin", TokopediaUrl.getInstance().WEB)
            .header("Accounts-Authorization", "Bearer $accessToken")
            .header("X-Device", "android-" + GlobalConfig.VERSION_NAME)
            .header(HEADER_RELEASE_TRACK, GlobalConfig.VERSION_NAME_SUFFIX)
            .build()
    }

    private val webSocketFlow: MutableSharedFlow<ChatbotWebSocketAction?>
        = MutableSharedFlow()

    private var mWebSocket: WebSocket? = null

    private var webSocketListener = object : WebSocketListener(){

        override fun onOpen(webSocket: WebSocket, response: Response) {
            mWebSocket = webSocket
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val newMessage = ChatbotWebSocketAction.NewMessage(gson.fromJson(text, ChatWebSocketResponse::class.java))
            webSocketFlow.tryEmit(newMessage)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            mWebSocket = null
            webSocketFlow.tryEmit(ChatbotWebSocketAction.Closed(ChatbotWebSocketException(t)))

        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, "Closing Socket")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            mWebSocket = null
        }

    }

    override fun getDataFromSocketAsFlow(): Flow<ChatbotWebSocketAction> {
        return webSocketFlow.filterNotNull().flowOn(Dispatchers.IO)
    }

    override fun send(message: String, interceptors: List<Interceptor>?) {
        mWebSocket?.send(message)
    }

    override fun connect(url: String) {
        close()
        mWebSocket = client.newWebSocket(getRequest(url, accessToken), webSocketListener)
    }

    override fun close() {
        try {
            mWebSocket?.close(1000, "Closing Socket")
        } catch (e: Throwable) {
            Timber.log(1, e)
        }
    }
}