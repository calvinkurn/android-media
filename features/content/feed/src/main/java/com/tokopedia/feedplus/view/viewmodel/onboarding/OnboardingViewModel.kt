package com.tokopedia.feedplus.view.viewmodel.onboarding

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory

/**
 * @author by yoasfs on 2019-08-30
 */
data class OnboardingViewModel (
         val id: Int = 0
): Visitable<FeedPlusTypeFactory> {
    override fun type(typeFactory: FeedPlusTypeFactory): Int {
        return typeFactory.type(this)
    }
}