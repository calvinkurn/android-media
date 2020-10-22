package com.tokopedia.top_ads_headline.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.top_ads_headline.di.HeadlineAdsComponent

class HeadlineStepperActivity : BaseStepperActivity(), HasComponent<HeadlineAdsComponent> {
    private var fragmentList: MutableList<Fragment>? = null

    override fun getListFragment(): MutableList<Fragment> {
        return fragmentList!!
    }

    override fun getComponent(): HeadlineAdsComponent {
        TODO("Not yet implemented")
    }
}