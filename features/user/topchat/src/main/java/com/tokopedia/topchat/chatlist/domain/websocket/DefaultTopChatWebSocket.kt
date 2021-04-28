package com.tokopedia.topchat.chatlist.domain.websocket

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
        private val token: String
) : TopchatWebSocket {

    var webSocket: WebSocket? = null

    override fun connectWebSocket(listener: WebSocketListener) {
        val request = generateWsRequest()
        webSocket = okHttpClient.newWebSocket(request, listener)
    }

    override fun close() {
        webSocket?.close(CODE_NORMAL_CLOSURE, "Bye!")
    }

    private fun generateWsRequest(): Request {
        val requestBuilder = Request.Builder().url(webSocketUrl)
                .header(HEADER_KEY_ORIGIN, TokopediaUrl.getInstance().WEB)
                .header(HEADER_KEY_AUTH, "$HEADER_VALUE_BEARER $token")
        if (BuildConfig.DEBUG) {
            requestBuilder.header(HEADER_KEY_PAGE, HEADER_VALUE_CHATLIST)
        }
        return requestBuilder.build()
    }

    companion object {
        private const val HEADER_KEY_ORIGIN = "Origin"
        private const val HEADER_KEY_AUTH = "Accounts-Authorization"
        private const val HEADER_KEY_PAGE = "page"

        private const val HEADER_VALUE_BEARER = "Bearer"
        private const val HEADER_VALUE_CHATLIST = "chatlist"

        const val CODE_NORMAL_CLOSURE = 1000
    }

}