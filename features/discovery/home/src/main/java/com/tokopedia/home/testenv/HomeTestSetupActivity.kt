package com.tokopedia.home.testenv

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.coachmark.CoachMark2.Companion.isCoachmmarkShowAllowed
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class HomeTestSetupActivity : AppCompatActivity(){
    companion object {
        private const val URI_HOME_MACROBENCHMARK = "macrobenchmark"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent != null) {
            val uri = intent.data
            uri?.let {
                if (uri.pathSegments[1] == URI_HOME_MACROBENCHMARK) {
                    handleHomeMacrobenchmarkUri(UserSession(this))
                }
            }
        }
    }

    private fun handleHomeMacrobenchmarkUri(userSession: UserSessionInterface) {
        isCoachmmarkShowAllowed = false
        userSession.setFirstTimeUserOnboarding(false)
    }
}