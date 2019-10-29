package com.tokopedia.mlp.merchantViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.mlp.contractModel.LeWidgetData
import com.tokopedia.mlp.modelToggle.Data
import com.tokopedia.mlp.usecase.MerchantLendingUseCase
import com.tokopedia.mlp.usecase.MerchantLendingUseCaseSPUpdate
import rx.Subscriber
import javax.inject.Inject

class MerchantLendingViewModel @Inject constructor(var merchantUseCase: MerchantLendingUseCase, var spUpdateUseCase: MerchantLendingUseCaseSPUpdate) : ViewModel() {

    val dataLive = MutableLiveData<LeWidgetData?>()
    val dataSpUpdate = MutableLiveData<Boolean?>()

    fun executeUseCase() {
        merchantUseCase.execute(object : Subscriber<LeWidgetData?>() {
            override fun onCompleted() {
            }
            override fun onError(exception: Throwable?) {
            }
            override fun onNext(leWidgetData: LeWidgetData?) {
                dataLive.value = leWidgetData
            }
        })
    }

    fun executeUseCaseToggle(setSP:Boolean) {
        spUpdateUseCase.execute(spUpdateUseCase.createRequestParams(setSP), object : Subscriber<Data?>() {
            override fun onCompleted() {
            }
            override fun onError(exception: Throwable?) {

            }
            override fun onNext(spUpdateData: Data?) {
                dataSpUpdate.value = spUpdateData?.spTogglesaldoprioritas?.success
            }
        })
    }

    fun getCategoryList(): MutableLiveData<LeWidgetData?> {
        return dataLive
    }

    fun getSPUpdateData(): MutableLiveData<Boolean?> {
        return dataSpUpdate
    }
}




