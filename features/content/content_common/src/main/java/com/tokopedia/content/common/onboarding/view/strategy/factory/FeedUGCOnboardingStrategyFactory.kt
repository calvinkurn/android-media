package com.tokopedia.content.common.onboarding.view.strategy.factory

import com.tokopedia.content.common.onboarding.di.qualifier.CompleteStrategy
import com.tokopedia.content.common.onboarding.di.qualifier.TncStrategy
import com.tokopedia.content.common.onboarding.view.strategy.base.FeedUGCOnboardingStrategy
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
class FeedUGCOnboardingStrategyFactory @Inject constructor(
    @CompleteStrategy private val completeStrategy: FeedUGCOnboardingStrategy,
    @TncStrategy private val tncStrategy: FeedUGCOnboardingStrategy,
) {

    fun create(username: String): FeedUGCOnboardingStrategy {
        return if(username.isEmpty()) completeStrategy
        else tncStrategy
    }
}