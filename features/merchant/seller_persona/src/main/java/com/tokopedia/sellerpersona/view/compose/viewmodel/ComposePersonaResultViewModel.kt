package com.tokopedia.sellerpersona.view.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerpersona.common.Constants
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaDataUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.TogglePersonaUseCase
import com.tokopedia.sellerpersona.view.compose.model.PersonaResultState
import com.tokopedia.sellerpersona.view.compose.model.ResultUiEvent
import com.tokopedia.sellerpersona.view.compose.model.UiState
import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel
import com.tokopedia.sellerpersona.view.model.PersonaStatus
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
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

    private val _personaState = MutableStateFlow(PersonaResultState(isLoading = true))
    val personaState: StateFlow<PersonaResultState>
        get() = _personaState.asStateFlow()

    private val _togglePersonaStatus = MutableStateFlow<UiState<PersonaStatus>>(UiState.None)
    val togglePersonaStatus: StateFlow<UiState<PersonaStatus>>
        get() = _togglePersonaStatus.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ResultUiEvent>(replay = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    fun onEvent(event: ResultUiEvent) = viewModelScope.launch {
        when (event) {
            is ResultUiEvent.TogglePersona -> {

            }

            is ResultUiEvent.ApplyChanges -> {

            }

            is ResultUiEvent.CheckChanged -> onCheckedChanged(event.isChecked)

            is ResultUiEvent.Reload -> fetchPersonaData()
            else -> {
                _uiEvent.emit(event)
                _uiEvent.emit(ResultUiEvent.None)
            }
        }
    }

    private suspend fun onCheckedChanged(isChecked: Boolean) {
        val lastState = _personaState.value
        val checkedChangedState = lastState.copy(
            isLoading = false, data = lastState.data.copy(isSwitchChecked = isChecked), error = null
        )
        _personaState.emit(checkedChangedState)
    }

    fun fetchPersonaData() {
        viewModelScope.launchCatchError(block = {
            _personaState.emit(getLoadingState())
            val result = getPersonaDataUseCase.get().execute(
                shopId = userSession.get().shopId, page = Constants.PERSONA_PAGE_PARAM
            )
            _personaState.emit(getSuccessState(result))
        }, onError = {
            _personaState.emit(getErrorState(it))
        })
    }

    private fun getSuccessState(data: PersonaDataUiModel): PersonaResultState {
        val lastState = _personaState.value
        return lastState.copy(isLoading = false, data = data, error = null)
    }

    private fun getLoadingState(): PersonaResultState {
        val lastState = _personaState.value
        return lastState.copy(isLoading = true, data = lastState.data, error = null)
    }

    private fun getErrorState(throwable: Throwable): PersonaResultState {
        val lastState = _personaState.value
        return lastState.copy(isLoading = false, data = lastState.data, error = throwable)
    }

    private fun toggleUserPersona(status: PersonaStatus) {
        viewModelScope.launchCatchError(block = {
            val toggleStatus = withContext(dispatchers.io) {
                togglePersonaUseCase.get().execute(
                    userSession.get().shopId, status
                )
            }
            _togglePersonaStatus.emit(UiState.Success(toggleStatus))
        }, onError = {
            _togglePersonaStatus.emit(UiState.Error(it))
        })
    }
}