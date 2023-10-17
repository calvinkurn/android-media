package com.tokopedia.sellerpersona.view.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaListUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.SetPersonaUseCase
import com.tokopedia.sellerpersona.view.compose.model.args.PersonaArgsUiModel
import com.tokopedia.sellerpersona.view.compose.model.state.SelectTypeState
import com.tokopedia.sellerpersona.view.compose.model.uieffect.SelectTypeUiEffect
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
import kotlinx.coroutines.flow.update
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
    val state: StateFlow<SelectTypeState> = _state.asStateFlow()

    private val _uiEffect = MutableSharedFlow<SelectTypeUiEffect>()
    val uiEffect: SharedFlow<SelectTypeUiEffect> = _uiEffect.asSharedFlow()

    fun onEvent(event: SelectTypeUiEvent) {
        when (event) {
            is SelectTypeUiEvent.FetchPersonaList -> fetchPersonaList(args = event.arguments)
            is SelectTypeUiEvent.Reload -> reloadPage()
            is SelectTypeUiEvent.ClickPersonaCard -> updateSelectedState(event.personaName)
            is SelectTypeUiEvent.ClickSubmitButton -> submitSelectedType()
        }
    }

    private fun fetchPersonaList(args: PersonaArgsUiModel) {
        viewModelScope.launch {
            runCatching {
                emitLoadingState()
                val data = withContext(dispatchers.io) {
                    getPersonaListUseCase.get().execute()
                }
                emitSuccessState(data, args)
            }.onFailure {
                emitErrorState(it)
            }
        }
    }

    private fun setPersona(persona: String) {
        viewModelScope.launch {
            runCatching {
                emitSelectButtonLoading(isLoading = true)
                val result = withContext(dispatchers.io) {
                    val shopId = userSession.get().shopId
                    return@withContext setPersonaUseCase.get().execute(
                        shopId = shopId, persona = persona, answers = emptyList()
                    )
                }
                emitSelectButtonLoading(isLoading = false)
                emitOnPersonaChanged(persona = result)
            }.onFailure {
                emitSelectButtonLoading(isLoading = false)
                emitOnPersonaChanged(t = it)
            }
        }
    }

    private fun emitOnPersonaChanged(persona: String = "", t: Throwable? = null) {
        viewModelScope.launch {
            _uiEffect.emit(
                SelectTypeUiEffect.OnPersonaChanged(
                    persona = persona,
                    throwable = t
                )
            )
        }
    }

    private fun reloadPage() {
        val args = _state.value.data.args
        fetchPersonaList(args)
    }

    private fun updateSelectedState(persona: String) {
        viewModelScope.launch(dispatchers.default) {
            val currentState = _state.value
            val data = currentState.data

            val currentSelected = data.personaList.firstOrNull { it.isSelected }?.value.orEmpty()
            if (persona == currentSelected) return@launch

            var selectedIndex = data.ui.selectedIndex
            val personaList = data.personaList.mapIndexed { i, p ->
                val updatePersona = if (p.value == persona) {
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
            _state.emit(selectedState)
        }
    }

    private fun submitSelectedType() {
        val currentState = _state.value
        val data = currentState.data
        val defaultPersona = data.args.paramPersona

        val currentSelectedPersona = data.personaList.firstOrNull { it.isSelected }?.value.orEmpty()
        if (currentSelectedPersona == defaultPersona) {
            eventCloseThePage(defaultPersona)
        } else {
            setPersona(currentSelectedPersona)
        }
    }

    private fun emitSelectButtonLoading(isLoading: Boolean) {
        val currentState = _state.value
        val data = currentState.data
        val loadingUi = data.ui.copy(isSelectButtonLoading = isLoading)
        val selectButtonLoadingState = currentState.copy(
            data = data.copy(ui = loadingUi)
        )
        _state.update { selectButtonLoadingState }
    }

    private fun eventCloseThePage(persona: String) {
        viewModelScope.launch {
            _uiEffect.emit(SelectTypeUiEffect.CloseThePage(persona))
        }
    }

    private fun emitErrorState(t: Throwable) {
        val errorState = _state.value.copy(state = SelectTypeState.State.Error(t))
        _state.update { errorState }
    }

    private fun emitSuccessState(
        personaList: List<PersonaUiModel>,
        args: PersonaArgsUiModel
    ) {
        val currentState = _state.value

        val persona = args.paramPersona
        var selectedIndex = -1
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
        _state.update { successState }
    }

    private fun emitLoadingState() {
        if (_state.value.state is SelectTypeState.State.Loading) return
        val currentState = _state.value
        val loadingState = currentState.copy(state = SelectTypeState.State.Loading)
        _state.update { loadingState }
    }
}