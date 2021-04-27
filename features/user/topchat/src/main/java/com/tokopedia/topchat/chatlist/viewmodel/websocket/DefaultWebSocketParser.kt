package com.tokopedia.topchat.chatlist.viewmodel.websocket

import com.google.gson.GsonBuilder
import com.tokopedia.websocket.WebSocketResponse
import javax.inject.Inject

class DefaultWebSocketParser @Inject constructor() : WebSocketParser {

    private val parser = GsonBuilder().create()

    override fun parseResponse(json: String): WebSocketResponse {
        return parser.fromJson(json, WebSocketResponse::class.java)
    }
}