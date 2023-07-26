package com.tokopedia.sellerpersona.view.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerpersona.common.Constants
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaDataUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.TogglePersonaUseCase
import com.tokopedia.sellerpersona.view.compose.model.args.PersonaArgsUiModel
import com.tokopedia.sellerpersona.view.compose.model.state.PersonaResultState
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

    private val _personaState = MutableStateFlow(PersonaResultState(isLoading = true, error = null))
    val personaState: StateFlow<PersonaResultState>
        get() = _personaState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ResultUiEvent>()
    val uiEvent: SharedFlow<ResultUiEvent>
        get() = _uiEvent.asSharedFlow()

    fun onEvent(event: ResultUiEvent) = viewModelScope.launch {
        when (event) {
            is ResultUiEvent.ApplyChanges -> onApplyClicked(event)
            is ResultUiEvent.CheckChanged -> onCheckedChanged(event)
            is ResultUiEvent.Reload -> reloadPage()
            else -> _uiEvent.emit(event)
        }
    }

    fun fetchPersonaData(args: PersonaArgsUiModel) {
        viewModelScope.launchCatchError(block = {
            _personaState.emit(getLoadingState())
            val result = getPersonaDataUseCase.get().execute(
                shopId = userSession.get().shopId, page = Constants.PERSONA_PAGE_PARAM
            )
            _personaState.emit(getSuccessState(args, result))
        }, onError = {
            _personaState.emit(getErrorState(args, it))
        })
    }

    private fun reloadPage() {
        val args = _personaState.value.data.args
        fetchPersonaData(args)
    }

    private fun toggleUserPersona(status: PersonaStatus) {
        viewModelScope.launchCatchError(block = {
            emitApplyButtonLoadingState(isLoading = true)
            val toggleStatus = withContext(dispatchers.io) {
                togglePersonaUseCase.get().execute(
                    userSession.get().shopId, status
                )
            }
            updatePersonaLocalData(toggleStatus)
            onEvent(
                ResultUiEvent.OnPersonaStatusChanged(personaStatus = toggleStatus)
            )
        }, onError = {
            emitApplyButtonLoadingState(isLoading = false)
            onEvent(ResultUiEvent.OnPersonaStatusChanged(throwable = it))
        })
    }

    private fun onApplyClicked(event: ResultUiEvent.ApplyChanges) {
        val status = if (event.isActive) {
            PersonaStatus.ACTIVE
        } else {
            PersonaStatus.INACTIVE
        }
        toggleUserPersona(status)
    }

    private suspend fun emitApplyButtonLoadingState(isLoading: Boolean) {
        val lastState = _personaState.value
        val buttonLoadingState =
            lastState.copy(data = lastState.data.copy(isApplyLoading = isLoading))
        _personaState.emit(buttonLoadingState)
    }

    private suspend fun onCheckedChanged(event: ResultUiEvent.CheckChanged) {
        val lastState = _personaState.value
        val checkedChangedState = lastState.copy(
            isLoading = false,
            data = lastState.data.copy(isSwitchChecked = event.isChecked),
            error = null
        )
        _personaState.emit(checkedChangedState)
    }

    private fun getSuccessState(
        args: PersonaArgsUiModel, data: PersonaDataUiModel
    ): PersonaResultState {
        val lastState = _personaState.value
        val isSwitchCheckedByDefault = args.paramPersona.isNotBlank()
        return if (isSwitchCheckedByDefault) {
            lastState.copy(
                isLoading = false,
                data = data.copy(isSwitchChecked = true, args = args),
                error = null
            )
        } else {
            lastState.copy(isLoading = false, data = data.copy(args = args), error = null)
        }
    }

    private fun getLoadingState(): PersonaResultState {
        val lastState = _personaState.value
        return lastState.copy(isLoading = true, data = lastState.data, error = null)
    }

    private fun getErrorState(args: PersonaArgsUiModel, throwable: Throwable): PersonaResultState {
        val lastState = _personaState.value
        return lastState.copy(
            isLoading = false, data = lastState.data.copy(args = args), error = throwable
        )
    }

    private suspend fun updatePersonaLocalData(status: PersonaStatus) {
        val lastState = _personaState.value
        val personaStatusState = lastState.copy(
            data = lastState.data.copy(
                isApplyLoading = false, personaStatus = status
            )
        )
        _personaState.emit(personaStatusState)
    }
}