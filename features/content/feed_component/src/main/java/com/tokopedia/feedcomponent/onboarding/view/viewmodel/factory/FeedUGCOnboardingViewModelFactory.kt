package com.tokopedia.feedcomponent.onboarding.view.viewmodel.factory

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.tokopedia.feedcomponent.onboarding.view.strategy.base.FeedUGCOnboardingStrategy
import com.tokopedia.feedcomponent.onboarding.view.viewmodel.FeedUGCOnboardingViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
class FeedUGCOnboardingViewModelFactory @AssistedInject constructor(
    @Assisted owner: SavedStateRegistryOwner,
    @Assisted private val username: String,
    @Assisted private val onboardingStrategy: FeedUGCOnboardingStrategy,
    private val feedUGCOnboardingViewModelFactory: FeedUGCOnboardingViewModel.Factory,
) : AbstractSavedStateViewModelFactory(owner, null) {

    @AssistedFactory
    interface Creator {
        fun create(
            owner: SavedStateRegistryOwner,
            username: String,
            onboardingStrategy: FeedUGCOnboardingStrategy,
        ): FeedUGCOnboardingViewModelFactory
    }

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return feedUGCOnboardingViewModelFactory.create(username, onboardingStrategy) as T
    }
}