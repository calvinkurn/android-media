package com.tokopedia.inbox.fake.domain.chat.websocket

import com.tokopedia.topchat.chatlist.domain.websocket.TopchatWebSocket
import okhttp3.WebSocketListener

class FakeTopchatWebSocket: TopchatWebSocket {
    override fun connectWebSocket(listener: WebSocketListener) {

    }

    override fun close() {

    }
}