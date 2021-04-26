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

class DefaultTopChatWebSocket constructor(
        private val okHttpClient: OkHttpClient,
        private val webSocketUrl: String,
        private val token: String
) {

    val textChannel = Channel<WebSocketResponse>()
    var webSocket: WebSocket? = null

    suspend fun createWebSocket() = suspendCoroutine<Channel<WebSocketResponse>> {
        webSocket = okHttpClient.newWebSocket(
                Request.Builder().url(webSocketUrl)
                        .header(HEADER_KEY_ORIGIN, TokopediaUrl.getInstance().WEB)
                        .header(HEADER_KEY_AUTH, "$HEADER_VALUE_BEARER $token")
                        .build(),
                object : WebSocketListener() {
                    override fun onOpen(webSocket: WebSocket, response: Response) {
                        Timber.d(" Open")
                        it.resume(textChannel)
                    }

                    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                        Timber.d("Failure $t")
                    }

                    override fun onClosing(webSocket: WebSocket, code: Int, reason: String?) {
                        Timber.d(" Closing")
                        webSocket.close(1000, "Bye!")
                    }

                    override fun onMessage(webSocket: WebSocket, text: String) {
                        runBlocking {
                            Timber.d(" Message $text")
                            textChannel.send(GsonBuilder()
                                    .create()
                                    .fromJson(text, WebSocketResponse::class.java))
                        }
                    }

                    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                        Timber.d(" Bytes $bytes")
                    }

                    override fun onClosed(webSocket: WebSocket, code: Int, reason: String?) {
                        Timber.d(" Closed $reason")
                        textChannel.close()
                    }
                }
        )
    }

    fun connectWebsocket(listener: WebSocketListener) {
        okHttpClient.newWebSocket(
                Request.Builder().url(webSocketUrl)
                        .header(HEADER_KEY_ORIGIN, TokopediaUrl.getInstance().WEB)
                        .header(HEADER_KEY_AUTH, "$HEADER_VALUE_BEARER $token")
                        .build(),
                listener
        )
    }

    fun cancel() {
        textChannel.close()
        webSocket?.close(1000, "Bye!")
    }


    // TODO: remove later
//    enum class Event {
//        ON_OPEN,
//        ON_FAILURE,
//        ON_CLOSING,
//        ON_MESSAGE,
//        ON_CLOSED
//    }
//
//    class WsEvent<out T> private constructor(
//            val status: Status,
//            val data: T?,
//            val throwable: Throwable?,
//    ) {
//
//        var referer: Any? = null
//
//        companion object {
//            fun <T> success(data: T?): WsEvent<T> {
//                return WsEvent(Status.SUCCESS, data, null)
//            }
//
//            fun <T> error(throwable: Throwable, data: T?): WsEvent<T> {
//                return WsEvent(Status.ERROR, data, throwable)
//            }
//
//            fun <T> loading(data: T?): WsEvent<T> {
//                return WsEvent(Status.LOADING, data, null)
//            }
//
//            fun <T> empty(): WsEvent<T> {
//                return WsEvent(Status.EMPTY, null, null)
//            }
//        }
//    }

    companion object {
        private const val HEADER_KEY_ORIGIN = "Origin"
        private const val HEADER_KEY_AUTH = "Accounts-Authorization"

        private const val HEADER_VALUE_BEARER = "Bearer"
    }

}