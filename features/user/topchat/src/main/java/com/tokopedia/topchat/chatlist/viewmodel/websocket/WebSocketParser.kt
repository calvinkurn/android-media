package com.tokopedia.topchat.chatlist.viewmodel.websocket

import com.tokopedia.websocket.WebSocketResponse

interface WebSocketParser {
    fun parseResponse(json: String): WebSocketResponse
}