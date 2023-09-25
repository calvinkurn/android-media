package com.tokopedia.common.stub

import android.content.Context
import com.tokopedia.user.session.UserSession

class UserSessionStub(private val isUserLoggedIn: Boolean, context: Context) : UserSession(context) {
    override fun isLoggedIn(): Boolean {
        return isUserLoggedIn
    }
}
