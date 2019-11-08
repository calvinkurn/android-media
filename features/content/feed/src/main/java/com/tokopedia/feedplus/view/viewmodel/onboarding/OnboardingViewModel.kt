package com.tokopedia.feedplus.view.viewmodel.onboarding

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory
import com.tokopedia.interest_pick_common.view.viewmodel.InterestPickDataViewModel

/**
 * @author by yoasfs on 2019-09-18
 */
data class OnboardingViewModel (
        val isEnableOnboarding: Boolean = false,
        val minimumPick: Int = 0,
        val source: String = "",
        val titleIntro: String = "",
        val titleFull: String = "",
        val instruction: String = "",
        val buttonCta: String = "",
        var dataList : List<InterestPickDataViewModel> = ArrayList()
) : Visitable<FeedPlusTypeFactory> {
    override fun type(typeFactory: FeedPlusTypeFactory): Int {
        return typeFactory.type(this)
    }
}