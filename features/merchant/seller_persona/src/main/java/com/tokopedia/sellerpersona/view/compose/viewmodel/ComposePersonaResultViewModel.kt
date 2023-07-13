package com.tokopedia.sellerpersona.view.compose.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerpersona.common.Constants
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaDataUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.TogglePersonaUseCase
import com.tokopedia.sellerpersona.view.compose.model.ResultUiEvent
import com.tokopedia.sellerpersona.view.compose.model.UiState
import com.tokopedia.sellerpersona.view.model.PersonaDataUiModel
import com.tokopedia.sellerpersona.view.model.PersonaStatus
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 31/01/23.
 */

class ComposePersonaResultViewModel @Inject constructor(
    private val getPersonaDataUseCase: Lazy<GetPersonaDataUseCase>,
    private val togglePersonaUseCase: Lazy<TogglePersonaUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.io) {

    private val _personaData = MutableStateFlow<UiState<PersonaDataUiModel>>(UiState.Default)
    val personaData: StateFlow<UiState<PersonaDataUiModel>>
        get() = _personaData.asStateFlow()

    private val _togglePersonaStatus = MutableStateFlow<UiState<PersonaStatus>>(UiState.Default)
    val togglePersonaStatus: StateFlow<UiState<PersonaStatus>>
        get() = _togglePersonaStatus.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ResultUiEvent>(replay = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    fun onEvent(event: ResultUiEvent) = viewModelScope.launch {
        _uiEvent.emit(event)
    }

    fun fetchPersonaData() {
        launchCatchError(block = {
            val result = getPersonaDataUseCase.get().execute(
                userSession.get().shopId,
                Constants.PERSONA_PAGE_PARAM
            )
            _personaData.emit(UiState.Success(result))
        }, onError = {
            _personaData.emit(UiState.Error(it))
        })
    }

    fun toggleUserPersona(status: PersonaStatus) {
        launchCatchError(block = {
            val toggleStatus = togglePersonaUseCase.get().execute(
                userSession.get().shopId, status
            )
            _togglePersonaStatus.postValue(Success(toggleStatus))
        }, onError = {
            _togglePersonaStatus.postValue(Fail(it))
        })
    }
}