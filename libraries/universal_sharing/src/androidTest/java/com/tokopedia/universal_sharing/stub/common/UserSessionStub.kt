package com.tokopedia.universal_sharing.stub.common

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class UserSessionStub @Inject constructor(
    @ApplicationContext context: Context
) : UserSession(context) {

    private var isUserLoggedIn: Boolean = true

    override fun isLoggedIn(): Boolean {
        return isUserLoggedIn
    }

    fun setUserLoginStatus(isUserLoggedIn: Boolean) {
        this.isUserLoggedIn = isUserLoggedIn
    }
}
