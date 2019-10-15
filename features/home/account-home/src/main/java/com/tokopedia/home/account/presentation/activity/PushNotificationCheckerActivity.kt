package com.tokopedia.home.account.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.home.account.presentation.fragment.PushNotifCheckerFragment



class PushNotificationCheckerActivity: BaseSimpleActivity() {

    override fun getNewFragment(): Fragment = PushNotifCheckerFragment()

    companion object {
        open fun createIntent(context: Context): Intent {
            return Intent(context, PushNotificationCheckerActivity::class.java)
        }
    }

    object DeeplinkIntent {
        @JvmStatic
        @DeepLink(ApplinkConst.PUSHNOTIFCHECKER)
        fun defaultIntent(context: Context, extras: Bundle): Intent {
            return Intent(context, PushNotificationCheckerActivity::class.java)
        }
    }

}