package com.tokopedia.websocket

import com.google.gson.GsonBuilder
import okhttp3.WebSocket
import okio.ByteString

/**
 * Created by Steven on 2018/12.
 */

class WebSocketInfo {

    var webSocket: WebSocket? = null

    var response: WebSocketResponse? = null
    var string: String = ""
    var byteString: ByteString = ByteString.EMPTY
    private var error: String = ""

    var isOnOpen: Boolean = false
    var isOnReconnect: Boolean = false
        private set


    private constructor()

    internal constructor(webSocket: WebSocket?, onOpen: Boolean) {
        this.webSocket = webSocket
        this.isOnOpen = onOpen
    }

    internal constructor(webSocket: WebSocket, byteString: ByteString) {
        this.webSocket = webSocket
        this.byteString = byteString
    }

    internal constructor(webSocket: WebSocket, data: String) {
        this.webSocket = webSocket
        this.string = data
        this.response = GsonBuilder().create().fromJson(data, WebSocketResponse::class.java)
    }


    companion object {

        @JvmOverloads
        internal fun createReconnect(error: String = ""): WebSocketInfo {
            val socketInfo = WebSocketInfo()
            socketInfo.isOnReconnect = true
            socketInfo.error = error
            return socketInfo
        }
    }
}
