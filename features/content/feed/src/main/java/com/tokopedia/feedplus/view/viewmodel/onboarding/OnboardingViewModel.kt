package com.tokopedia.feedplus.view.viewmodel.onboarding

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory

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
        var dataList : List<OnboardingDataViewModel> = ArrayList()
) : Visitable<FeedPlusTypeFactory> {
    override fun type(typeFactory: FeedPlusTypeFactory): Int {
        return typeFactory.type(this)
    }
}