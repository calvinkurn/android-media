package com.tokopedia.feedcomponent.onboarding.view.strategy

import com.tokopedia.feedcomponent.onboarding.domain.repository.FeedUGCOnboardingRepository
import com.tokopedia.feedcomponent.onboarding.view.strategy.base.FeedUGCOnboardingStrategy
import com.tokopedia.feedcomponent.onboarding.view.uimodel.action.FeedUGCOnboardingAction
import com.tokopedia.feedcomponent.onboarding.view.uimodel.event.FeedUGCOnboardingUiEvent
import com.tokopedia.feedcomponent.onboarding.view.uimodel.state.FeedUGCOnboardingUiState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
class FeedUGCTncOnboardingStrategy @Inject constructor(
    private val repo: FeedUGCOnboardingRepository,
): FeedUGCOnboardingStrategy() {

    private val _username = MutableStateFlow("")
    private val _isCheckTnc = MutableStateFlow(false)
    private val _hasAcceptTnc = MutableStateFlow(false)

    private val _uiEvent = MutableSharedFlow<FeedUGCOnboardingUiEvent>()

    override val uiState: Flow<FeedUGCOnboardingUiState> = combine(
        _username,
        _isCheckTnc,
        _hasAcceptTnc,
    ) { username, isCheckTnc, hasAcceptTnc ->
        FeedUGCOnboardingUiState(
            username = username,
            isCheckTnc = isCheckTnc,
            hasAcceptTnc = hasAcceptTnc,
        )
    }

    override val uiEvent: Flow<FeedUGCOnboardingUiEvent>
        get() = _uiEvent

    override fun submitAction(action: FeedUGCOnboardingAction) {
        when(action) {
            is FeedUGCOnboardingAction.CheckTnc -> {
                handleCheckTnc()
            }
            is FeedUGCOnboardingAction.ClickNext -> {
                handleClickNext()
            }
            else -> {}
        }
    }

    private fun handleCheckTnc() {
        _isCheckTnc.update { _isCheckTnc.value.not() }
    }

    private fun handleClickNext() {
        job?.cancel()
        job = scope.launchCatchError(block = {
            val result = repo.acceptTnc()
            _hasAcceptTnc.update { result }
        }) {
            _uiEvent.emit(
                FeedUGCOnboardingUiEvent.ErrorAcceptTnc
            )
        }
    }
}