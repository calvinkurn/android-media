package com.tokopedia.affiliate.feature.education.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.affiliate.feature.education.view.fragment.AffiliateEducationFragment

/**
 * @author by milhamj on 15/01/19.
 */
class AffiliateEducationActivity : BaseSimpleActivity() {

    companion object {
        fun createIntent(context: Context) = Intent(context, AffiliateEducationActivity::class.java)
    }

    object DeeplinkIntent {
        @JvmStatic
        fun createIntent(context: Context, extras: Bundle) = createIntent(context)
    }

    override fun getNewFragment() = AffiliateEducationFragment.createInstance()
}