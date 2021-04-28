package com.tokopedia.topchat.chatlist.domain.websocket

import okhttp3.WebSocketListener

interface TopchatWebSocket {
    fun connectWebSocket(listener: WebSocketListener)
}