package com.tokopedia.topchat.common.websocket

import com.tokopedia.network.authentication.AuthHelper.Companion.getUserAgent
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
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
    private val page: String,
    private val abTestPlatform: AbTestPlatform
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

    override fun reset() {
        webSocket = null
        isDestroyed = false
    }

    private fun generateWsRequest(): Request {
        val requestBuilder = Request.Builder().url(webSocketUrl)
            .header(HEADER_KEY_ORIGIN, TokopediaUrl.getInstance().WEB)
            .header(HEADER_KEY_AUTH, "$HEADER_VALUE_BEARER $token")

        if (isUsingUserAgent()) {
            requestBuilder.header(HEADER_USER_AGENT, getUserAgent())
        }
        if (BuildConfig.DEBUG) {
            requestBuilder.header(HEADER_KEY_PAGE, page)
        }
        return requestBuilder.build()
    }

    private fun isUsingUserAgent(): Boolean {
        return abTestPlatform.getString(KEY_USER_AGENT) == KEY_USER_AGENT
    }

    companion object {
        private const val HEADER_KEY_ORIGIN = "Origin"
        private const val HEADER_KEY_AUTH = "Accounts-Authorization"
        private const val HEADER_KEY_PAGE = "page"
        private const val HEADER_USER_AGENT = "User-Agent"
        private const val HEADER_VALUE_BEARER = "Bearer"

        private const val KEY_USER_AGENT = "chat_useragent"

        const val CODE_NORMAL_CLOSURE = 1000
        const val PAGE_CHATLIST = "chatlist"
        const val PAGE_CHATROOM = "chatroom"
    }
}
