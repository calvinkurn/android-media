package com.tokopedia.topchat.chatlist.viewmodel.websocket

import okhttp3.WebSocketListener

interface TopchatWebSocket {
    fun connectWebSocket(listener: WebSocketListener)
}