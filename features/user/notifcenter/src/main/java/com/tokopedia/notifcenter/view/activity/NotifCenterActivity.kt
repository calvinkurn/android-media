package com.tokopedia.notifcenter.view.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.notifcenter.view.fragment.NotifCenterFragment

/**
 * @author by alvinatin on 21/08/18.
 */
class NotifCenterActivity : BaseSimpleActivity() {

    companion object {
        val NOTIF_ID = "notif_id"
        fun createIntent(context: Context) = Intent(context, NotifCenterActivity::class.java)
    }

    object DeeplinkIntent {
        @DeepLink(ApplinkConst.BUYER_INFO)
        @JvmStatic
        fun createIntent(context: Context, extras: Bundle) = Companion.createIntent(context)

        @DeepLink(ApplinkConst.BUYER_INFO_WITH_ID)
        @JvmStatic
        fun createIntentDetail(context: Context, extras: Bundle): Intent {
            val uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon()
            val notifId = extras.getString(NOTIF_ID, "")
            val intent = Intent(context, NotifCenterActivity::class.java)
            intent.putExtra(NOTIF_ID, notifId)
            return intent
        }
    }

    override fun getNewFragment(): Fragment{
        return NotifCenterFragment.createInstance(intent?.extras)
    }
}