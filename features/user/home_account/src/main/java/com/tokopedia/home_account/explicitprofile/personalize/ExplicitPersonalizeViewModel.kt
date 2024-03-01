package com.tokopedia.home_account.explicitprofile.personalize

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel
import com.tokopedia.home_account.explicitprofile.data.SaveMultipleAnswersParam
import com.tokopedia.home_account.explicitprofile.domain.GetQuestionsUseCase
import com.tokopedia.home_account.explicitprofile.domain.SaveMultipleAnswersUseCase
import com.tokopedia.home_account.explicitprofile.personalize.ui.OptionSelected
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject

class ExplicitPersonalizeViewModel @Inject constructor(
    val getQuestionsUseCase: GetQuestionsUseCase,
    val saveMultipleAnswersUseCase: SaveMultipleAnswersUseCase,
    val dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val _stateGetQuestion = MutableLiveData<ExplicitPersonalizeResult>()
    val stateGetQuestion : LiveData<ExplicitPersonalizeResult> get() = _stateGetQuestion

    private val _stateSaveAnswer = MutableLiveData<PersonalizeSaveAnswerResult>()
    val stateSaveAnswer : LiveData<PersonalizeSaveAnswerResult> get() = _stateSaveAnswer

    private val _counterState = MutableLiveData(0)
    val counterState : LiveData<Int> get() = _counterState

    private var sectionId = 0
    private var templateId = 0

    init {
        getQuestion()
    }

    fun itemSelected(item: OptionSelected) {
        if (_stateSaveAnswer.value is PersonalizeSaveAnswerResult.Loading) {
            return
        }

        val currentCounter = _counterState.value

        _counterState.value = if (item.isSelected) {
                (currentCounter?.minus(1))
            } else {
                (currentCounter?.plus(1))
            }

        when (val state = stateGetQuestion.value) {
            is ExplicitPersonalizeResult.Success -> {
                val currentList = state.listQuestion
                currentList[item.indexCategory].property.options[item.indexOption].isSelected = !item.isSelected

                _stateGetQuestion.value = ExplicitPersonalizeResult.Success(
                    listQuestion = currentList,
                    maxItemSelected = state.maxItemSelected
                )
            }
            else -> {}
        }
    }

    fun getQuestion() {
        _stateGetQuestion.value = ExplicitPersonalizeResult.Loading

        launchCatchError (
            block = {
                val template = getQuestionsUseCase(GetQuestionsUseCase.QuestionParams(templateName = CATEGORY_PREFERENCE))
                    .explicitProfileQuestionDataModel.template
                templateId = template.id
                val sections = template.sections
                if (sections.isNotEmpty() && sections.first().layout == MULTIPLE_ANSWER) {
                    sectionId = sections.first().sectionId
                    sectionId = sections.first().sectionId
                    _stateGetQuestion.value = ExplicitPersonalizeResult.Success(
                        listQuestion = sections.first().questions,
                        maxItemSelected = 10//sections.first().maxAnswer
                    )
                } else {
                    _stateGetQuestion.value = ExplicitPersonalizeResult.Failed
                }
            },
            onError = {
                _stateGetQuestion.value = ExplicitPersonalizeResult.Failed
            }
        )
    }

    fun saveAnswers() {
        _stateSaveAnswer.value = PersonalizeSaveAnswerResult.Loading
        launchCatchError (
            context = dispatcher.io,
            block = {
                val questions = mutableListOf<SaveMultipleAnswersParam.InputParam.SectionsParam.QuestionsParam>()

                when (val state = stateGetQuestion.value) {
                    is ExplicitPersonalizeResult.Success -> {
                        val currentList = state.listQuestion
                        currentList.forEach { item ->
                            val answers = mutableListOf<String>()
                            item.property.options.forEach { option ->
                                if (option.isSelected) {
                                    answers.add(option.value)
                                }
                            }

                            if (answers.isNotEmpty()) {
                                questions.add(
                                    SaveMultipleAnswersParam.InputParam.SectionsParam.QuestionsParam(
                                        questionId = item.questionId,
                                        answerValue = answers.toString()
                                    )
                                )
                            }
                        }
                    }
                    else -> {}
                }

                val parameter = SaveMultipleAnswersParam(
                    input = SaveMultipleAnswersParam.InputParam(
                        templateName = CATEGORY_PREFERENCE,
                        sections = mutableListOf(
                            SaveMultipleAnswersParam.InputParam.SectionsParam(
                                sectionId = sectionId,
                                questions = questions
                            )
                        )
                    )
                )

                saveMultipleAnswersUseCase(mutableListOf(parameter))
                _stateSaveAnswer.postValue(PersonalizeSaveAnswerResult.Success)
            },
            onError = {
                _stateSaveAnswer.postValue(PersonalizeSaveAnswerResult.Failed(it))
            }
        )
    }

    companion object {
        private const val CATEGORY_PREFERENCE = "category_preference"
        private const val MULTIPLE_ANSWER = "MultipleAnswer"
    }

}

sealed interface ExplicitPersonalizeResult {
    object Loading : ExplicitPersonalizeResult
    data class Success(
        val listQuestion: List<QuestionDataModel>,
        val maxItemSelected: Int
    ) : ExplicitPersonalizeResult
    object Failed : ExplicitPersonalizeResult
}

sealed interface PersonalizeSaveAnswerResult {
    object Loading : PersonalizeSaveAnswerResult
    object Success : PersonalizeSaveAnswerResult
    data class Failed(val throwable: Throwable) : PersonalizeSaveAnswerResult
}
