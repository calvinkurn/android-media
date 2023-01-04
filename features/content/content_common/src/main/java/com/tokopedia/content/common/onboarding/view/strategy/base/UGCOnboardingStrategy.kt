package com.tokopedia.content.common.onboarding.view.strategy.base

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.onboarding.view.uimodel.action.UGCOnboardingAction
import com.tokopedia.content.common.onboarding.view.uimodel.event.UGCOnboardingUiEvent
import com.tokopedia.content.common.onboarding.view.uimodel.state.UGCOnboardingUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
abstract class UGCOnboardingStrategy(
    dispatcher: CoroutineDispatchers,
) {

    private var job: Job = SupervisorJob()
    protected val scope = CoroutineScope(dispatcher.computation + job)

    abstract val uiState: Flow<UGCOnboardingUiState>
    abstract val uiEvent: Flow<UGCOnboardingUiEvent>

    open fun submitAction(action: UGCOnboardingAction) {
        when(action) {
            UGCOnboardingAction.CheckTnc -> handleCheckTnc()
            UGCOnboardingAction.ClickNext -> handleClickNext()
            else -> {}
        }
    }

    abstract fun handleCheckTnc()
    abstract fun handleClickNext()

    fun onCleared() {
        job.cancel()
    }
}
