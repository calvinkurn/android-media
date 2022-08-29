package com.tokopedia.content.common.onboarding.view.strategy.base

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.onboarding.view.uimodel.action.FeedUGCOnboardingAction
import com.tokopedia.content.common.onboarding.view.uimodel.event.FeedUGCOnboardingUiEvent
import com.tokopedia.content.common.onboarding.view.uimodel.state.FeedUGCOnboardingUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
abstract class FeedUGCOnboardingStrategy(
    dispatcher: CoroutineDispatchers,
) {

    private var job: Job = SupervisorJob()
    protected val scope = CoroutineScope(dispatcher.computation + job)

    abstract val uiState: Flow<FeedUGCOnboardingUiState>
    abstract val uiEvent: Flow<FeedUGCOnboardingUiEvent>

    open fun submitAction(action: FeedUGCOnboardingAction) {
        when(action) {
            FeedUGCOnboardingAction.CheckTnc -> handleCheckTnc()
            FeedUGCOnboardingAction.ClickNext -> handleClickNext()
            else -> {}
        }
    }

    abstract fun handleCheckTnc()
    abstract fun handleClickNext()

    fun onCleared() {
        job.cancel()
    }
}