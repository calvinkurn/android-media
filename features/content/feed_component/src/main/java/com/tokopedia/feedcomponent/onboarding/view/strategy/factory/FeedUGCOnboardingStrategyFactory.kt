package com.tokopedia.feedcomponent.onboarding.view.strategy.factory

import com.tokopedia.feedcomponent.onboarding.util.COMPLETE_STRATEGY
import com.tokopedia.feedcomponent.onboarding.util.TNC_STRATEGY
import com.tokopedia.feedcomponent.onboarding.view.strategy.base.FeedUGCOnboardingStrategy
import javax.inject.Inject
import javax.inject.Named

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
class FeedUGCOnboardingStrategyFactory @Inject constructor(
    @Named(COMPLETE_STRATEGY) private val completeStrategy: FeedUGCOnboardingStrategy,
    @Named(TNC_STRATEGY) private val tncStrategy: FeedUGCOnboardingStrategy,
) {

    fun create(username: String): FeedUGCOnboardingStrategy {
        return if(username.isEmpty()) completeStrategy
        else tncStrategy
    }
}