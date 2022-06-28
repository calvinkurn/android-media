package com.tokopedia.people.views.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.people.views.fragment.FollowerFollowingListingFragment

class FollowerFollowingListingActivity : BaseSimpleActivity() {

    private var bundle: Bundle? = null

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
