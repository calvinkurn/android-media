package com.tokopedia.topads.auto.base

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.topads.auto.di.AutoAdsComponent
import com.tokopedia.topads.auto.di.DaggerAutoAdsComponent
import com.tokopedia.topads.auto.router.TopAdsAutoRouter

/**
 * Author errysuprayogi on 20,May,2019
 */
abstract class AutoAdsBaseActivity : BaseSimpleActivity(), HasComponent<AutoAdsComponent> {

    override fun getComponent(): AutoAdsComponent = DaggerAutoAdsComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent).build()

    fun gotoCreateProductAd() {
        val router = application as TopAdsAutoRouter
        if (GlobalConfig.isSellerApp()) {
            startActivity(router.getTopAdsGroupNewPromoIntent(this))
        } else {
            router.openTopAdsDashboardApplink(this)
        }
    }

    fun onSummaryGroupClicked() {
        val router = application as TopAdsAutoRouter
        if (GlobalConfig.isSellerApp()) {
            startActivity(router.getTopAdsGroupAdListIntent(this))
        } else {
            router.openTopAdsDashboardApplink(this)
        }
    }

    fun gotoCreateKeyword() {
        val router = application as TopAdsAutoRouter
        if (GlobalConfig.isSellerApp()) {
            startActivity(router.getTopAdsKeywordNewChooseGroupIntent(this, true, null))
        } else {
            router.openTopAdsDashboardApplink(this)
        }
    }
}
