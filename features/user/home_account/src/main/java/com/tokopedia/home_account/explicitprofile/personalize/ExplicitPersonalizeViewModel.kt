package com.tokopedia.home_account.explicitprofile.personalize

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.home_account.explicitprofile.data.QuestionDataModel
import com.tokopedia.home_account.explicitprofile.data.SaveMultipleAnswersParam
import com.tokopedia.home_account.explicitprofile.domain.GetQuestionsUseCase
import com.tokopedia.home_account.explicitprofile.domain.GetRolloutUserVariantUseCase
import com.tokopedia.home_account.explicitprofile.domain.SaveMultipleAnswersUseCase
import com.tokopedia.home_account.explicitprofile.personalize.ui.OptionSelected
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject

class ExplicitPersonalizeViewModel @Inject constructor(
    val getQuestionsUseCase: GetQuestionsUseCase,
    val getRolloutUserVariantUseCase: GetRolloutUserVariantUseCase,
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
                    maxItemSelected = state.maxItemSelected,
                    minItemSelected = state.minItemSelected
                )
            }
            else -> {}
        }
    }

    fun getQuestion() {
        _stateGetQuestion.value = ExplicitPersonalizeResult.Loading

        launchCatchError (
            block = {
                val isActivated = getRolloutUserVariantUseCase(listOf(ABTEST_KEY_EXPLICIT_PERSONALIZE))

                if (isActivated.rolloutUserVariant.featureVariants.first().variant != ABTEST_VARIANT_EXPLICIT_PERSONALIZE) {
                    _stateGetQuestion.value = ExplicitPersonalizeResult.Failed
                    return@launchCatchError
                }

                val template = getQuestionsUseCase(GetQuestionsUseCase.QuestionParams(templateName = CATEGORY_PREFERENCE))
                    .explicitProfileQuestionDataModel.template
                templateId = template.id
                val sections = template.sections
                val maxItemSelected = getRuleMinMax(template.rules.maxAnswer)
                val minItemSelected = getRuleMinMax(template.rules.minAnswer)
                if (sections.isNotEmpty() && sections.first().layout == MULTIPLE_ANSWER) {
                    sectionId = sections.first().sectionId
                    initCounter(sections.first().questions)
                    _stateGetQuestion.value = ExplicitPersonalizeResult.Success(
                        listQuestion = sections.first().questions,
                        maxItemSelected = maxItemSelected,
                        minItemSelected = minItemSelected
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

    //value 0. meaning there no rule for min or max
    private fun getRuleMinMax(rule: Int?): Int {
        return rule ?: 0
    }

    private fun initCounter(list: List<QuestionDataModel>) {
        var counter = 0
        list.forEach {
            counter += it.answerValueList.size
        }
        _counterState.value = counter
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
                            val answers = getAnswersList(item.property.options)

                            questions.add(
                                SaveMultipleAnswersParam.InputParam.SectionsParam.QuestionsParam(
                                    questionId = item.questionId,
                                    answerValueList = answers
                                )
                            )
                        }
                    }
                    else -> {}
                }

                val parameter = SaveMultipleAnswersParam(
                    input = SaveMultipleAnswersParam.InputParam(
                        templateId = templateId,
                        templateName = CATEGORY_PREFERENCE,
                        sections = mutableListOf(
                            SaveMultipleAnswersParam.InputParam.SectionsParam(
                                sectionId = sectionId,
                                questions = questions
                            )
                        )
                    )
                )

                val response = saveMultipleAnswersUseCase(mutableListOf(parameter))
                if (response.response.isSuccess) {
                    _stateSaveAnswer.postValue(PersonalizeSaveAnswerResult.Success)
                } else {
                    val message = response.response.message
                    _stateSaveAnswer.postValue(PersonalizeSaveAnswerResult.Failed(Throwable(message)))
                }
            },
            onError = {
                _stateSaveAnswer.postValue(PersonalizeSaveAnswerResult.Failed(it))
            }
        )
    }

    private fun getAnswersList(list : List<QuestionDataModel.Property.Options>): MutableList<String> {
        val answers = mutableListOf<String>()
        list.forEach { option ->
            if (option.isSelected) {
                answers.add(option.value)
            }
        }
        return answers
    }

    companion object {
        private const val ABTEST_KEY_EXPLICIT_PERSONALIZE = "s_cat_affinity_andro"
        private const val ABTEST_VARIANT_EXPLICIT_PERSONALIZE = "treatment_variant"
        private const val CATEGORY_PREFERENCE = "category_preference"
        private const val MULTIPLE_ANSWER = "MultipleAnswer"
    }

}

sealed interface ExplicitPersonalizeResult {
    object Loading : ExplicitPersonalizeResult
    data class Success(
        val listQuestion: List<QuestionDataModel>,
        val maxItemSelected: Int,
        val minItemSelected: Int,
    ) : ExplicitPersonalizeResult
    object Failed : ExplicitPersonalizeResult
}

sealed interface PersonalizeSaveAnswerResult {
    object Loading : PersonalizeSaveAnswerResult
    object Success : PersonalizeSaveAnswerResult
    data class Failed(val throwable: Throwable) : PersonalizeSaveAnswerResult
}
