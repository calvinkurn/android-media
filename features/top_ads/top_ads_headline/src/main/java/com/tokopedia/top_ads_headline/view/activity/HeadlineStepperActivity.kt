package com.tokopedia.top_ads_headline.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseStepperActivity
import com.tokopedia.top_ads_headline.view.fragment.AdDetailsFragment

class HeadlineStepperActivity : BaseStepperActivity() {
    private var fragmentList: MutableList<Fragment>? = null

    override fun getListFragment(): MutableList<Fragment> {
        fragmentList = fragmentList ?: mutableListOf(AdDetailsFragment.newInstance())
        return fragmentList!!
    }

}