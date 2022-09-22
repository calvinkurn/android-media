package com.tokopedia.macrobenchmark_util.env

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.coachmark.CoachMark2.Companion.isCoachmmarkShowAllowed
import com.tokopedia.macrobenchmark_util.env.session.MacrobenchmarkAuthHelper
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class SessionSetupActivity : AppCompatActivity(){
    companion object {
        private const val URI_HOME_LOGIN = "login"
        private const val URI_HOME_NON_LOGIN = "non-login"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent != null) {
            val uri = intent.data
            uri?.let {
                if (uri.pathSegments[1] == URI_HOME_LOGIN) {
                    handleHomeMacrobenchmarkUri(true, UserSession(this))
                } else {
                    handleHomeMacrobenchmarkUri(false, UserSession(this))
                }
            }
        }
    }

    private fun handleHomeMacrobenchmarkUri(isLogin: Boolean, userSession: UserSessionInterface) {
        isCoachmmarkShowAllowed = false
        userSession.setFirstTimeUserOnboarding(false)

        if (isLogin) {
            MacrobenchmarkAuthHelper.loginInstrumentationTestUser1(this)
        } else {
            MacrobenchmarkAuthHelper.clearUserSession(this)
        }
    }
}