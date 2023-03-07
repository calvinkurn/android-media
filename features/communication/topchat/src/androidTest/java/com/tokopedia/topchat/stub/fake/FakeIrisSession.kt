package com.tokopedia.topchat.stub.fake

import com.tokopedia.iris.util.Session

class FakeIrisSession : Session {
    override fun getSessionId(): String {
        return "test iris session id"
    }
}
