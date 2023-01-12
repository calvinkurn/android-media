package com.tokopedia.topchat.common.websocket

import okhttp3.WebSocketListener

interface TopchatWebSocket {
    fun connectWebSocket(listener: WebSocketListener)
    fun close()
    fun destroy()
    fun sendPayload(wsPayload: String)
    fun reset()
}
