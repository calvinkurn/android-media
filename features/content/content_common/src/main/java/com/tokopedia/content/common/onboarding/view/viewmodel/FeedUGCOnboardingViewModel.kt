package com.tokopedia.content.common.onboarding.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.content.common.onboarding.view.strategy.base.FeedUGCOnboardingStrategy
import com.tokopedia.content.common.onboarding.view.uimodel.action.FeedUGCOnboardingAction
import com.tokopedia.content.common.onboarding.view.uimodel.event.FeedUGCOnboardingUiEvent
import com.tokopedia.content.common.onboarding.view.uimodel.state.FeedUGCOnboardingUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

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

    val uiState: Flow<FeedUGCOnboardingUiState>
        get() = onboardingStrategy.uiState

    val uiEvent: Flow<FeedUGCOnboardingUiEvent>
        get() = onboardingStrategy.uiEvent

    fun submitAction(action: FeedUGCOnboardingAction) {
        onboardingStrategy.submitAction(action)
    }

    override fun onCleared() {
        super.onCleared()
        onboardingStrategy.onCleared()
    }
}