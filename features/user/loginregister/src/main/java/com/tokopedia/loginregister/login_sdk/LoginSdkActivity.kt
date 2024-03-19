package com.tokopedia.loginregister.login_sdk

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.view.activity.LoginActivity
import com.tokopedia.user.session.UserSession

class LoginSdkActivity : LoginActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userSession = UserSession(this)

        toolbar?.title = "Masuk ke Tokopedia"
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
            replace(R.id.parent_view, LoginSdkConsentFragment.createInstance(bundle), "")
        }
    }

    override fun onBackPressed() {
        LoginSdkUtils.redirectToTargetUri(
            activity = this,
            redirectUrl = intent?.extras?.getString("redirect_uri") ?: "",
            authCode = "",
            error = "err:user:cancelled"
        )
    }
}
