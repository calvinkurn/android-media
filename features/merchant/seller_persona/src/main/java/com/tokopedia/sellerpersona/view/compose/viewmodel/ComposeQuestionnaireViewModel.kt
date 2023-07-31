package com.tokopedia.sellerpersona.view.compose.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.sellerpersona.data.remote.model.QuestionnaireAnswerParam
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaQuestionnaireUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.SetPersonaUseCase
import com.tokopedia.sellerpersona.view.compose.model.state.QuestionnaireState
import com.tokopedia.sellerpersona.view.compose.model.uievent.QuestionnaireUiEvent
import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by @ilhamsuaib on 30/01/23.
 */

class ComposeQuestionnaireViewModel @Inject constructor(
    private val getQuestionnaireUseCase: Lazy<GetPersonaQuestionnaireUseCase>,
    private val setPersonaUseCase: Lazy<SetPersonaUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    val questionnaire: LiveData<Result<List<QuestionnairePagerUiModel>>>
        get() = _questionnaire
    val setPersonaResult: LiveData<Result<String>>
        get() = _setPersonaResult

    private val _questionnaire = MutableLiveData<Result<List<QuestionnairePagerUiModel>>>()
    private val _setPersonaResult = MutableLiveData<Result<String>>()

    private val _state = MutableStateFlow(QuestionnaireState())
    val state: StateFlow<QuestionnaireState>
        get() = _state

    private val _uiEvent = MutableSharedFlow<QuestionnaireUiEvent>()
    val uiEvent: SharedFlow<QuestionnaireUiEvent>
        get() = _uiEvent.asSharedFlow()

    fun onEvent(event: QuestionnaireUiEvent) {

    }

    fun fetchQuestionnaire() {
        viewModelScope.launchCatchError(block = {
            emitLoadingState()
            val data = withContext(dispatchers.io) {
                getQuestionnaireUseCase.get().execute()
            }
            delay(2000)
            emitSuccessState(data)
        }, onError = {
            emitErrorState()
        })
    }

    private suspend fun emitErrorState() {
        val errorState = _state.value.copy(state = QuestionnaireState.State.Error)
        _state.emit(errorState)
    }

    private suspend fun emitLoadingState() {
        val loadingState = _state.value.copy(state = QuestionnaireState.State.Loading)
        _state.emit(loadingState)
    }

    private suspend fun emitSuccessState(data: List<QuestionnairePagerUiModel>) {
        val successState = _state.value.copy(
            state = QuestionnaireState.State.Success,
            data = state.value.data.copy(questionnaireList = data)
        )
        _state.emit(successState)
    }

    fun submitAnswer(answers: List<QuestionnaireAnswerParam>) {
        viewModelScope.launchCatchError(block = {
            val persona = withContext(dispatchers.io) {
                val shopId = userSession.get().shopId
                return@withContext setPersonaUseCase.get().execute(shopId, String.EMPTY, answers)
            }
            delay(2000)
            _uiEvent.emit(QuestionnaireUiEvent.OnSubmitted(persona))
        }, onError = {
            _setPersonaResult.postValue(Fail(it))
        })
    }
}