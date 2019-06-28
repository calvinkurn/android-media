package com.tokopedia.mlp.merchantViewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.tokopedia.mlp.contractModel.LeWidgetData
import com.tokopedia.mlp.usecase.MerchantLendingUseCase
import rx.Subscriber
import javax.inject.Inject

class MerchantLendingViewModel @Inject constructor(var merchantUsecase: MerchantLendingUseCase) : ViewModel() {

    val data_live = MutableLiveData<LeWidgetData?>()

    fun executeUseCase() {
        merchantUsecase.execute(object : Subscriber<LeWidgetData?>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onNext(t: LeWidgetData?) {
                data_live.value = t

            }
        })

    }

    fun getCategoryList(): MutableLiveData<LeWidgetData?> {

        return data_live
    }
}

