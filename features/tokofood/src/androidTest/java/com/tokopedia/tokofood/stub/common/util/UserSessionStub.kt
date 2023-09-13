package com.tokopedia.tokofood.stub.common.util

import android.content.Context
import com.tokopedia.user.session.UserSession

class UserSessionStub(context: Context) : UserSession(context) {

    var nameStub = "Rizqi Aryansa"
    var loggedIn = true

    override fun getName(): String {
        return nameStub
    }

    override fun isLoggedIn(): Boolean {
        return loggedIn
    }
}
