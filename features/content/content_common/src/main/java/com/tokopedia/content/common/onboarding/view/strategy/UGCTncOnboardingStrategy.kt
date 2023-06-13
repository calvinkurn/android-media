package com.tokopedia.content.common.onboarding.view.strategy

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.onboarding.domain.repository.UGCOnboardingRepository
import com.tokopedia.content.common.onboarding.view.strategy.base.UGCOnboardingStrategy
import com.tokopedia.content.common.onboarding.view.uimodel.event.UGCOnboardingUiEvent
import com.tokopedia.content.common.onboarding.view.uimodel.state.UGCOnboardingUiState
import com.tokopedia.content.common.onboarding.view.uimodel.state.UsernameState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.flow.*
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
class UGCTncOnboardingStrategy @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val repo: UGCOnboardingRepository,
): UGCOnboardingStrategy(dispatcher) {

    private val _username = MutableStateFlow("")
    private val _isCheckTnc = MutableStateFlow(false)
    private val _isSubmit = MutableStateFlow(false)
    private val _hasAcceptTnc = MutableStateFlow(false)

    private val _uiEvent = MutableSharedFlow<UGCOnboardingUiEvent>()

    override val uiState: Flow<UGCOnboardingUiState> = combine(
        _username,
        _isCheckTnc,
        _isSubmit,
        _hasAcceptTnc,
    ) { username, isCheckTnc, isSubmit, hasAcceptTnc ->
        UGCOnboardingUiState(
            username = username,
            usernameState = UsernameState.Valid,
            isCheckTnc = isCheckTnc,
            isSubmit = isSubmit,
            hasAcceptTnc = hasAcceptTnc,
        )
    }

    override val uiEvent: Flow<UGCOnboardingUiEvent>
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
                    _uiEvent.emit(UGCOnboardingUiEvent.ShowError)
                }

                _isSubmit.update { false }
                _hasAcceptTnc.update { result }
            }
        }) {
            _isSubmit.update { false }
            _hasAcceptTnc.update { false }
            _uiEvent.emit(UGCOnboardingUiEvent.ShowError)
        }
    }
}
