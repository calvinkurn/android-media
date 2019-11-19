package com.tokopedia.changepassword.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.changepassword.view.fragment.ChangePasswordFragment

class ChangePasswordActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ChangePasswordFragment()
    }

    companion object {
        open fun createIntent(context: Context): Intent {
            return Intent(context, ChangePasswordActivity::class.java)
        }
    }

    object DeeplinkIntents {
        @JvmStatic
        @DeepLink(ApplinkConst.CHANGE_PASSWORD)
        fun defaultIntent(context: Context, extras: Bundle): Intent {
            return Intent(context, ChangePasswordActivity::class.java)
        }
    }
}
