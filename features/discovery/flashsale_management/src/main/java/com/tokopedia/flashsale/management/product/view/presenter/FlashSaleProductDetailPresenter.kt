package com.tokopedia.flashsale.management.product.view.presenter

import com.tokopedia.flashsale.management.data.RequestError
import com.tokopedia.flashsale.management.data.ResponseError
import com.tokopedia.flashsale.management.data.Success
import com.tokopedia.flashsale.management.ekstension.thenOnUI
import com.tokopedia.flashsale.management.product.domain.usecase.GetFlashSaleProductDetailUseCase
import com.tokopedia.flashsale.management.util.AppExecutors
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class FlashSaleProductDetailPresenter @Inject constructor(val getFlashSaleProductDetailUseCase: GetFlashSaleProductDetailUseCase) {

    private val job = Job()

    fun getFlashSaleDetail(onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        GlobalScope.launch(AppExecutors.uiContext + job) {
            GlobalScope.async(AppExecutors.networkContext) {
                getFlashSaleProductDetailUseCase.getResponse()
            }.thenOnUI {
                //TODO just test, the object should be in form of list
                when (it) {
                    is ResponseError -> {
                        //TODO use this = onError(it.error)
                        onSuccess("aaa")
                    }
                    is RequestError -> {
                        //TODO use this = onError(it.error)
                        onSuccess("aaa")
                    }
                    is Success -> onSuccess("aaa")
                }
            }
        }
    }

    fun detachView() {
        job.cancel()
    }
}