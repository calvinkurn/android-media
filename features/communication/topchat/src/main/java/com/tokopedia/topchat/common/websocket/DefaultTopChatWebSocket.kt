package com.tokopedia.topchat.common.websocket

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iris.util.Session
import com.tokopedia.network.authentication.AuthHelper.Companion.getUserAgent
import com.tokopedia.url.TokopediaUrl
import okhttp3.*
import javax.inject.Inject

class DefaultTopChatWebSocket @Inject constructor(
    @ApplicationContext context: Context,
    private val okHttpClient: OkHttpClient,
    private val webSocketUrl: String,
    private val token: String,
    private val page: String,
    private val irisSession: Session,
    webSocketParser: WebSocketParser
) : TopchatWebSocket {

    private var webSocket: WebSocket? = null
    private var isDestroyed = false

    private val webSocketListener = DebugWebSocketLogListener(
        context,
        webSocketParser,
        page
    )

    override fun connectWebSocket(listener: WebSocketListener) {
        if (isDestroyed) return

        /**
         * Create only one connection at time
         */
        if (webSocket == null) {
            webSocket = okHttpClient.newWebSocket(
                generateWsRequest(),
                webSocketListener.build(listener)
            )
        }
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
        webSocket?.cancel()
        webSocket = null
    }

    private fun generateWsRequest(): Request {
        val requestBuilder = Request.Builder().url(webSocketUrl)
            .header(HEADER_KEY_ORIGIN, TokopediaUrl.getInstance().WEB)
            .header(HEADER_KEY_AUTH, "$HEADER_VALUE_BEARER $token")
            .header(HEADER_IRIS_SESSION_ID, irisSession.getSessionId())
            .header(HEADER_USER_AGENT, getUserAgent())

        if (GlobalConfig.isAllowDebuggingTools()) {
            requestBuilder.header(HEADER_KEY_PAGE, page)
        }
        return requestBuilder.build()
    }

    companion object {
        private const val HEADER_KEY_ORIGIN = "Origin"
        private const val HEADER_KEY_AUTH = "Accounts-Authorization"
        private const val HEADER_KEY_PAGE = "page"
        private const val HEADER_USER_AGENT = "User-Agent"
        private const val HEADER_VALUE_BEARER = "Bearer"
        private const val HEADER_IRIS_SESSION_ID = "X-Iris-Session-Id"

        const val CODE_NORMAL_CLOSURE = 1000
        const val PAGE_CHATLIST = "chatlist"
        const val PAGE_CHATROOM = "chatroom"
    }
}
