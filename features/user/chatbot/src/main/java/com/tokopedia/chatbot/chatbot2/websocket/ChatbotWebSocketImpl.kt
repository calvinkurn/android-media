package com.tokopedia.chatbot.chatbot2.websocket

import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.teleporter.Teleporter.gson
import com.tokopedia.chatbot.chatbot2.util.ChatbotNewRelicLogger
import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.authentication.HEADER_DEVICE
import com.tokopedia.network.authentication.HEADER_RELEASE_TRACK
import com.tokopedia.url.TokopediaUrl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
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
    private val accessToken: String,
    private val dispatchers: CoroutineDispatchers
) : ChatbotWebSocket {

    private val client: OkHttpClient
    private var isSocketErrorSent = false
    private var isSocketSendMessageError = false

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
            .header(HEADER_ORIGIN, TokopediaUrl.getInstance().WEB)
            .header(HEADER_AUTHORIZATION, "$HEADER_BEARER $accessToken")
            .header(HEADER_DEVICE, HEADER_ANDROID + GlobalConfig.VERSION_NAME)
            .header(HEADER_RELEASE_TRACK, GlobalConfig.VERSION_NAME_SUFFIX)
            .build()
    }

    private val webSocketFlow: MutableSharedFlow<ChatbotWebSocketAction?> = MutableSharedFlow(
        extraBufferCapacity = 100
    )

    private var mWebSocket: WebSocket? = null

    private var webSocketListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            mWebSocket = webSocket
            webSocketFlow.tryEmit(ChatbotWebSocketAction.SocketOpened)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val newMessage = ChatbotWebSocketAction.NewMessage(
                gson.fromJson(
                    text,
                    ChatWebSocketResponse::class.java
                )
            )
            webSocketFlow.tryEmit(newMessage)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            mWebSocket = null
            webSocketFlow.tryEmit(ChatbotWebSocketAction.Failure(ChatbotWebSocketException(t)))
            if (!isSocketErrorSent) {
                isSocketErrorSent = true
                ChatbotNewRelicLogger.logNewRelicForSocket(t)
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(CODE_NORMAL_CLOSURE, SOCKET_NORMAL_CLOSURE_TEXT)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            webSocketFlow.tryEmit(ChatbotWebSocketAction.Closed(code))
            mWebSocket = null
        }
    }

    override fun getDataFromSocketAsFlow(): Flow<ChatbotWebSocketAction> {
        return webSocketFlow.filterNotNull().flowOn(dispatchers.io)
    }

    override fun send(message: JsonObject?, interceptors: List<Interceptor>?) {
        try {
            mWebSocket?.send(message.toString())
            if (GlobalConfig.isAllowDebuggingTools()) {
                Timber.d("Chatbot Socket Message Sent: $message")
            }
        } catch (e: Exception) {
            if (!isSocketSendMessageError) {
                isSocketSendMessageError = true
                ChatbotNewRelicLogger.logNewRelicForSocket(
                    e
                )
            }
        }
    }

    override fun connect(url: String) {
        close()
        mWebSocket = client.newWebSocket(getRequest(url, accessToken), webSocketListener)
    }

    override fun close() {
        try {
            mWebSocket?.close(CODE_NORMAL_CLOSURE, SOCKET_NORMAL_CLOSURE_TEXT)
        } catch (e: Throwable) {
            Timber.log(TIMBER_PRIORITY, e)
        }
    }

    companion object {
        const val CODE_NORMAL_CLOSURE = 1000
        const val SOCKET_NORMAL_CLOSURE_TEXT = "Closing Socket"
        const val HEADER_ORIGIN = "Origin"
        const val HEADER_AUTHORIZATION = "Accounts-Authorization"
        const val HEADER_BEARER = "Bearer"
        const val HEADER_ANDROID = "android-"
        const val TIMBER_PRIORITY = 1
    }
}
