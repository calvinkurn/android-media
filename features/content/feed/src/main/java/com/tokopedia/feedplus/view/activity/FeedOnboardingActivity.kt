package com.tokopedia.feedplus.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.feedplus.view.fragment.FeedOnboardingFragment

/**
 * @author by milhamj on 2019-09-20.
 */
class FeedOnboardingActivity : BaseSimpleActivity() {

    object DeeplinkIntents {
        @DeepLink(ApplinkConst.INTEREST_PICK)
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
        return FeedOnboardingFragment()
    }
}
