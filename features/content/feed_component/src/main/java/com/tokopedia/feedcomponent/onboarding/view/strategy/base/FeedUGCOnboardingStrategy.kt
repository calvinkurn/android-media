package com.tokopedia.feedcomponent.onboarding.view.strategy.base

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.feedcomponent.onboarding.view.uimodel.action.FeedUGCOnboardingAction
import com.tokopedia.feedcomponent.onboarding.view.uimodel.event.FeedUGCOnboardingUiEvent
import com.tokopedia.feedcomponent.onboarding.view.uimodel.state.FeedUGCOnboardingUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
abstract class FeedUGCOnboardingStrategy {

    private val dispatchers = CoroutineDispatchersProvider
    protected var job: Job? = null
    protected val scope = CoroutineScope(dispatchers.computation)

    abstract val uiState: Flow<FeedUGCOnboardingUiState>
    abstract val uiEvent: Flow<FeedUGCOnboardingUiEvent>

    abstract fun submitAction(action: FeedUGCOnboardingAction)

    fun onCleared() {
        job?.cancel()
    }
}