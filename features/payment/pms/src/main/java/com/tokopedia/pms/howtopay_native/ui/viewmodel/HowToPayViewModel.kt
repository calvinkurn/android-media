package com.tokopedia.pms.howtopay_native.ui.viewmodel

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pms.howtopay_native.data.model.AppLinkPaymentInfo
import com.tokopedia.pms.howtopay_native.data.model.HowToPayGqlResponse
import com.tokopedia.pms.howtopay_native.data.model.HowToPayInstruction
import com.tokopedia.pms.howtopay_native.domain.AppLinkPaymentUseCase
import com.tokopedia.pms.howtopay_native.domain.GetGqlHowToPayInstructions
import com.tokopedia.pms.howtopay_native.domain.GetHowToPayInstructions
import com.tokopedia.pms.howtopay_native.domain.GetHowToPayInstructionsMapper
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

    val howToPayLiveData = MutableLiveData<Result<HowToPayInstruction>>()
    val appLinkPaymentLiveData = MutableLiveData<Result<AppLinkPaymentInfo>>()

    fun getHowToPayInstruction(appLinkPaymentInfo: AppLinkPaymentInfo) {
        /*val requestParams = getHowToPayInstructions.getRequestParam(appLinkPaymentInfo)
        getHowToPayInstructions.getHowToPayInstruction(
            requestParams,
            { result ->
                howToPayLiveData.postValue(Success(result))
            },
            {
                howToPayLiveData.postValue(Fail(it))
            }
        )*/
    }

    fun getGqlHtpInstructions() {
        getGqlHowToPayInstructions.getGqlHowToPayInstruction(
            "null" ,"",
            ::onHowToPayDataSuccess,
            ::onHowToPayDataFailure
        )
    }

    private fun onHowToPayDataSuccess(
        howToPayGqlResponse: HowToPayGqlResponse,
    ) {
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
        getGqlHowToPayInstructions.cancelJobs()
        getHowToPayInstructionsMapper.cancelJobs()
    }
}