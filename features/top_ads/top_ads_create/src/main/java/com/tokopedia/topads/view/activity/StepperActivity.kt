package com.tokopedia.topads.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication

import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.di.DaggerCreateAdsComponent
import com.tokopedia.topads.view.fragment.*

/**
 * Author errysuprayogi on 29,October,2019
 */
class StepperActivity : BaseStepperActivity(), HasComponent<CreateAdsComponent> {

    private var fragmentList: MutableList<Fragment>? = null

    override fun getListFragment(): MutableList<Fragment> {
        fragmentList = fragmentList ?: mutableListOf(CreateGroupAdsFragment.createInstance(),
                ProductAdsListFragment.createInstance(),
                KeywordAdsListFragment.createInstance(),
                BudgetingAdsFragment.createInstance(),
                SummaryAdsFragment.createInstance())
        return fragmentList!!
    }

    override fun getComponent(): CreateAdsComponent {
        return DaggerCreateAdsComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }
}
