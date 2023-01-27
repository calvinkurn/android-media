package com.tokopedia.topchat.common.websocket

import com.google.gson.Gson
import com.tokopedia.analyticsdebugger.debugger.WebSocketLogger
import com.tokopedia.chat_common.data.WebsocketEvent
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iris.util.Session
import com.tokopedia.network.authentication.AuthHelper.Companion.getUserAgent
import com.tokopedia.topchat.chatlist.domain.pojo.reply.WebSocketResponseData
import com.tokopedia.url.TokopediaUrl
import okhttp3.*
import okio.ByteString
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
            webSocketLogger.send("Web Socket Open")
            listener.onOpen(webSocket, response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val response = webSocketParser.parseResponse(text)
            val data = Gson().fromJson(response.jsonObject, WebSocketResponseData::class.java)
            val messageId = data?.msgId.toString()

            webSocketLogger.init(
                buildDetailInfo(
                    response.code,
                    messageId
                ).toString()
            )

            webSocketLogger.send(
                WebsocketEvent.Event.mapToEventName(response.code),
                response.jsonElement.toString()
            )

            listener.onMessage(webSocket, text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            listener.onMessage(webSocket, bytes)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocketLogger.send("Web Socket Closing")
            listener.onClosing(webSocket, code, reason)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            webSocketLogger.send("Web Socket Close (Intended)")
            listener.onClosed(webSocket, code, reason)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            webSocketLogger.send("Web Socket Close (Error)")
            listener.onFailure(webSocket, t, response)
        }
    }

    private fun buildDetailInfo(code: Int? = 0, messageId: String? = "") = mapOf(
        "source" to page.ifEmpty { "\"\"" },
        "code" to code.toString().ifEmpty { "\"\"" },
        "messageId" to messageId?.ifEmpty { "\"\"" },
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
