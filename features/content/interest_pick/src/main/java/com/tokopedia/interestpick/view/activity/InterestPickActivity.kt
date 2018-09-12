package com.tokopedia.interestpick.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.airbnb.deeplinkdispatch.DeepLink
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.interestpick.view.fragment.InterestPickFragment

/**
 * @author by milhamj on 03/09/18.
 */
class InterestPickActivity : BaseSimpleActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, InterestPickActivity::class.java)
    }

    object DeeplinkIntent {
        @DeepLink(ApplinkConst.INTEREST_PICK)
        @JvmStatic
        fun createIntent(context: Context, extras: Bundle) = Companion.createIntent(context)
    }

    override fun getNewFragment() = InterestPickFragment.createInstance()
}