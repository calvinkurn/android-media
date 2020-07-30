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

/**
 * @author : Steven 2019-08-09
 */
class EasyWS(val webSocket: WebSocket, val response: Response) {
    val textChannel = Channel<WebSocketResponse>()
}

suspend fun OkHttpClient.easyWebSocket(url: String, accessToken: String) = suspendCoroutine<EasyWS> {
    val TAG = "coroutineWebSocket"
    var easyWs: EasyWS? = null
    newWebSocket(
            Request.Builder().url(url)
                    .header("Origin", TokopediaUrl.getInstance().WEB)
                    .header("Accounts-Authorization",
                            "Bearer $accessToken")
                    .build(),
            object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    easyWs = EasyWS(webSocket, response)
                    easyWs?.let{ws ->
                        it.resume(ws)
                    }
                    Timber.d(" Open")
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    Timber.d( "Failure $t")
//                    it.resumeWithException(t)
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String?) {
                    Timber.d(" Closing")
                    webSocket.close(1000, "Bye!")
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    runBlocking {
                        Timber.d( " Message $text")
                        val data = GsonBuilder().create().fromJson(text, WebSocketResponse::class.java)
                        easyWs?.textChannel?.send(data)
                    }
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    Timber.d( " Bytes $bytes")
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String?) {
                    Timber.d(" Closed $reason")
                    easyWs?.textChannel?.close()
                }
            }
    )
}