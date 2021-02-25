package com.tokopedia.power_merchant.subscribe.view_old.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gm.common.data.source.cloud.model.PMCancellationQuestionnaireAnswerModel
import com.tokopedia.gm.common.domain.interactor.DeactivatePowerMerchantUseCase
import com.tokopedia.power_merchant.subscribe.data.model.GoldCancellationsQuestionaire
import com.tokopedia.power_merchant.subscribe.data.model.Question
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMCancellationQuestionnaireDataUseCase
import com.tokopedia.power_merchant.subscribe.domain.model.PMCancellationQuestionnaireDataUseCaseModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireData
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireMultipleOptionModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireQuestionModel
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireQuestionModel.Companion.TYPE_MULTIPLE_OPTION
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireQuestionModel.Companion.TYPE_RATE
import com.tokopedia.power_merchant.subscribe.view_old.model.PMCancellationQuestionnaireRateModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

class PMCancellationQuestionnaireViewModel @Inject constructor(
        private val getPMCancellationQuestionnaireDataUseCase: GetPMCancellationQuestionnaireDataUseCase,
        private val deactivatePowerMerchantUseCase: DeactivatePowerMerchantUseCase,
        dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _pmCancellationQuestionnaireData by lazy {
        MutableLiveData<Result<PMCancellationQuestionnaireData>>()
    }

    val pmCancellationQuestionnaireData: LiveData<Result<PMCancellationQuestionnaireData>> by lazy {
        _pmCancellationQuestionnaireData
    }

    private val _isSuccessUnsubscribe by lazy {
        MutableLiveData<Result<Boolean>>()
    }

    val isSuccessUnsubscribe: LiveData<Result<Boolean>> by lazy {
        _isSuccessUnsubscribe
    }

    fun getPMCancellationQuestionnaireData(shopId: String) {
        getPMCancellationQuestionnaireDataUseCase.execute(
                GetPMCancellationQuestionnaireDataUseCase.createRequestParams(shopId),
                object : Subscriber<PMCancellationQuestionnaireDataUseCaseModel>() {
                    override fun onNext(data: PMCancellationQuestionnaireDataUseCaseModel) {
                        val expiredDate = data.goldGetPmOsStatus.result.data.powerMerchant.expiredTime
                        val questionData = generateQuestionsData(data.goldCancellationQuestionnaire)
                        val cancellationQuestionnaireData = PMCancellationQuestionnaireData(
                                expiredDate,
                                questionData
                        )
                        _pmCancellationQuestionnaireData.value = Success(cancellationQuestionnaireData)
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable) {
                        _pmCancellationQuestionnaireData.value = Fail(e)
                    }

                }
        )
    }

    private fun generateQuestionsData(
            goldCancellationQuestionnaire: GoldCancellationsQuestionaire
    ): MutableList<PMCancellationQuestionnaireQuestionModel> {
        val questionsData = mutableListOf<PMCancellationQuestionnaireQuestionModel>()
        for (questionData in goldCancellationQuestionnaire.result.data.questionList) {
            when (questionData.questionType) {
                TYPE_RATE -> {
                    questionsData.add(createRateQuestion(questionData))
                }
                TYPE_MULTIPLE_OPTION -> {
                    questionsData.add(createMultipleOptionQuestion(questionData))
                }
            }
        }
        return questionsData
    }

    private fun createMultipleOptionQuestion(
            questionData: Question
    ): PMCancellationQuestionnaireMultipleOptionModel {
        val options = mutableListOf<PMCancellationQuestionnaireMultipleOptionModel.OptionModel>()
        for (option in questionData.option) {
            options.add(PMCancellationQuestionnaireMultipleOptionModel.OptionModel(
                    option.value
            ))
        }
        return PMCancellationQuestionnaireMultipleOptionModel(
                questionData.questionType,
                questionData.question,
                options
        )
    }

    private fun createRateQuestion(questionData: Question): PMCancellationQuestionnaireRateModel {
        return PMCancellationQuestionnaireRateModel(
                questionData.questionType,
                questionData.question
        )
    }

    fun sendQuestionAnswerDataAndTurnOffAutoExtend(
            questionData: MutableList<PMCancellationQuestionnaireAnswerModel>
    ) {
        deactivatePowerMerchantUseCase.execute(
                DeactivatePowerMerchantUseCase.createRequestParam(questionData),
                object : Subscriber<Boolean>() {
                    override fun onNext(successUnsubscribe: Boolean) {
                        _isSuccessUnsubscribe.value = Success(successUnsubscribe)
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable) {
                        _isSuccessUnsubscribe.value = Fail(e)
                    }
                }
        )
    }
}