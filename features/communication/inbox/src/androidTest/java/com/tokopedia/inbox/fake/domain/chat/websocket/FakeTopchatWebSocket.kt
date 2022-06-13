package com.tokopedia.inbox.fake.domain.chat.websocket

import com.tokopedia.topchat.common.websocket.TopchatWebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class FakeTopchatWebSocket @Inject constructor() : TopchatWebSocket {
    override fun connectWebSocket(listener: WebSocketListener) {

    }

    override fun close() {

    }

    override fun destroy() {

    }

    override fun sendPayload(wsPayload: String) {

    }
}