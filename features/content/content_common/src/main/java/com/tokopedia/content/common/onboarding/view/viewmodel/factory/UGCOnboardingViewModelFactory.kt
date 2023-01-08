package com.tokopedia.content.common.onboarding.view.viewmodel.factory

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.tokopedia.content.common.onboarding.view.strategy.base.UGCOnboardingStrategy
import com.tokopedia.content.common.onboarding.view.viewmodel.UGCOnboardingViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
class UGCOnboardingViewModelFactory @AssistedInject constructor(
    @Assisted owner: SavedStateRegistryOwner,
    @Assisted private val onboardingType: Int,
    @Assisted private val onboardingStrategy: UGCOnboardingStrategy,
    private val UGCOnboardingViewModelFactory: UGCOnboardingViewModel.Factory,
) : AbstractSavedStateViewModelFactory(owner, null) {

    @AssistedFactory
    interface Creator {
        fun create(
            owner: SavedStateRegistryOwner,
            onboardingType: Int,
            onboardingStrategy: UGCOnboardingStrategy,
        ): UGCOnboardingViewModelFactory
    }

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return UGCOnboardingViewModelFactory.create(onboardingType, onboardingStrategy) as T
    }
}