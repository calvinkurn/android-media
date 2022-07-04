package com.tokopedia.feedcomponent.onboarding.view.strategy

import com.tokopedia.feedcomponent.onboarding.domain.repository.FeedUGCOnboardingRepository
import com.tokopedia.feedcomponent.onboarding.view.strategy.base.FeedUGCOnboardingStrategy
import com.tokopedia.feedcomponent.onboarding.view.uimodel.action.FeedUGCOnboardingAction
import com.tokopedia.feedcomponent.onboarding.view.uimodel.event.FeedUGCOnboardingUiEvent
import com.tokopedia.feedcomponent.onboarding.view.uimodel.state.FeedUGCOnboardingUiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
class FeedUGCCompleteOnboardingStrategy @Inject constructor(
    private val repo: FeedUGCOnboardingRepository,
): FeedUGCOnboardingStrategy() {

    override val uiState: Flow<FeedUGCOnboardingUiState>
        get() = TODO("Not yet implemented")

    override val uiEvent: Flow<FeedUGCOnboardingUiEvent>
        get() = TODO("Not yet implemented")

    override fun submitAction(action: FeedUGCOnboardingAction) {
        TODO("Not yet implemented")
    }
}