package com.tokopedia.pms.howtopay_native.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pms.howtopay_native.data.model.AppLinkPaymentInfo
import com.tokopedia.pms.howtopay_native.data.model.HowToPayInstruction
import com.tokopedia.pms.howtopay_native.domain.GetHowToPayInstructions
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class HowToPayViewModel @Inject constructor(private val getHowToPayInstructions: GetHowToPayInstructions)
    : BaseViewModel(Dispatchers.Main){

    val mutableLiveData = MutableLiveData<Result<HowToPayInstruction>>()

    fun getHowToPayInstruction(appLinkPaymentInfo: AppLinkPaymentInfo){
        val requestParams = getHowToPayInstructions.getRequestParam(appLinkPaymentInfo)
        getHowToPayInstructions.getHowToPayInstruction(
                requestParams,
                { result ->
                    mutableLiveData.postValue(Success(result))
                },
                {
                    mutableLiveData.postValue(Fail(it))
                }
        )
    }


    override fun onCleared() {
        super.onCleared()
        getHowToPayInstructions.cancelJobs()
    }
}