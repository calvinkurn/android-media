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
    var string: String? = null
    var byteString: ByteString? = null
    var isOnOpen: Boolean? = false
    var isOnReconnect: Boolean = false
        private set
    private var error: String? = null

    private constructor() {}

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

        private var DEFAULT_NO_POLL = 0
        private var OPTION_TEXT = "Text"
        private var OPTION_IMAGE = "Image"
        private var FORMAT_DISCOUNT_LABEL = "%d%% OFF"

        @JvmOverloads
        internal fun createReconnect(error: String = ""): WebSocketInfo {
            var socketInfo = WebSocketInfo()
            socketInfo.isOnReconnect = true
            socketInfo.error = error
            return socketInfo
        }
    }
}
