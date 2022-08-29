package com.tokopedia.content.common.onboarding.view.strategy

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.onboarding.domain.repository.FeedUGCOnboardingRepository
import com.tokopedia.content.common.onboarding.view.strategy.base.FeedUGCOnboardingStrategy
import com.tokopedia.content.common.onboarding.view.uimodel.event.FeedUGCOnboardingUiEvent
import com.tokopedia.content.common.onboarding.view.uimodel.state.FeedUGCOnboardingUiState
import com.tokopedia.content.common.onboarding.view.uimodel.state.UsernameState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
class FeedUGCTncOnboardingStrategy @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val repo: FeedUGCOnboardingRepository,
): FeedUGCOnboardingStrategy(dispatcher) {

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

    override fun handleCheckTnc() {
        _isCheckTnc.update { _isCheckTnc.value.not() }
    }

    override fun handleClickNext() {
        scope.launchCatchError(block = {
            if(!_isSubmit.value && _isCheckTnc.value) {
                _isSubmit.update { true }

                val result = repo.acceptTnc()
                if(!result) {
                    _uiEvent.emit(FeedUGCOnboardingUiEvent.ShowError)
                }

                _isSubmit.update { false }
                _hasAcceptTnc.update { result }
            }
        }) {
            _isSubmit.update { false }
            _hasAcceptTnc.update { false }
            _uiEvent.emit(FeedUGCOnboardingUiEvent.ShowError)
        }
    }
}