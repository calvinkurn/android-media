package com.tokopedia.kyc_centralized.data

import android.content.Context
import com.tokopedia.user.session.UserSession

class UserSessionStub(context: Context) : UserSession(context) {

    var isLoggedInState: UserSessionState = UserSessionState.AUTHORIZED

    override fun isLoggedIn(): Boolean {
        return when (isLoggedInState) {
            UserSessionState.AUTHORIZED -> true
            UserSessionState.UNAUTHORIZED -> false
        }
    }

}
