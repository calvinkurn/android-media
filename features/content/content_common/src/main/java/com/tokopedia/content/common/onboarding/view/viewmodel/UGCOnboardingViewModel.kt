package com.tokopedia.content.common.onboarding.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.content.common.onboarding.view.strategy.base.UGCOnboardingStrategy
import com.tokopedia.content.common.onboarding.view.uimodel.action.UGCOnboardingAction
import com.tokopedia.content.common.onboarding.view.uimodel.event.UGCOnboardingUiEvent
import com.tokopedia.content.common.onboarding.view.uimodel.state.FeedUGCOnboardingUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
class UGCOnboardingViewModel @AssistedInject constructor(
    @Assisted private val onboardingType: Int,
    @Assisted private val onboardingStrategy: UGCOnboardingStrategy,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            onboardingType: Int,
            onboardingStrategy: UGCOnboardingStrategy,
        ): UGCOnboardingViewModel
    }

    val uiState: Flow<FeedUGCOnboardingUiState>
        get() = onboardingStrategy.uiState

    val uiEvent: Flow<UGCOnboardingUiEvent>
        get() = onboardingStrategy.uiEvent

    fun submitAction(action: UGCOnboardingAction) {
        onboardingStrategy.submitAction(action)
    }

    override fun onCleared() {
        super.onCleared()
        onboardingStrategy.onCleared()
    }
}