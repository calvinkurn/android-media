package com.tokopedia.inbox.fake.domain.chat.websocket

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.topchat.common.websocket.TopchatWebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

@ActivityScope
class FakeTopchatWebSocket @Inject constructor() : TopchatWebSocket {

    override fun reset() {
    }

    override fun connectWebSocket(listener: WebSocketListener) {
    }

    override fun close() {
    }

    override fun destroy() {
    }

    override fun sendPayload(wsPayload: String) {
    }
}
