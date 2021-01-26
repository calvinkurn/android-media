package com.tokopedia.top_ads_headline.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.top_ads_headline.R
import com.tokopedia.top_ads_headline.data.HeadlineAdStepperModel
import com.tokopedia.top_ads_headline.view.fragment.AdContentFragment
import com.tokopedia.top_ads_headline.view.fragment.AdDetailsFragment
import com.tokopedia.top_ads_headline.view.fragment.AdScheduleAndBudgetFragment
import com.tokopedia.top_ads_headline.view.fragment.TopAdsHeadlineKeyFragment

class HeadlineStepperActivity : BaseStepperActivity() {
    private var fragmentList: MutableList<Fragment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initiateStepperModel()
        super.onCreate(savedInstanceState)
    }

    private fun initiateStepperModel() {
        stepperModel = HeadlineAdStepperModel()
        (stepperModel as HeadlineAdStepperModel).slogan = getString(R.string.topads_headline_promotional_dummy_message)
    }

    override fun getListFragment(): MutableList<Fragment> {
        fragmentList = fragmentList ?: mutableListOf(AdDetailsFragment.newInstance(),
                AdContentFragment.newInstance(),
                TopAdsHeadlineKeyFragment.createInstance(),
                AdScheduleAndBudgetFragment.createInstance())
        return fragmentList as MutableList<Fragment>
    }
}