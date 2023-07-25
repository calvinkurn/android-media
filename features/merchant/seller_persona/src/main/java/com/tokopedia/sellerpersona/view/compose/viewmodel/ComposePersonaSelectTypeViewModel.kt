package com.tokopedia.sellerpersona.view.compose.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaListUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.SetPersonaUseCase
import com.tokopedia.sellerpersona.view.compose.model.args.PersonaArgsUiModel
import com.tokopedia.sellerpersona.view.compose.model.state.SelectTypeState
import com.tokopedia.sellerpersona.view.compose.model.uievent.SelectTypeUiEvent
import com.tokopedia.sellerpersona.view.model.PersonaUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
) : BaseViewModel(dispatchers.main) {

    val state: StateFlow<SelectTypeState>
        get() = _state.asStateFlow()
    val setPersonaResult: LiveData<Result<String>>
        get() = _setPersonaResult

    private val _state = MutableStateFlow(SelectTypeState(state = SelectTypeState.State.Loading))
    private val _setPersonaResult = MutableLiveData<Result<String>>()

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

    private suspend fun emitErrorState(e: Exception) {
        val errorState = _state.value.copy(state = SelectTypeState.State.Error(e))
        _state.emit(errorState)
    }

    private suspend fun emitSuccessState(
        personaList: List<PersonaUiModel>,
        args: PersonaArgsUiModel
    ) {
        val currentState = _state.value
        val newData = currentState.data.copy(personaList = personaList, args = args)
        val successState = currentState.copy(state = SelectTypeState.State.Success, data = newData)
        _state.emit(successState)
    }

    private suspend fun emitLoadingState() {
        if (_state.value.state is SelectTypeState.State.Loading) return
        val currentState = _state.value
        val loadingState = currentState.copy(state = SelectTypeState.State.Loading)
        _state.emit(loadingState)
    }

    fun setPersona(persona: String) {
        launchCatchError(block = {
            val shopId = userSession.get().shopId
            val result = setPersonaUseCase.get().execute(
                shopId = shopId, persona = persona, answers = emptyList()
            )
            _setPersonaResult.postValue(Success(result))
        }, onError = {
            _setPersonaResult.postValue(Fail(it))
        })
    }

    fun onEvent(uiEvent: SelectTypeUiEvent) {

    }
}