package com.tokopedia.top_ads_headline.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.top_ads_headline.view.fragment.AdContentFragment
import com.tokopedia.top_ads_headline.view.fragment.AdDetailsFragment
import com.tokopedia.top_ads_headline.view.fragment.AdScheduleAndBudgetFragment
import com.tokopedia.top_ads_headline.view.fragment.TopAdsHeadlineKeyFragment

class HeadlineStepperActivity : BaseStepperActivity() {
    private var fragmentList: MutableList<Fragment>? = null

    override fun getListFragment(): MutableList<Fragment> {
        fragmentList = fragmentList ?: mutableListOf(AdDetailsFragment.newInstance(),
                AdContentFragment.newInstance(),
                TopAdsHeadlineKeyFragment.createInstance(),
                AdScheduleAndBudgetFragment.createInstance())
        return fragmentList as MutableList<Fragment>
    }
}