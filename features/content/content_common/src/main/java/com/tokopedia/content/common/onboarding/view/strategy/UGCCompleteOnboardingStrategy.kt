package com.tokopedia.content.common.onboarding.view.strategy

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.onboarding.domain.repository.UGCOnboardingRepository
import com.tokopedia.content.common.onboarding.view.strategy.base.UGCOnboardingStrategy
import com.tokopedia.content.common.onboarding.view.uimodel.action.UGCOnboardingAction
import com.tokopedia.content.common.onboarding.view.uimodel.event.UGCOnboardingUiEvent
import com.tokopedia.content.common.onboarding.view.uimodel.state.UGCOnboardingUiState
import com.tokopedia.content.common.onboarding.view.uimodel.state.UsernameState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
class UGCCompleteOnboardingStrategy @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val repo: UGCOnboardingRepository,
) : UGCOnboardingStrategy(dispatcher) {

    private val _username = MutableStateFlow("")
    private val _usernameState = MutableStateFlow<UsernameState>(UsernameState.Unknown)
    private val _isCheckTnc = MutableStateFlow(false)
    private val _isSubmit = MutableStateFlow(false)
    private val _hasAcceptTnc = MutableStateFlow(false)

    private val _uiEvent = MutableSharedFlow<UGCOnboardingUiEvent>()

    override val uiState: Flow<UGCOnboardingUiState> = combine(
        _username,
        _usernameState,
        _isCheckTnc,
        _isSubmit,
        _hasAcceptTnc,
    ) { username, usernameState, isCheckTnc, isSubmit, hasAcceptTnc ->
        UGCOnboardingUiState(
            username = username,
            usernameState = usernameState,
            isCheckTnc = isCheckTnc,
            isSubmit = isSubmit,
            hasAcceptTnc = hasAcceptTnc,
        )
    }

    override val uiEvent: Flow<UGCOnboardingUiEvent>
        get() = _uiEvent

    override fun submitAction(action: UGCOnboardingAction) {
        super.submitAction(action)
        when(action) {
            is UGCOnboardingAction.InputUsername -> handleInputUsername(action.username)
            UGCOnboardingAction.CheckUsername -> handleCheckUsername()
            else -> {}
        }
    }

    init {
        scope.launch {
            _username
                .debounce(USERNAME_THROTTLE)
                .collect {
                submitAction(UGCOnboardingAction.CheckUsername)
            }
        }
    }

    private fun handleInputUsername(username: String) {
        _username.update { username }
    }

    private fun handleCheckUsername() {
        scope.launchCatchError(block = {
            if(_usernameState.value !is UsernameState.Loading) {
                _usernameState.update { checkUsername() }
            }
        }) {
            _usernameState.update { UsernameState.Invalid("") }
        }
    }

    override fun handleCheckTnc() {
        _isCheckTnc.update { _isCheckTnc.value.not() }
    }

    override fun handleClickNext() {
        scope.launchCatchError(block = {
            if(!_isSubmit.value && _isCheckTnc.value && _usernameState.value is UsernameState.Valid) {
                _isSubmit.update { true }

                val validateUsernameResult = checkUsername()
                _usernameState.update { validateUsernameResult }

                if(validateUsernameResult is UsernameState.Invalid) {
                    submitFail()
                    return@launchCatchError
                }

                val insertUsernameResult = repo.insertUsername(_username.value)
                if(!insertUsernameResult) {
                    _uiEvent.emit(UGCOnboardingUiEvent.ShowError)
                    submitFail()
                    return@launchCatchError
                }

                val acceptTncResult = repo.acceptTnc()
                if(!acceptTncResult) {
                    _uiEvent.emit(UGCOnboardingUiEvent.ShowError)
                }

                _isSubmit.update { false }
                _hasAcceptTnc.update { acceptTncResult }
            }
        }) {
            submitFail()
            _uiEvent.emit(UGCOnboardingUiEvent.ShowError)
        }
    }

    private suspend fun checkUsername(): UsernameState {
        _usernameState.update { UsernameState.Loading }

        if(_username.value.isEmpty())
            return UsernameState.Unknown

        val result = repo.validateUsername(_username.value)

        return if(result.first) UsernameState.Valid
        else UsernameState.Invalid(result.second)
    }

    private fun submitFail() {
        _isSubmit.update { false }
        _hasAcceptTnc.update { false }
    }

    companion object {
        private const val USERNAME_THROTTLE = 300L
    }
}
