package com.tokopedia.feedcomponent.onboarding.view.strategy

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.onboarding.domain.repository.FeedUGCOnboardingRepository
import com.tokopedia.feedcomponent.onboarding.view.strategy.base.FeedUGCOnboardingStrategy
import com.tokopedia.feedcomponent.onboarding.view.uimodel.action.FeedUGCOnboardingAction
import com.tokopedia.feedcomponent.onboarding.view.uimodel.event.FeedUGCOnboardingUiEvent
import com.tokopedia.feedcomponent.onboarding.view.uimodel.state.FeedUGCOnboardingUiState
import com.tokopedia.feedcomponent.onboarding.view.uimodel.state.UsernameState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.tokopedia.abstraction.R as abstractionR

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
class FeedUGCCompleteOnboardingStrategy @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repo: FeedUGCOnboardingRepository,
): FeedUGCOnboardingStrategy() {

    private val _username = MutableStateFlow("")
    private val _usernameState = MutableStateFlow<UsernameState>(UsernameState.Unknown)
    private val _isCheckTnc = MutableStateFlow(false)
    private val _isSubmit = MutableStateFlow(false)
    private val _hasAcceptTnc = MutableStateFlow(false)

    private val _uiEvent = MutableSharedFlow<FeedUGCOnboardingUiEvent>()

    override val uiState: Flow<FeedUGCOnboardingUiState> = combine(
        _username,
        _usernameState,
        _isCheckTnc,
        _isSubmit,
        _hasAcceptTnc,
    ) { username, usernameState, isCheckTnc, isSubmit, hasAcceptTnc ->
        FeedUGCOnboardingUiState(
            username = username,
            usernameState = usernameState,
            isCheckTnc = isCheckTnc,
            isSubmit = isSubmit,
            hasAcceptTnc = hasAcceptTnc,
        )
    }

    override val uiEvent: Flow<FeedUGCOnboardingUiEvent>
        get() = _uiEvent

    override fun submitAction(action: FeedUGCOnboardingAction) {
        super.submitAction(action)
        when(action) {
            is FeedUGCOnboardingAction.InputUsername -> handleInputUsername(action.username)
            FeedUGCOnboardingAction.CheckUsername -> handleCheckUsername()
            else -> {}
        }
    }

    init {
        scope.launch {
            _username
                .debounce(USERNAME_THROTTLE)
                .collect {
                submitAction(FeedUGCOnboardingAction.CheckUsername)
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
            _usernameState.update { UsernameState.Invalid(getDefaultErrorMessage()) }
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
                    _uiEvent.emit(FeedUGCOnboardingUiEvent.ShowError)
                    submitFail()
                    return@launchCatchError
                }

                val acceptTncResult = repo.acceptTnc()
                if(acceptTncResult) {
                    _uiEvent.emit(FeedUGCOnboardingUiEvent.ShowError)
                }

                _hasAcceptTnc.update { acceptTncResult }
                _isSubmit.update { false }
            }
        }) {
            submitFail()
            _uiEvent.emit(FeedUGCOnboardingUiEvent.ShowError)
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

    private fun getDefaultErrorMessage(): String {
        return context.getString(abstractionR.string.default_request_error_unknown)
    }

    private fun submitFail() {
        _hasAcceptTnc.update { false }
        _isSubmit.update { false }
    }

    companion object {
        private const val USERNAME_THROTTLE = 300L
    }
}