package com.tokopedia.notifcenter.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.notifcenter.view.fragment.NotifCenterFragment

/**
 * @author by alvinatin on 21/08/18.
 */
class NotifCenterActivity : BaseSimpleActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, NotifCenterActivity::class.java)
    }

    object DeeplinkIntent {
        @DeepLink(ApplinkConst.BUYER_INFO)
        @JvmStatic
        fun createIntent(context: Context, extras: Bundle) = Companion.createIntent(context)

        @DeepLink(ApplinkConst.BUYER_INFO_WITH_ID)
        @JvmStatic
        fun createIntentDetail(context: Context, extras: Bundle): Intent {
            return Companion.createIntent(context)
        }
    }

    override fun getNewFragment() = NotifCenterFragment.createInstance()
}