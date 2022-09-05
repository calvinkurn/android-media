package com.tokopedia.usercomponents.explicit.stub.data

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class UserSessionStub @Inject constructor(@ApplicationContext context: Context) : UserSession(context) {

    private var isLoggedIn: UserSessionState = UserSessionState.UNAUTHORIZED

    fun setLoggedIn(state: UserSessionState) {
        isLoggedIn = state
    }

    override fun isLoggedIn(): Boolean {
        return when (isLoggedIn) {
            UserSessionState.AUTHORIZED -> true
            UserSessionState.UNAUTHORIZED -> false
        }
    }

}