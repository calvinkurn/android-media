package com.tokopedia.tokochat.stub.common

import android.content.Context
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class UserSessionStub @Inject constructor(context: Context?) : UserSession(context) {

    override fun isLoggedIn(): Boolean {
        return true
    }
}
