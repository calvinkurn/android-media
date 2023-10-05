package com.tokopedia.sellerpersona.view.compose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerpersona.data.remote.model.QuestionnaireAnswerParam
import com.tokopedia.sellerpersona.data.remote.usecase.GetPersonaQuestionnaireUseCase
import com.tokopedia.sellerpersona.data.remote.usecase.SetPersonaUseCase
import com.tokopedia.sellerpersona.view.compose.model.state.QuestionnaireState
import com.tokopedia.sellerpersona.view.compose.model.uieffect.QuestionnaireUiEffect
import com.tokopedia.sellerpersona.view.compose.model.uievent.QuestionnaireUserEvent
import com.tokopedia.sellerpersona.view.model.BaseOptionUiModel
import com.tokopedia.sellerpersona.view.model.QuestionnaireDataUiModel
import com.tokopedia.sellerpersona.view.model.QuestionnairePagerUiModel
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
 * Created by @ilhamsuaib on 30/01/23.
 */

class ComposeQuestionnaireViewModel @Inject constructor(
    private val getQuestionnaireUseCase: Lazy<GetPersonaQuestionnaireUseCase>,
    private val setPersonaUseCase: Lazy<SetPersonaUseCase>,
    private val userSession: Lazy<UserSessionInterface>,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    private val _state = MutableStateFlow<QuestionnaireState>(QuestionnaireState.Loading)
    val state: StateFlow<QuestionnaireState>
        get() = _state.asStateFlow()

    private val _uiEffect = MutableSharedFlow<QuestionnaireUiEffect>()
    val uiEffect: SharedFlow<QuestionnaireUiEffect>
        get() = _uiEffect.asSharedFlow()

    private var data = QuestionnaireDataUiModel()

    fun onEvent(event: QuestionnaireUserEvent) {
        viewModelScope.launch {
            when (event) {
                is QuestionnaireUserEvent.OnOptionItemSelected -> setOnOptionItemSelected(event)
                is QuestionnaireUserEvent.FetchQuestionnaire -> fetchQuestionnaire()
                is QuestionnaireUserEvent.OnPagerSwipe -> setOnPagerSwipe(event.page)
                is QuestionnaireUserEvent.ClickNext -> moveToNextPage()
                is QuestionnaireUserEvent.ClickPrevious -> moveToPrevPage()
            }
        }
    }

    private fun moveToPrevPage() {
        val currentPage = data.currentPage
        val canMovePrev = currentPage > 0
        if (canMovePrev) {
            successSuccessState(data.copy(currentPage = currentPage.minus(1)))
        }
    }

    private fun moveToNextPage() {
        val currentPage = data.currentPage
        val maxPage = data.questionnaireList.size.minus(1)
        val canMoveNext = currentPage < maxPage
        if (canMoveNext) {
            successSuccessState(data.copy(currentPage = currentPage.plus(1)))
        } else {
            submitAnswer()
        }
    }

    private fun setOnPagerSwipe(page: Int) {
        successSuccessState(data.copy(currentPage = page))
    }

    private fun getSingleOptionQuestion(
        event: QuestionnaireUserEvent.OnOptionItemSelected,
        data: List<QuestionnairePagerUiModel>
    ): QuestionnairePagerUiModel {
        val selected = event.option
        val pagePosition = event.pagePosition
        val options = data[pagePosition].options.map {
            val option = it.copyData()
            val isTheSameOption = option.value == selected.value
            option.isSelected = isTheSameOption
            return@map option
        }
        return data[pagePosition].copy(options = options)
    }

    private fun getMultipleOptionQuestion(
        event: QuestionnaireUserEvent.OnOptionItemSelected,
        data: List<QuestionnairePagerUiModel>
    ): QuestionnairePagerUiModel {
        val selected = event.option
        val pagePosition = event.pagePosition
        val options = data[event.pagePosition].options.map {
            val option = it.copyData()
            if (option.value == selected.value) {
                option.isSelected = event.isChecked
            }
            return@map option
        }
        return data[pagePosition].copy(options = options)
    }

    private fun setOnOptionItemSelected(event: QuestionnaireUserEvent.OnOptionItemSelected) {
        val questionnaireList = data.questionnaireList.toMutableList()
        when (val option = event.option) {
            is BaseOptionUiModel.QuestionOptionSingleUiModel -> {
                if (option.isSelected) return
                questionnaireList[event.pagePosition] = getSingleOptionQuestion(
                    event = event, data = questionnaireList
                )
            }

            is BaseOptionUiModel.QuestionOptionMultipleUiModel -> {
                questionnaireList[event.pagePosition] = getMultipleOptionQuestion(
                    event = event, data = questionnaireList
                )
            }
        }

        successSuccessState(data.copy(questionnaireList = questionnaireList.toList()))
    }

    private fun successSuccessState(data: QuestionnaireDataUiModel) {
        this.data = data
        _state.update { QuestionnaireState.Success(data) }
    }

    private fun emitNonSuccessState(state: QuestionnaireState) {
        _state.update { state }
    }

    private suspend fun emitUiEffect(uiEffect: QuestionnaireUiEffect) {
        _uiEffect.emit(uiEffect)
    }

    private fun fetchQuestionnaire() {
        viewModelScope.launch {
            runCatching {
                emitNonSuccessState(QuestionnaireState.Loading)
                val data = withContext(dispatchers.io) {
                    getQuestionnaireUseCase.get().execute()
                }
                successSuccessState(QuestionnaireDataUiModel(questionnaireList = data))
            }.onFailure {
                emitNonSuccessState(QuestionnaireState.Error)
            }
        }
    }

    private fun submitAnswer() {
        viewModelScope.launch {
            runCatching {
                //show Next Button loading state
                successSuccessState(data.copy(isNextButtonLoading = true))

                val answers = data.questionnaireList.map { pager ->
                    QuestionnaireAnswerParam(id = pager.id.toLongOrZero(),
                        answers = pager.options.filter { it.isSelected }.map { it.value })
                }
                val persona = withContext(dispatchers.io) {
                    val shopId = userSession.get().shopId
                    return@withContext setPersonaUseCase.get()
                        .execute(shopId, String.EMPTY, answers)
                }

                //dismiss Next Button loading state
                successSuccessState(data.copy(isNextButtonLoading = false))

                //navigate to result page with new persona
                emitUiEffect(QuestionnaireUiEffect.NavigateToResultPage(persona))
            }.onFailure {
                //dismiss Next Button loading state
                successSuccessState(data.copy(isNextButtonLoading = false))
            }
        }
    }
}