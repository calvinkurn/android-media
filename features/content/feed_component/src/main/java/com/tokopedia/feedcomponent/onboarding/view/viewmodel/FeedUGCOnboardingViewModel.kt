package com.tokopedia.feedcomponent.onboarding.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.feedcomponent.onboarding.view.strategy.base.FeedUGCOnboardingStrategy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
class FeedUGCOnboardingViewModel @AssistedInject constructor(
    @Assisted private val username: String,
    @Assisted private val onboardingStrategy: FeedUGCOnboardingStrategy,
) : ViewModel() {


    @AssistedFactory
    interface Factory {
        fun create(
            username: String,
            onboardingStrategy: FeedUGCOnboardingStrategy,
        ): FeedUGCOnboardingViewModel
    }

    init {
        username.isEmpty()
    }
}