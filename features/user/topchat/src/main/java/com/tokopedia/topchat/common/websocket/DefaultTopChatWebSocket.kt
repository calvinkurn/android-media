package com.tokopedia.topchat.common.websocket

import com.tokopedia.topchat.BuildConfig
import com.tokopedia.url.TokopediaUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class DefaultTopChatWebSocket @Inject constructor(
        private val okHttpClient: OkHttpClient,
        private val webSocketUrl: String,
        private val token: String,
        private val page: String
) : TopchatWebSocket {

    var webSocket: WebSocket? = null
    private var isDestroyed = false

    override fun connectWebSocket(listener: WebSocketListener) {
        if (isDestroyed) return
        val request = generateWsRequest()
        webSocket = okHttpClient.newWebSocket(request, listener)
    }

    override fun close() {
        webSocket?.close(CODE_NORMAL_CLOSURE, "Bye!")
    }

    override fun destroy() {
        isDestroyed = true
    }

    override fun sendPayload(wsPayload: String) {
        webSocket?.send(wsPayload)
    }

    private fun generateWsRequest(): Request {
        val requestBuilder = Request.Builder().url(webSocketUrl)
                .header(HEADER_KEY_ORIGIN, TokopediaUrl.getInstance().WEB)
                .header(HEADER_KEY_AUTH, "$HEADER_VALUE_BEARER $token")
        if (BuildConfig.DEBUG) {
            requestBuilder.header(HEADER_KEY_PAGE, page)
        }
        return requestBuilder.build()
    }

    companion object {
        private const val HEADER_KEY_ORIGIN = "Origin"
        private const val HEADER_KEY_AUTH = "Accounts-Authorization"
        private const val HEADER_KEY_PAGE = "page"

        private const val HEADER_VALUE_BEARER = "Bearer"

        const val CODE_NORMAL_CLOSURE = 1000
        const val PAGE_CHATLIST = "chatlist"
        const val PAGE_CHATROOM = "chatroom"
    }

}