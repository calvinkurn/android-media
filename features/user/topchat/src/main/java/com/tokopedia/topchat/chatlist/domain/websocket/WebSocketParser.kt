package com.tokopedia.topchat.chatlist.domain.websocket

import com.tokopedia.websocket.WebSocketResponse

interface WebSocketParser {
    fun parseResponse(json: String): WebSocketResponse
}