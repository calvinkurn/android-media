package com.tokopedia.topads.view.activity

import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.topads.create.R
import kotlinx.android.synthetic.main.topads_create_activity_onboarding.*

/**
 * Author errysuprayogi on 29,October,2019
 */
class OnboardingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topads_create_activity_onboarding)

        btn_start_auto_ads.setOnClickListener {
            RouteManager.route(this, ApplinkConst.SellerApp.TOPADS_AUTOADS)
        }
        btn_start_manual_ads.setOnClickListener {
            startActivity(Intent(this, StepperActivity::class.java))
        }

    }

}
