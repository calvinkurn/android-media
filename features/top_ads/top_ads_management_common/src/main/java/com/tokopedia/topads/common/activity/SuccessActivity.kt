package com.tokopedia.topads.common.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.topads.common.fragment.OnSuccessFragment

class SuccessActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return OnSuccessFragment.newInstance()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL)
        startActivity(intent)
        finish()
    }

}
