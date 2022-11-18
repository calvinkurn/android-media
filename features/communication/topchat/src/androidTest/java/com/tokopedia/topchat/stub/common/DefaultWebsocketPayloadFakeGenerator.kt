package com.tokopedia.topchat.stub.common

import com.tokopedia.topchat.common.websocket.DefaultWebsocketPayloadGenerator
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class DefaultWebsocketPayloadFakeGenerator @Inject constructor(
    userSession: UserSessionInterface
): DefaultWebsocketPayloadGenerator(userSession) {

    var fakeLocalId: String = "0"

    override fun generateLocalId(): String {
        return fakeLocalId
    }
}