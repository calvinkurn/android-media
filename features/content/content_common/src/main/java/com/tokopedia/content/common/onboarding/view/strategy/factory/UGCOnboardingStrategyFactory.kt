package com.tokopedia.content.common.onboarding.view.strategy.factory

import com.tokopedia.content.common.onboarding.di.qualifier.CompleteStrategy
import com.tokopedia.content.common.onboarding.di.qualifier.TncStrategy
import com.tokopedia.content.common.onboarding.view.fragment.UGCOnboardingParentFragment.Companion.VALUE_ONBOARDING_TYPE_COMPLETE
import com.tokopedia.content.common.onboarding.view.strategy.base.UGCOnboardingStrategy
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
class UGCOnboardingStrategyFactory @Inject constructor(
    @CompleteStrategy private val completeStrategy: UGCOnboardingStrategy,
    @TncStrategy private val tncStrategy: UGCOnboardingStrategy,
) {

    fun create(onboardingType: Int): UGCOnboardingStrategy {
        return if(onboardingType == VALUE_ONBOARDING_TYPE_COMPLETE) completeStrategy
        else tncStrategy
    }
}