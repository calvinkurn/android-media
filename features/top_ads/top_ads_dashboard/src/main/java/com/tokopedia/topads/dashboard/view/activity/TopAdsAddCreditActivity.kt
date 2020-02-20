package com.tokopedia.topads.dashboard.view.activity

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.di.DaggerTopAdsDashboardComponent
import com.tokopedia.topads.dashboard.di.TopAdsDashboardComponent
import com.tokopedia.topads.dashboard.view.fragment.TopAdsAddCreditFragment

/**
 * Created by Nathaniel on 11/22/2016.
 */

class TopAdsAddCreditActivity : BaseSimpleActivity(), HasComponent<TopAdsDashboardComponent> {

    override fun getNewFragment() = TopAdsAddCreditFragment.createInstance()

    override fun getScreenName(): String? = null

    override fun onBackPressed() {
        if (intent.extras?.getBoolean(TopAdsDashboardConstant.EXTRA_APPLINK_FROM_PUSH, false) == true) {
            val homeIntent = RouteManager.getIntent(this, ApplinkConst.HOME)
            startActivity(homeIntent)
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun getComponent(): TopAdsDashboardComponent = DaggerTopAdsDashboardComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent).build()
}