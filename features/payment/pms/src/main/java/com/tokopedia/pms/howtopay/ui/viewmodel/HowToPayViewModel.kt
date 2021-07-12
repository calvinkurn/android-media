package com.tokopedia.pms.howtopay.ui.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pms.howtopay.data.model.AppLinkPaymentInfo
import com.tokopedia.pms.howtopay.data.model.HowToPayData
import com.tokopedia.pms.howtopay.data.model.HowToPayGqlResponse
import com.tokopedia.pms.howtopay.domain.AppLinkPaymentUseCase
import com.tokopedia.pms.howtopay.domain.GetGqlHowToPayInstructions
import com.tokopedia.pms.howtopay.domain.GetHowToPayInstructionsMapper
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class HowToPayViewModel @Inject constructor(
    private val appLinkPaymentUseCase: AppLinkPaymentUseCase,
    private val getGqlHowToPayInstructions: GetGqlHowToPayInstructions,
    private val getHowToPayInstructionsMapper: GetHowToPayInstructionsMapper
) : BaseViewModel(Dispatchers.Main) {

    val howToPayLiveData = MutableLiveData<Result<HowToPayData>>()
    val appLinkPaymentLiveData = MutableLiveData<Result<AppLinkPaymentInfo>>()

    fun getGqlHtpInstructions(appLinkPaymentInfo: AppLinkPaymentInfo) {
        getGqlHowToPayInstructions.getGqlHowToPayInstruction(
            ::onHowToPayDataSuccess,
            ::onHowToPayDataFailure,
            appLinkPaymentInfo
        )
    }

    private fun onHowToPayDataSuccess(howToPayGqlResponse: HowToPayGqlResponse) {
        getHowToPayInstructionsMapper.getHowToPayInstruction(
            howToPayGqlResponse,
            { result -> howToPayLiveData.postValue(Success(result)) },
            { onHowToPayDataFailure(it) }
        )
    }

    private fun onHowToPayDataFailure(throwable: Throwable) {
        howToPayLiveData.postValue(Fail(throwable))
    }

    fun getAppLinkPaymentInfoData(bundle: Bundle) {
        appLinkPaymentUseCase.getAppLinkPaymentInfo(bundle,
            {
                appLinkPaymentLiveData.postValue(Success(it))
            }, {
                appLinkPaymentLiveData.postValue(Fail(it))
            })
    }

    override fun onCleared() {
        super.onCleared()
        appLinkPaymentUseCase.cancelJobs()
        getGqlHowToPayInstructions.cancelJobs()
        getHowToPayInstructionsMapper.cancelJobs()
    }
}