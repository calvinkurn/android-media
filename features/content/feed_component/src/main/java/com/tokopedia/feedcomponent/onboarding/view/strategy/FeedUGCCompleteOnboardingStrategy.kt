package com.tokopedia.feedcomponent.onboarding.view.strategy

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.feedcomponent.R
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
        when(action) {
            is FeedUGCOnboardingAction.InputUsername -> handleInputUsername(action.username)
            FeedUGCOnboardingAction.CheckUsername -> handleCheckUsername()
            FeedUGCOnboardingAction.CheckTnc -> handleCheckTnc()
            FeedUGCOnboardingAction.ClickNext -> handleClickNext()
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
            _usernameState.update {
                UsernameState.Invalid(context.getString(abstractionR.string.default_request_error_unknown))
            }
            _uiEvent.emit(
                FeedUGCOnboardingUiEvent.ErrorCheckUsername
            )
        }
    }

    private fun handleCheckTnc() {
        _isCheckTnc.update { _isCheckTnc.value.not() }
    }

    private fun handleClickNext() {
        scope.launchCatchError(block = {
            if(!_isSubmit.value && _isCheckTnc.value && _usernameState.value is UsernameState.Valid) {
                _isSubmit.update { true }

                val validateUsernameResult = checkUsername()
                if(validateUsernameResult is UsernameState.Invalid) {
                    _usernameState.update { validateUsernameResult }
                    return@launchCatchError
                }

                val insertUsernameResult = repo.insertUsername(_username.value)
                if(!insertUsernameResult.first) {
                    _usernameState.update {
                        UsernameState.Invalid(insertUsernameResult.second)
                    }
                    _hasAcceptTnc.update { false }
                    _isSubmit.update { false }
                    return@launchCatchError
                }

                val acceptTncResult = repo.acceptTnc()

                _hasAcceptTnc.update { acceptTncResult }
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

    private suspend fun checkUsername(): UsernameState {
        _usernameState.update { UsernameState.Loading }

        if(_username.value.isEmpty())
            return UsernameState.Unknown

        if(_username.value.length < 3)
            return UsernameState.Invalid(context.getString(R.string.up_username_min_3_char))

        val result = repo.validateUsername(_username.value)

        return if(result.first) UsernameState.Valid
        else UsernameState.Invalid(result.second)
    }

    companion object {
        private const val USERNAME_THROTTLE = 300L
    }
}