package com.tokopedia.topchat.common.websocket

import com.tokopedia.analyticsdebugger.debugger.WebSocketLogger
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iris.util.Session
import com.tokopedia.network.authentication.AuthHelper.Companion.getUserAgent
import com.tokopedia.topchat.common.websocket.logger.parseInitPayload
import com.tokopedia.url.TokopediaUrl
import okhttp3.*
import javax.inject.Inject

class DefaultTopChatWebSocket @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val webSocketUrl: String,
    private val token: String,
    private val page: String,
    private val irisSession: Session,
    private val webSocketParser: WebSocketParser,
    private val webSocketLogger: WebSocketLogger
) : TopchatWebSocket {

    var webSocket: WebSocket? = null
    private var isDestroyed = false

    override fun connectWebSocket(listener: WebSocketListener) {
        if (isDestroyed) return

        webSocketLogger.init(buildDetailInfo().toString())
        webSocket = okHttpClient.newWebSocket(
            generateWsRequest(),
            webSocketListener(listener)
        )
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
            .header(HEADER_IRIS_SESSION_ID, irisSession.getSessionId())
            .header(HEADER_USER_AGENT, getUserAgent())

        if (GlobalConfig.isAllowDebuggingTools()) {
            requestBuilder.header(HEADER_KEY_PAGE, page)
        }
        return requestBuilder.build()
    }

    /**
     * This listener only for middleware-ing the WebSocket listener.
     * The main purpose to track every single ws behavior in active
     * listen active of subscriber.
     *
     * We need to create this middleware to store the log for Chucker.
     */
    private fun webSocketListener(listener: WebSocketListener) = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            listener.onOpen(webSocket, response)
            webSocketLogger.send("Web Socket Open")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            listener.onMessage(webSocket, text)

            // Parse to get the code and messageId only
            val parseSimplePayload = parseInitPayload(text)
            webSocketLogger.init(
                buildDetailInfo(
                    parseSimplePayload?.code,
                    parseSimplePayload?.messageId
                ).toString()
            )

            // Parse for common type
            val response = webSocketParser.parseResponse(text)
            webSocketLogger.send(response.type, response.jsonElement.toString())
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            listener.onClosing(webSocket, code, reason)
            webSocket.close(1000, "Closing Socket")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            listener.onClosed(webSocket, code, reason)
            webSocketLogger.send("Web Socket Close (Intended)")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            listener.onFailure(webSocket, t, response)
            webSocketLogger.send("Web Socket Close (Error)")
        }
    }

    private fun buildDetailInfo(code: Int? = 0, messageId: String? = "") = mapOf(
        "source" to page,
        "url" to webSocketUrl,
        "code" to code.toString(),
        "messageId" to messageId,
    )

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
