package com.tokopedia.topads.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.topads.view.fragment.OboardingFragment

/**
 * Author errysuprayogi on 29,October,2019
 */
class OnboardingActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return OboardingFragment.newInstance()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL)
        startActivity(intent)
        finish()
    }
}
