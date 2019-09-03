package com.tokopedia.power_merchant.subscribe.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gm.common.data.source.cloud.model.PMCancellationQuestionnaireAnswerModel
import com.tokopedia.gm.common.domain.interactor.DeactivatePowerMerchantUseCase
import com.tokopedia.power_merchant.subscribe.domain.interactor.GetPMCancellationQuestionnaireDataUseCase
import com.tokopedia.power_merchant.subscribe.model.PMCancellationQuestionnaireData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

class PMCancellationQuestionnaireViewModel @Inject constructor(
        private val getPMCancellationQuestionnaireDataUseCase: GetPMCancellationQuestionnaireDataUseCase,
        private val deactivationPowerMerchant: DeactivatePowerMerchantUseCase,
        @Named("Main") dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    val pmCancellationQuestionnaireData by lazy {
        MutableLiveData<Result<PMCancellationQuestionnaireData>>()
    }

    fun getPMCancellationQuestionnaireData(shopId: String) {
        getPMCancellationQuestionnaireDataUseCase.execute(
                GetPMCancellationQuestionnaireDataUseCase.createRequestParams(shopId),
                object : Subscriber<PMCancellationQuestionnaireData>() {
                    override fun onNext(data: PMCancellationQuestionnaireData) {
                        pmCancellationQuestionnaireData.value = Success(data)
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable) {
                        pmCancellationQuestionnaireData.value = Fail(e)
                    }

                }
        )
    }

    fun sendQuestionDataAndTurnOffAutoExtend(questionData: MutableList<PMCancellationQuestionnaireAnswerModel>) {
        deactivationPowerMerchant.execute(
                DeactivatePowerMerchantUseCase.createRequestParam(questionData),
                object : Subscriber<Boolean>() {
                    override fun onNext(t: Boolean?) {
                        val a = 20

                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        val a = 20
                    }
                }
        )
    }
}