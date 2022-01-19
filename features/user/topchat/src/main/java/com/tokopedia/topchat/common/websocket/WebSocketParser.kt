package com.tokopedia.topchat.common.websocket

import com.tokopedia.websocket.WebSocketResponse

interface WebSocketParser {
    fun parseResponse(json: String): WebSocketResponse
}