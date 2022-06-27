package com.tokopedia.people.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide

class UserProfileActivity : BaseSimpleActivity() {

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
            bundle = UriUtil.destructiveUriBundle(
                ApplinkConstInternalGlobal.USER_PROFILE_LANDING,
                intent.data,
                bundle
            )
        }

        bundle?.putString(EXTRA_USERNAME, intent.data?.pathSegments?.get(0))
    }

    override fun getNewFragment(): Fragment? {
        return UserProfileFragment.newInstance(bundle ?: Bundle())
    }

    companion object {
        val EXTRA_USERNAME = "userName"
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
