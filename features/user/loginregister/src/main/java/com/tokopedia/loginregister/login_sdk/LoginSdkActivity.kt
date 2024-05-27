package com.tokopedia.loginregister.login_sdk

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.loginregister.login_sdk.LoginSdkConstant.REDIRECT_URI
import com.tokopedia.sessioncommon.util.LoginSdkUtils
import com.tokopedia.sessioncommon.util.LoginSdkUtils.ERR_CLIENT_CANCELLED
import com.tokopedia.sessioncommon.util.LoginSdkUtils.ERR_CODE_CLIENT
import com.tokopedia.sessioncommon.util.LoginSdkUtils.getClientName
import com.tokopedia.sessioncommon.util.LoginSdkUtils.removeLoginSdkFlow
import com.tokopedia.user.session.UserSession

class LoginSdkActivity : LoginActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userSession = UserSession(this)

        if (userSession.accessToken.isNotEmpty() &&
            userSession.freshToken.isNotEmpty() &&
            userSession.userId != "0" &&
            userSession.userId.isNotEmpty()) {
            switchToConsentFragment()
        }
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        bundle.putAll(getBundleFromData())
        intent?.extras?.let {
            bundle.putAll(it)
        }
        return LoginSdkFragment.createInstance(bundle)
    }

    fun switchToConsentFragment() {
        supportFragmentManager.commit {
            val bundle = Bundle()
            bundle.putAll(getBundleFromData())
            intent?.extras?.let {
                bundle.putAll(it)
            }
            add(R.id.parent_view, LoginSdkConsentFragment.createInstance(bundle), "")
        }
    }

    override fun onBackPressed() {
        LoginSdkAnalytics.sendClickOnButtonBackEvent(getClientName())
        LoginSdkUtils.redirectToTargetUri(
            activity = this,
            redirectUrl = intent?.extras?.getString(REDIRECT_URI) ?: "",
            authCode = "",
            errorCode = ERR_CODE_CLIENT,
            error = ERR_CLIENT_CANCELLED
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        removeLoginSdkFlow()
    }
}
