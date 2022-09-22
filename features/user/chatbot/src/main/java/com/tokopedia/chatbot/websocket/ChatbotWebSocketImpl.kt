package com.tokopedia.chatbot.websocket

import android.util.Log
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.teleporter.Teleporter.gson
import com.tokopedia.config.GlobalConfig
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
    private val dispatchers: CoroutineDispatchers,
) : ChatbotWebSocket {

    private val client: OkHttpClient

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

    private val webSocketFlow: MutableSharedFlow<ChatbotWebSocketAction?> = MutableSharedFlow(
        extraBufferCapacity = 100
    )

    private var mWebSocket: WebSocket? = null

    var onChatListener : ChatbotSocketStateListener? = null

    private var webSocketListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.d("Eren", " In IMPL onOpen: ")
            mWebSocket = webSocket
            onChatListener?.onOpen()
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("Eren", " In IMPL onMessage(text): ")
            val newMessage = ChatbotWebSocketAction.NewMessage(
                gson.fromJson(
                    text,
                    ChatWebSocketResponse::class.java
                )
            )
            webSocketFlow.tryEmit(newMessage)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.d("Eren", " In IMPL onMessage(bytes): ")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d("Eren", " In IMPL onFailure: $t")
            mWebSocket = null
            webSocketFlow.tryEmit(ChatbotWebSocketAction.Closed(ChatbotWebSocketException(t)))
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("Eren", " In IMPL onClosing: $reason $code")
            webSocket.close(1000, "Closing Socket")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("Eren", " In IMPL onClosed: $reason $code")
            mWebSocket = null
        }
    }

    //TODO change this
    override fun getDataFromSocketAsFlow(): Flow<ChatbotWebSocketAction> {
        return webSocketFlow.filterNotNull().flowOn(dispatchers.main)
    }

    //TODO add null check
    override fun send(message: JsonObject?, interceptors: List<Interceptor>?) {
        mWebSocket?.send(message.toString())
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

    override fun setOnOpenListener(listener : ChatbotSocketStateListener) {
        onChatListener = listener
    }
}