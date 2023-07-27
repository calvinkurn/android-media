package com.tokopedia.sellerpersona.view.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaListUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.SetPersonaUseCase
import com.tokopedia.sellerpersona.view.compose.model.args.PersonaArgsUiModel
import com.tokopedia.sellerpersona.view.compose.model.state.SelectTypeState
import com.tokopedia.sellerpersona.view.compose.model.uievent.SelectTypeUiEvent
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
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
 * Created by @ilhamsuaib on 29/01/23.
 */

class ComposePersonaSelectTypeViewModel @Inject constructor(
    private val getPersonaListUseCase: Lazy<GetPersonaListUseCase>,
    private val setPersonaUseCase: Lazy<SetPersonaUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private val _state = MutableStateFlow(SelectTypeState(state = SelectTypeState.State.Loading))
    val state: StateFlow<SelectTypeState>
        get() = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<SelectTypeUiEvent>()
    val uiEvent: SharedFlow<SelectTypeUiEvent>
        get() = _uiEvent.asSharedFlow()

    fun fetchPersonaList(args: PersonaArgsUiModel) {
        viewModelScope.launch {
            try {
                emitLoadingState()
                val data = withContext(dispatchers.io) {
                    getPersonaListUseCase.get().execute()
                }
                emitSuccessState(data, args)
            } catch (e: Exception) {
                emitErrorState(e)
            }
        }
    }

    fun onEvent(event: SelectTypeUiEvent) = viewModelScope.launch {
        when (event) {
            is SelectTypeUiEvent.Reload -> reloadPage()
            is SelectTypeUiEvent.ClickPersonaCard -> updateSelectedState(event.persona)
            is SelectTypeUiEvent.ClickSelectButton -> submitSelectedType()
            else -> _uiEvent.emit(event)
        }
    }

    private fun setPersona(persona: String) {
        viewModelScope.launch {
            try {
                emitSelectButtonLoading(isLoading = true)
                val result = withContext(dispatchers.io) {
                    val shopId = userSession.get().shopId
                    setPersonaUseCase.get().execute(
                        shopId = shopId, persona = persona, answers = emptyList()
                    )
                }
                emitSelectButtonLoading(isLoading = false)
                emitOnPersonaChanged(persona = result)
            } catch (e: Exception) {
                emitSelectButtonLoading(isLoading = false)
                emitOnPersonaChanged(e = e)
            }
        }
    }

    private suspend fun emitOnPersonaChanged(persona: String = "", e: Exception? = null) {
        _uiEvent.emit(
            SelectTypeUiEvent.OnPersonaChanged(
                persona = persona,
                exception = e
            )
        )
    }

    private fun reloadPage() {
        val args = _state.value.data.args
        fetchPersonaList(args)
    }

    private fun updateSelectedState(persona: PersonaUiModel) {
        viewModelScope.launch(dispatchers.default) {
            val currentState = _state.value
            val data = currentState.data

            val currentSelected = data.personaList.firstOrNull { it.isSelected }
            if (persona.value == currentSelected?.value) return@launch

            var selectedIndex = data.ui.selectedIndex
            val personaList = data.personaList.mapIndexed { i, p ->
                val updatePersona = if (p.value == persona.value) {
                    selectedIndex = i
                    p.copy(isSelected = true)
                } else {
                    p.copy(isSelected = false)
                }
                return@mapIndexed updatePersona
            }
            val updatedData = data.copy(
                personaList = personaList, ui = data.ui.copy(selectedIndex = selectedIndex)
            )
            val selectedState = currentState.copy(data = updatedData, state = currentState.state)
            withContext(dispatchers.main) {
                _state.emit(selectedState)
            }
        }
    }

    private fun submitSelectedType() = viewModelScope.launch {
        val currentState = _state.value
        val data = currentState.data
        val defaultPersona = data.args.paramPersona

        val currentSelectedPersona = data.personaList.firstOrNull { it.isSelected }?.value.orEmpty()
        if (currentSelectedPersona == defaultPersona) {
            eventCloseThePage(defaultPersona)
            return@launch
        }

        setPersona(currentSelectedPersona)
    }

    private suspend fun emitSelectButtonLoading(isLoading: Boolean) {
        val currentState = _state.value
        val data = currentState.data
        val loadingUi = data.ui.copy(isSelectButtonLoading = isLoading)
        val selectButtonLoadingState = currentState.copy(
            data = data.copy(ui = loadingUi)
        )
        _state.emit(selectButtonLoadingState)
    }

    private suspend fun eventCloseThePage(persona: String) {
        _uiEvent.emit(SelectTypeUiEvent.CloseThePage(persona))
    }

    private suspend fun emitErrorState(e: Exception) {
        val errorState = _state.value.copy(state = SelectTypeState.State.Error(e))
        _state.emit(errorState)
    }

    private suspend fun emitSuccessState(
        personaList: List<PersonaUiModel>,
        args: PersonaArgsUiModel
    ) {
        val currentState = _state.value

        val persona = args.paramPersona
        var selectedIndex = 0
        val mPersonaList = if (persona.isBlank()) {
            personaList
        } else {
            personaList.mapIndexed { i, p ->
                return@mapIndexed if (p.value == persona) {
                    selectedIndex = i
                    p.copy(isSelected = true)
                } else {
                    p.copy(isSelected = false)
                }
            }
        }
        val data = currentState.data.copy(
            personaList = mPersonaList,
            args = args,
            ui = currentState.data.ui.copy(selectedIndex = selectedIndex)
        )
        val successState = currentState.copy(
            state = SelectTypeState.State.Success,
            data = data
        )
        _state.emit(successState)
    }

    private suspend fun emitLoadingState() {
        if (_state.value.state is SelectTypeState.State.Loading) return
        val currentState = _state.value
        val loadingState = currentState.copy(state = SelectTypeState.State.Loading)
        _state.emit(loadingState)
    }
}