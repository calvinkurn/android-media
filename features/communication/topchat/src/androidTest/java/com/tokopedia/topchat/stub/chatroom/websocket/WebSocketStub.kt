package com.tokopedia.topchat.stub.chatroom.websocket

import okhttp3.Request
import okhttp3.WebSocket
import okio.ByteString

class WebSocketStub : WebSocket {
    override fun queueSize(): Long {
        return 1
    }

    override fun send(text: String): Boolean {
        return true
    }

    override fun send(bytes: ByteString): Boolean {
        return true
    }

    override fun close(code: Int, reason: String?): Boolean {
        return true
    }

    override fun cancel() {

    }

    override fun request(): Request {
        return Request.Builder().url("/").build()
    }
}