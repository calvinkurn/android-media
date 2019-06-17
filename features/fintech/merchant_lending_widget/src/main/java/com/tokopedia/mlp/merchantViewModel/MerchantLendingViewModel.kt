package com.tokopedia.mlp.merchantViewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.tokopedia.mlp.contractModel.LeWidgetData
import com.tokopedia.mlp.usecase.MerchantLendingUseCase
import rx.Subscriber
import javax.inject.Inject

class MerchantLendingViewModel @Inject constructor(var merchantUsecase: MerchantLendingUseCase) : ViewModel() {

    val data_live = MutableLiveData<LeWidgetData>()


    fun bound() {
        merchantUsecase.execute(object : Subscriber<LeWidgetData>() {
            override fun onCompleted() {
                Log.d("MerchantLendingFragment", "oncomplete")
            }

            override fun onError(e: Throwable?) {
                Log.d("MerchantLendingFragment", "error" + e.toString())
            }

            override fun onNext(t: LeWidgetData?) {
                Log.d("MerchantLendingFragment", "onNext")
                data_live.value = t

            }
        })

    }


    fun getCategoryList(): MutableLiveData<LeWidgetData> {

        return data_live
    }


}

