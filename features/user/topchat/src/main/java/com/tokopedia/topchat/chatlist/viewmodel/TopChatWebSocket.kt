package com.tokopedia.topchat.chatlist.viewmodel

import com.google.gson.GsonBuilder
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.websocket.WebSocketResponse
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okio.ByteString
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TopChatWebSocket(
        private val okHttpClient: OkHttpClient,
        private val webSocketUrl: String,
        private val token: String
) {

    val textChannel = Channel<WebSocketResponse>()
    var webSocket: WebSocket? = null

    suspend fun createWebSocket() = suspendCoroutine<Channel<WebSocketResponse>> {
        okHttpClient.newWebSocket(
                Request.Builder().url(webSocketUrl)
                        .header("Origin", TokopediaUrl.getInstance().WEB)
                        .header("Accounts-Authorization",
                                "Bearer $token")
                        .build(),

                object : WebSocketListener() {
                    override fun onOpen(webSocket: WebSocket, response: Response) {
                        this@TopChatWebSocket.webSocket = webSocket

                        Timber.d(" Open")
                        it.resume(textChannel)
                    }

                    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                        Timber.d( "Failure $t")
                    }

                    override fun onClosing(webSocket: WebSocket, code: Int, reason: String?) {
                        Timber.d(" Closing")
                        webSocket.close(1000, "Bye!")
                    }

                    override fun onMessage(webSocket: WebSocket, text: String) {
                        runBlocking {
                            Timber.d( " Message $text")
                            textChannel.send(GsonBuilder()
                                    .create()
                                    .fromJson(text, WebSocketResponse::class.java))
                        }
                    }

                    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                        Timber.d( " Bytes $bytes")
                    }

                    override fun onClosed(webSocket: WebSocket, code: Int, reason: String?) {
                        Timber.d(" Closed $reason")
                        textChannel.close()
                    }
                }
        )
    }

    fun cancel() {
        webSocket?.cancel()
        textChannel.close()
    }

}