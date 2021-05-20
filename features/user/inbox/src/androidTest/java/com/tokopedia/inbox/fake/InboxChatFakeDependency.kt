package com.tokopedia.inbox.fake

import com.tokopedia.inbox.fake.common.FakeUserSession
import com.tokopedia.inbox.fake.domain.chat.websocket.FakeTopchatWebSocket
import javax.inject.Inject

class InboxChatFakeDependency @Inject constructor(
        val userSession: FakeUserSession,
        val ws: FakeTopchatWebSocket
) {
}