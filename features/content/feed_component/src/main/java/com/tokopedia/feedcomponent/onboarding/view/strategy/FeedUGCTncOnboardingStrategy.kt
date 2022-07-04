package com.tokopedia.feedcomponent.onboarding.view.strategy

import com.tokopedia.feedcomponent.onboarding.domain.repository.FeedUGCOnboardingRepository
import com.tokopedia.feedcomponent.onboarding.view.strategy.base.FeedUGCOnboardingStrategy
import com.tokopedia.feedcomponent.onboarding.view.uimodel.action.FeedUGCOnboardingAction
import com.tokopedia.feedcomponent.onboarding.view.uimodel.event.FeedUGCOnboardingUiEvent
import com.tokopedia.feedcomponent.onboarding.view.uimodel.state.FeedUGCOnboardingUiState
import com.tokopedia.feedcomponent.onboarding.view.uimodel.state.UsernameState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.delay
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
    private val _isSubmit = MutableStateFlow(false)
    private val _hasAcceptTnc = MutableStateFlow(false)

    private val _uiEvent = MutableSharedFlow<FeedUGCOnboardingUiEvent>()

    override val uiState: Flow<FeedUGCOnboardingUiState> = combine(
        _username,
        _isCheckTnc,
        _isSubmit,
        _hasAcceptTnc,
    ) { username, isCheckTnc, isSubmit, hasAcceptTnc ->
        FeedUGCOnboardingUiState(
            username = username,
            usernameState = UsernameState.Valid,
            isCheckTnc = isCheckTnc,
            isSubmit = isSubmit,
            hasAcceptTnc = hasAcceptTnc,
        )
    }

    override val uiEvent: Flow<FeedUGCOnboardingUiEvent>
        get() = _uiEvent

    override fun submitAction(action: FeedUGCOnboardingAction) {
        when(action) {
            FeedUGCOnboardingAction.CheckTnc -> handleCheckTnc()
            FeedUGCOnboardingAction.ClickNext -> handleClickNext()
            else -> {}
        }
    }

    private fun handleCheckTnc() {
        _isCheckTnc.update { _isCheckTnc.value.not() }
    }

    private fun handleClickNext() {
        scope.launchCatchError(block = {
            if(!_isSubmit.value) {
                _isSubmit.update { true }

                /** TODO: just for testing purpose */
//                val result = repo.acceptTnc()
                delay(2000)
                val result = true
                _hasAcceptTnc.update { result }
                _isSubmit.update { false }
            }
        }) {
            _hasAcceptTnc.update { false }
            _isSubmit.update { false }
            _uiEvent.emit(
                FeedUGCOnboardingUiEvent.ErrorAcceptTnc
            )
        }
    }
}