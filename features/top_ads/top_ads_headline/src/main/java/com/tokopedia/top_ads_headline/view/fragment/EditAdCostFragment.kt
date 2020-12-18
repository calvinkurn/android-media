package com.tokopedia.top_ads_headline.view.fragment

import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.top_ads_headline.di.DaggerHeadlineAdsComponent

class EditAdCostFragment : BaseDaggerFragment() {
    override fun getScreenName(): String {
        return EditAdCostFragment::class.java.simpleName
    }

    override fun initInjector() {
        DaggerHeadlineAdsComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build().inject(this)
    }

    companion object {
        fun newInstance(): EditAdCostFragment = EditAdCostFragment()
    }
}