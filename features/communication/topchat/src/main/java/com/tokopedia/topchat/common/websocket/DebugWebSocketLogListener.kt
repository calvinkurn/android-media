package com.tokopedia.topchat.common.websocket

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tokopedia.analyticsdebugger.debugger.ws.TopchatWebSocketLogger
import com.tokopedia.chat_common.data.WebsocketEvent
import com.tokopedia.config.GlobalConfig
import com.tokopedia.topchat.chatlist.domain.pojo.reply.WebSocketResponseData
import okhttp3.Headers
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
    context: Context,
    private val webSocketParser: WebSocketParser,
    private val page: String
) {

    private val webSocketLogger = TopchatWebSocketLogger(context)

    fun build(listener: WebSocketListener): WebSocketListener {
        if (!GlobalConfig.isAllowDebuggingTools()) return listener

        webSocketLogger.init(
            mapOf(
                "source" to page
            ).toString()
        )

        return object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                listener.onOpen(webSocket, response)

                webSocketLogger.send("Web Socket Open")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                listener.onMessage(webSocket, text)

                val response = webSocketParser.parseResponse(text)
                val data = Gson().fromJson(response.jsonObject, WebSocketResponseData::class.java)
                val messageId = data?.msgId.toString()

                // Need re-init to send the response code and message id
                webSocketLogger.init(
                    mapOf(
                        "source" to page.ifEmpty { "\"\"" },
                        "code" to response.code.toString().ifEmpty { "\"\"" },
                        "messageId" to messageId.ifEmpty { "\"\"" },
                        "header" to headerRequest(webSocket.request().headers)
                    ).toString()
                )

                webSocketLogger.send(
                    WebsocketEvent.Event.mapToEventName(response.code),
                    response.jsonElement.toString()
                )
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                listener.onMessage(webSocket, bytes)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                listener.onClosing(webSocket, code, reason)
                webSocketLogger.send("Web Socket Closing")
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
    }

    private fun headerRequest(header: Headers): String {
        val headers = mutableMapOf<String, String>().apply {
            for (i in 0 until header.size) {
                put(header.name(i), header.value(i))
            }
        }

        return GsonBuilder()
            .setPrettyPrinting()
            .create()
            .toJson(headers)
    }
}
