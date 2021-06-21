package com.tokopedia.topads.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.di.DaggerCreateAdsComponent
import com.tokopedia.topads.view.fragment.AutoBidSelectionFragment
import com.tokopedia.topads.view.fragment.BudgetingAdsFragment
import com.tokopedia.topads.view.fragment.ProductAdsListFragment
import com.tokopedia.topads.view.fragment.SummaryAdsFragment

/**
 * Author errysuprayogi on 29,October,2019
 */

private const val CLICK_BACK_BUTTON = "click-back button"
class StepperActivity : BaseStepperActivity(), HasComponent<CreateAdsComponent> {

    private var fragmentList: MutableList<Fragment>? = null

    override fun getListFragment(): MutableList<Fragment> {
        fragmentList = fragmentList ?: mutableListOf(
                ProductAdsListFragment.createInstance(),
                AutoBidSelectionFragment.createInstance(),
                BudgetingAdsFragment.createInstance(),
                SummaryAdsFragment.createInstance())
        return fragmentList!!
    }

    override fun getComponent(): CreateAdsComponent {
        return DaggerCreateAdsComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    override fun onBackEvent() {
        if (currentPosition <= fragmentList?.size ?: 0 && fragmentList?.get(currentPosition - 1) is BudgetingAdsFragment) {
            TopAdsCreateAnalytics.topAdsCreateAnalytics.sendTopAdsEvent(CLICK_BACK_BUTTON, "")
        }
        super.onBackEvent()
    }

}
