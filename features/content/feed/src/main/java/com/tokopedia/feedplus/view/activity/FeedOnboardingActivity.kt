package com.tokopedia.feedplus.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.feedplus.view.fragment.FeedOnboardingFragment

/**
 * @author by milhamj on 2019-09-20.
 */
class FeedOnboardingActivity : BaseSimpleActivity() {

    object DeeplinkIntents {
        @JvmStatic
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            return Intent(context, FeedOnboardingActivity::class.java)
                    .setData(uri.build())
                    .putExtras(extras)
        }
    }

    companion object {
        fun getCallingIntent(context: Context, extras: Bundle): Intent {
            return Intent(context, FeedOnboardingActivity::class.java).putExtras(extras)
        }
    }

    override fun getNewFragment(): Fragment? {
        intent?.extras?.let {
            return FeedOnboardingFragment.getInstance(it)
        }
        return FeedOnboardingFragment()
    }

    override fun onBackPressed() {
        var intentResult = Intent()
        val fragment = supportFragmentManager.findFragmentByTag(tagFragment)
        fragment?.let {
            intentResult = (it as FeedOnboardingActivityListener).onBackPressedOnActivity()
        }
        setResult(Activity.RESULT_CANCELED, intentResult)
        finish()
    }

    interface FeedOnboardingActivityListener {
        fun onBackPressedOnActivity(): Intent
    }
}
