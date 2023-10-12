package com.tokopedia.sellerpersona.view.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.sellerpersona.common.Constants
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaDataUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.TogglePersonaUseCase
import com.tokopedia.sellerpersona.view.compose.model.args.PersonaArgsUiModel
import com.tokopedia.sellerpersona.view.compose.model.state.PersonaResultState
import com.tokopedia.sellerpersona.view.compose.model.uievent.ResultUiEffect
import com.tokopedia.sellerpersona.view.compose.model.uievent.ResultUiEvent
import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel
import com.tokopedia.sellerpersona.view.model.PersonaStatus
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 31/01/23.
 */

class ComposePersonaResultViewModel @Inject constructor(
    private val getPersonaDataUseCase: Lazy<GetPersonaDataUseCase>,
    private val togglePersonaUseCase: Lazy<TogglePersonaUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private val _personaState = MutableStateFlow(
        PersonaResultState(state = PersonaResultState.State.Loading)
    )
    val personaState: StateFlow<PersonaResultState> = _personaState.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ResultUiEffect>()
    val uiEffect: SharedFlow<ResultUiEffect> = _uiEffect.asSharedFlow()

    fun onEvent(event: ResultUiEvent) {
        when (event) {
            is ResultUiEvent.Reload -> reloadPage()
            is ResultUiEvent.FetchPersonaData -> fetchPersonaData(event.arguments)
            is ResultUiEvent.ApplyChanges -> onApplyClicked(event)
            is ResultUiEvent.OnSwitchCheckChanged -> onCheckedChanged(event)
            is ResultUiEvent.RetakeQuiz -> setEffect(ResultUiEffect.NavigateToQuestionnaire)
            is ResultUiEvent.SelectPersona -> setEffect(ResultUiEffect.NavigateToSelectPersona(event.currentPersona))
            is ResultUiEvent.OnResultImpressedEvent -> setOnResultImpressedEvent()
        }
    }

    private fun fetchPersonaData(args: PersonaArgsUiModel) {
        viewModelScope.launch {
            runCatching {
                _personaState.update { getLoadingState() }
                val result = getPersonaDataUseCase.get().execute(
                    shopId = userSession.get().shopId, page = Constants.PERSONA_PAGE_PARAM
                )
                _personaState.update { getSuccessState(args, result) }
            }.onFailure {
                _personaState.update { getErrorState(args) }
            }
        }
    }

    private fun toggleUserPersona(status: PersonaStatus) {
        viewModelScope.launch {
            runCatching {
                emitApplyButtonLoadingState(isLoading = true)
                val toggleStatus = withContext(dispatchers.io) {
                    togglePersonaUseCase.get().execute(
                        userSession.get().shopId, status
                    )
                }
                updatePersonaLocalData(toggleStatus)
                setEffect(ResultUiEffect.OnPersonaStatusChanged(personaStatus = toggleStatus))
            }.onFailure {
                emitApplyButtonLoadingState(isLoading = false)
                setEffect(ResultUiEffect.OnPersonaStatusChanged(throwable = it))
            }
        }
    }

    private fun setOnResultImpressedEvent() {
        setEffect(ResultUiEffect.SendImpressionResultTracking)
        _personaState.update {
            val state = _personaState.value
            return@update state.copy(hasImpressed = true)
        }
    }

    private fun setEffect(effect: ResultUiEffect) {
        viewModelScope.launch {
            _uiEffect.emit(effect)
        }
    }

    private fun reloadPage() {
        val args = _personaState.value.data.args
        fetchPersonaData(args)
    }

    private fun onApplyClicked(event: ResultUiEvent.ApplyChanges) {
        val status = if (event.isActive) {
            PersonaStatus.ACTIVE
        } else {
            PersonaStatus.INACTIVE
        }
        toggleUserPersona(status)
        setEffect(
            ResultUiEffect.SendClickApplyTracking(
                persona = event.persona,
                isActive = event.isActive
            )
        )
    }

    private fun emitApplyButtonLoadingState(isLoading: Boolean) {
        _personaState.update {
            val lastState = _personaState.value
            val buttonLoadingState = lastState.copy(
                data = lastState.data.copy(isApplyLoading = isLoading)
            )
            buttonLoadingState
        }
    }

    private fun onCheckedChanged(event: ResultUiEvent.OnSwitchCheckChanged) {
        _personaState.update {
            val lastState = _personaState.value
            val checkedChangedState = lastState.copy(
                data = lastState.data.copy(isSwitchChecked = event.isChecked)
            )
            checkedChangedState
        }
        setEffect(ResultUiEffect.SendSwitchCheckedChangedTracking)
    }

    private fun getSuccessState(
        args: PersonaArgsUiModel, data: PersonaDataUiModel
    ): PersonaResultState {
        val lastState = _personaState.value
        val isSwitchCheckedByDefault = args.paramPersona.isNotBlank()
        return if (isSwitchCheckedByDefault) {
            lastState.copy(
                state = PersonaResultState.State.Success,
                data = data.copy(isSwitchChecked = true, args = args),
                hasImpressed = false
            )
        } else {
            lastState.copy(
                state = PersonaResultState.State.Success,
                data = data.copy(args = args),
                hasImpressed = false
            )
        }
    }

    private fun getLoadingState(): PersonaResultState {
        val lastState = _personaState.value
        return lastState.copy(
            state = PersonaResultState.State.Loading,
            data = lastState.data,
            hasImpressed = false
        )
    }

    private fun getErrorState(args: PersonaArgsUiModel): PersonaResultState {
        val lastState = _personaState.value
        return lastState.copy(
            state = PersonaResultState.State.Error,
            data = lastState.data.copy(args = args),
            hasImpressed = false
        )
    }

    private fun updatePersonaLocalData(status: PersonaStatus) {
        _personaState.update {
            val lastState = _personaState.value
            val personaStatusState = lastState.copy(
                data = lastState.data.copy(isApplyLoading = false, personaStatus = status),
                hasImpressed = false
            )
            personaStatusState
        }
    }
}