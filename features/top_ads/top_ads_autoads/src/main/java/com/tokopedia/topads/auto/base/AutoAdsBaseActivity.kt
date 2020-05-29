package com.tokopedia.topads.auto.base

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.config.GlobalConfig
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.auto.di.DaggerAutoAdsComponent
import com.tokopedia.topads.auto.di.module.AutoAdsQueryModule

/**
 * Author errysuprayogi on 20,May,2019
 */
abstract class AutoAdsBaseActivity : BaseSimpleActivity(), HasComponent<AutoAdsComponent> {

    override fun getComponent(): AutoAdsComponent = DaggerAutoAdsComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).autoAdsQueryModule(AutoAdsQueryModule(this))
            .build()

    fun gotoCreateProductAd() {
        if (GlobalConfig.isSellerApp()) {
            startActivity(RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_GROUP_NEW_PROMO))
        } else {
            RouteManager.route(this, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_SELLER)
        }
    }

    fun onSummaryGroupClicked() {
        if (GlobalConfig.isSellerApp()) {
            startActivity(RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_GROUP_ADS_LIST))
        } else {
            RouteManager.route(this, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_SELLER)
        }
    }

    fun gotoCreateKeyword() {
        if (GlobalConfig.isSellerApp()) {
            startActivity(RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_KEYWORD_NEW_CHOOSE_GROUP, true.toString(), ""))
        } else {
            RouteManager.route(this, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_SELLER)
        }
    }
}
