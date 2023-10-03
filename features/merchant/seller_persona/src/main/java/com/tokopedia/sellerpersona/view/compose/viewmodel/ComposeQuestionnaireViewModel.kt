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
import com.tokopedia.sellerpersona.view.model.BaseOptionUiModel
import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel
import com.tokopedia.sellerpersona.view.model.QuestionnaireDataUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.delay
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
 * Created by @ilhamsuaib on 30/01/23.
 */

class ComposeQuestionnaireViewModel @Inject constructor(
    private val getQuestionnaireUseCase: Lazy<GetPersonaQuestionnaireUseCase>,
    private val setPersonaUseCase: Lazy<SetPersonaUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    val setPersonaResult: LiveData<Result<String>>
        get() = _setPersonaResult

    private val _setPersonaResult = MutableLiveData<Result<String>>()

    private val _state = MutableStateFlow<QuestionnaireState>(QuestionnaireState.Loading)
    val state: StateFlow<QuestionnaireState>
        get() = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<QuestionnaireUiEvent>()
    val uiEvent: SharedFlow<QuestionnaireUiEvent>
        get() = _uiEvent.asSharedFlow()

    private var questionnaireData = QuestionnaireDataUiModel()

    fun onEvent(event: QuestionnaireUiEvent) {
        viewModelScope.launch {
            when (event) {
                is QuestionnaireUiEvent.OnOptionItemSelected -> setOnOptionItemSelected(event)
                is QuestionnaireUiEvent.FetchQuestionnaire -> fetchQuestionnaire()
                else -> {

                }
            }
        }
    }

    private fun handleSingleOptionSelected(
        option: BaseOptionUiModel.QuestionOptionSingleUiModel,
        pagePosition: Int,
        data: List<QuestionnairePagerUiModel>
    ) {
        data[pagePosition].options.forEach {
            val isTheSameOption = it.value == option.value
            it.isSelected = isTheSameOption
        }
    }

    private fun handleMultipleOptionSelected(
        option: BaseOptionUiModel.QuestionOptionMultipleUiModel,
        pagePosition: Int,
        data: List<QuestionnairePagerUiModel>
    ) {
        data[pagePosition].options.forEach {
            if (it.value == option.value) {
                it.isSelected = !it.isSelected
            }
        }
    }

    private fun setOnOptionItemSelected(
        event: QuestionnaireUiEvent.OnOptionItemSelected
    ) {
        val questionnaireList = questionnaireData.questionnaireList
        when (val option = event.option) {
            is BaseOptionUiModel.QuestionOptionSingleUiModel -> {
                if (option.isSelected) return
                handleSingleOptionSelected(option, event.pagePosition, questionnaireList)
            }

            is BaseOptionUiModel.QuestionOptionMultipleUiModel -> {
                handleMultipleOptionSelected(option, event.pagePosition, questionnaireList)
            }
        }

        _state.value = QuestionnaireState.Success(QuestionnaireDataUiModel(questionnaireList))
    }

    private fun fetchQuestionnaire() {
        viewModelScope.launch {
            runCatching {
                _state.value = QuestionnaireState.Loading
                val data = withContext(dispatchers.io) {
                    getQuestionnaireUseCase.get().execute()
                }
                setQuestionnaire(data)
                _state.value = QuestionnaireState.Success(QuestionnaireDataUiModel(data))
            }.onFailure {
                _state.value = QuestionnaireState.Error
            }
        }
    }

    private fun setQuestionnaire(data: List<QuestionnairePagerUiModel>) {
        questionnaireData = questionnaireData.copy(questionnaireList = data)
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