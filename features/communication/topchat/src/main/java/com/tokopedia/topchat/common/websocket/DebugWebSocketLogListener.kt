package com.tokopedia.topchat.common.websocket

import com.google.gson.Gson
import com.tokopedia.analyticsdebugger.debugger.WebSocketLogger
import com.tokopedia.chat_common.data.WebsocketEvent
import com.tokopedia.config.GlobalConfig
import com.tokopedia.topchat.chatlist.domain.pojo.reply.WebSocketResponseData
import com.tokopedia.websocket.WebSocketResponse
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

/**
 * This listener as middleware the WebSocket listener and the logger trace.
 * A main goal is to tracking every single websocket state invoked in topchat.
 *
 * We need to create this mediator to store the log for Logger.
 */
class DebugWebSocketLogListener constructor(
    private val webSocketLogger: WebSocketLogger,
    private val webSocketParser: WebSocketParser,
    private val page: String
) {

    init {
        webSocketLogger.init(
            mapOf(
                "source" to page,
                "code" to "\"\"",
                "messageId" to "\"\"",
            ).toString()
        )
    }

    fun onSendLogMessage(payload: String, header: String = "", code: Int = 0) {
        if (!GlobalConfig.isAllowDebuggingTools()) return
        if (page.isEmpty()) return

        val (response, data) = payload.parse()

        val responseCode = if (code != 0) code else response.code
        val messageId = data?.msgId.toString()

        // Need re-init to send the response code and message id
        webSocketLogger.init(
            mapOf(
                "source" to page,
                "code" to response.code.toString().ifEmpty { "\"\"" },
                "messageId" to messageId.ifEmpty { "\"\"" },
                "header" to header,
            ).toString()
        )

        webSocketLogger.send(
            WebsocketEvent.Event.mapToEventName(responseCode),
            response.jsonElement.toString()
        )
    }

    fun build(listener: WebSocketListener): WebSocketListener {
        if (!GlobalConfig.isAllowDebuggingTools()) return listener

        return object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                webSocketLogger.send("Web Socket Open")
                listener.onOpen(webSocket, response)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val header = webSocket.request().headers.toString()
                onSendLogMessage(
                    payload = text,
                    header = header
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
    }

    private fun String.parse(): Pair<WebSocketResponse, WebSocketResponseData?> {
        val response = webSocketParser.parseResponse(this)

        val data = try {
            Gson().fromJson(response.jsonObject, WebSocketResponseData::class.java)
        } catch (t: Throwable) {
            WebSocketResponseData()
        }

        return Pair(response, data)
    }
}
