package com.tokopedia.topads.view.activity

import androidx.fragment.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.topads.view.fragment.*

/**
 * Author errysuprayogi on 29,October,2019
 */
class CreateManualAdsStepperActivity : BaseStepperActivity() {

    private var fragmentList: MutableList<Fragment>? = null

    override fun getListFragment(): MutableList<Fragment> {
        fragmentList = fragmentList ?: mutableListOf(CreateManualAdsCreateGroupAdsFragment.createInstance(),
                CreateManualAdsProductAdsListFragment.createInstance(),
                CreateManualAdsKeywordAdsListFragment.createInstance(),
                CreateManualAdsBudgetingAdsFragment.createInstance(),
                CreateManualAdsSummaryAdsFragment.createInstance())
        return fragmentList!!
    }

}
