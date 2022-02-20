package com.tokopedia.createpost.uprofile.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface


class UserProfileActivity : BaseSimpleActivity() {

    private var bundle: Bundle? = null
    private val REQUEST_CODE_LOGIN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        forDeeplink()
        super.onCreate(savedInstanceState)
        setResult(Activity.RESULT_OK, intent)
    }

    private fun forDeeplink() {
        bundle = intent.extras
        if (intent.data != null) {
            bundle = UriUtil.destructiveUriBundle(ApplinkConstInternalPromo.TOKOPOINTS_COUPON_DETAIL, intent.data, bundle)
        }
    }

    override fun getNewFragment(): Fragment? {
        val userSession: UserSessionInterface = UserSession(this)
        return if (userSession.isLoggedIn) {
            UserProfileFragment.newInstance(bundle ?: Bundle())
        } else {
            startActivityForResult(RouteManager.getIntent(this, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN)
            null
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    companion object {

        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val intent = Intent(context, UserProfileActivity::class.java)
            intent.putExtras(extras)
            return intent
        }

        fun getUserProfile(context: Context, extras: Bundle): Intent {
            return getCallingIntent(context, extras)
        }
    }
}
