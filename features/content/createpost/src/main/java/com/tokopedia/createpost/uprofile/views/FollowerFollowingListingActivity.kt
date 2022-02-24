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
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalPromo
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class FollowerFollowingListingActivity : BaseSimpleActivity() {

    private var bundle: Bundle? = null
    private val REQUEST_CODE_LOGIN = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        forDeeplink()
        super.onCreate(savedInstanceState)
        toolbar.hide()
        setResult(Activity.RESULT_OK, intent)
    }

    private fun forDeeplink() {
        bundle = intent.extras
        if (intent.data != null) {
            bundle = UriUtil.destructiveUriBundle(ApplinkConstInternalGlobal.USER_PROFILE_FOLLOWERS, intent.data, bundle)
        }
    }

    override fun getNewFragment(): Fragment? {
        return FollowerFollowingListingFragment.newInstance(bundle ?: Bundle())
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    companion object {

        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val intent = Intent(context, FollowerFollowingListingActivity::class.java)
            intent.putExtras(extras)
            return intent
        }

        fun getFollowerFollowingListing(context: Context, extras: Bundle): Intent {
            return getCallingIntent(context, extras)
        }
    }
}
