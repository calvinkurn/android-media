package com.tokopedia.power_merchant.subscribe.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.data.source.cloud.model.PMCancellationQuestionnaireAnswerModel
import com.tokopedia.gm.common.domain.interactor.DeactivatePMUseCase
import com.tokopedia.gm.common.domain.interactor.DeactivatePowerMerchantUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.power_merchant.subscribe.data.model.GoldCancellationsQuestionaire
import com.tokopedia.power_merchant.subscribe.data.model.Question
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMCancellationQuestionnaireDataUseCase
import com.tokopedia.power_merchant.subscribe.domain.model.PMCancellationQuestionnaireDataUseCaseModel
import com.tokopedia.power_merchant.subscribe.view.model.DeactivationQuestionnaireUiModel
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireOptionUiModel
import com.tokopedia.power_merchant.subscribe.view.model.QuestionnaireUiModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireQuestionModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import rx.Subscriber
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 06/03/21
 */

class DeactivationViewModel @Inject constructor(
        private val getPMCancellationQuestionnaireDataUseCase: GetPMCancellationQuestionnaireDataUseCase,
        private val deactivatePmUseCase: DeactivatePMUseCase,
        private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _pmDeactivationQuestionnaireData by lazy {
        MutableLiveData<Result<DeactivationQuestionnaireUiModel>>()
    }

    val pmCancellationQuestionnaireData: LiveData<Result<DeactivationQuestionnaireUiModel>> by lazy {
        _pmDeactivationQuestionnaireData
    }

    private val _isSuccessDeactivate by lazy {
        MutableLiveData<Result<Boolean>>()
    }

    val isSuccessDeactivate: LiveData<Result<Boolean>> by lazy {
        _isSuccessDeactivate
    }

    fun getPMCancellationQuestionnaireData(shopId: String, pmTireType: Int) {
        getPMCancellationQuestionnaireDataUseCase.execute(
                GetPMCancellationQuestionnaireDataUseCase.createRequestParams(shopId),
                object : Subscriber<PMCancellationQuestionnaireDataUseCaseModel>() {
                    override fun onNext(data: PMCancellationQuestionnaireDataUseCaseModel) {
                        val expiredDate = data.goldGetPmOsStatus.result.data.powerMerchant.expiredTime
                        val questionData = generateQuestionsData(data.goldCancellationQuestionnaire)
                        val deactivationQuestionnaire = DeactivationQuestionnaireUiModel(
                                expiredDate,
                                questionData
                        )
                        _pmDeactivationQuestionnaireData.value = Success(deactivationQuestionnaire)
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable) {
                        _pmDeactivationQuestionnaireData.value = Fail(e)
                    }
                }
        )
    }

    private fun generateQuestionsData(
            goldCancellationQuestionnaire: GoldCancellationsQuestionaire
    ): List<QuestionnaireUiModel> {
        val numOfQuestions = goldCancellationQuestionnaire.result.data.questionList.size
        return goldCancellationQuestionnaire.result
                .data.questionList.mapIndexed { index, question ->
                    return@mapIndexed when (question.questionType) {
                        PMCancellationQuestionnaireQuestionModel.TYPE_MULTIPLE_OPTION -> {
                            createMultipleOptionQuestion(question, index != numOfQuestions.minus(1))
                        }
                        else -> QuestionnaireUiModel.QuestionnaireRatingUiModel(question.question)
                    }
                }
    }

    private fun createMultipleOptionQuestion(
            questionData: Question,
            showItemDivider: Boolean
    ): QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel {
        return QuestionnaireUiModel.QuestionnaireMultipleOptionUiModel(
                question = questionData.question,
                options = questionData.option.map {
                    QuestionnaireOptionUiModel(it.value)
                },
                showItemDivider = showItemDivider
        )
    }

    fun submitPmDeactivation(questionData: MutableList<PMCancellationQuestionnaireAnswerModel>) {
        launchCatchError(block = {
            deactivatePmUseCase.params = DeactivatePMUseCase.createRequestParam(questionData)
            val result = Success(withContext(dispatchers.io) {
                deactivatePmUseCase.executeOnBackground()
            })
            _isSuccessDeactivate.value = result
        }, onError = {
            _isSuccessDeactivate.value = Fail(it)
        })
    }
}