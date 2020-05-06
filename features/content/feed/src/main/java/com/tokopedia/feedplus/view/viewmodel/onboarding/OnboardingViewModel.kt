package com.tokopedia.feedplus.view.viewmodel.onboarding

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory
import com.tokopedia.interest_pick_common.view.viewmodel.InterestPickDataViewModel
import com.tokopedia.interest_pick_common.view.viewmodel.InterestPickViewModel

/**
 * @author by yoasfs on 2019-09-18
 */
data class OnboardingViewModel(
        override var isEnableOnboarding: Boolean = false,
        override val minimumPick: Int = 0,
        override val source: String = "",
        override val titleIntro: String = "",
        override val titleFull: String = "",
        override val instruction: String = "",
        override val buttonCta: String = "",
        override var dataList : MutableList<InterestPickDataViewModel> = mutableListOf())
    : InterestPickViewModel, Visitable<FeedPlusTypeFactory> {
    override fun type(typeFactory: FeedPlusTypeFactory): Int {
        return typeFactory.type(this)
    }
}