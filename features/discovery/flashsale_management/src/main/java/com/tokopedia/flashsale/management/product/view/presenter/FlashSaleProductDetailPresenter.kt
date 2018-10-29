
package com.tokopedia.flashsale.management.product.view.presenter

import com.google.gson.Gson
import com.tokopedia.flashsale.management.data.RequestError
import com.tokopedia.flashsale.management.data.ResponseError
import com.tokopedia.flashsale.management.data.Success
import com.tokopedia.flashsale.management.ekstension.thenOnUI
import com.tokopedia.flashsale.management.ekstension.toEligibleSellerProductViewModel
import com.tokopedia.flashsale.management.product.data.FlashSaleProduct
import com.tokopedia.flashsale.management.product.domain.usecase.GetFlashSaleProductDetailUseCase
import com.tokopedia.flashsale.management.product.domain.usecase.GetFlashSaleProductUseCase
import com.tokopedia.flashsale.management.product.domain.usecase.GetSellerStatusLabelUseCase
import com.tokopedia.flashsale.management.product.model.FlashSaleProductViewModel
import com.tokopedia.flashsale.management.util.AppExecutors
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import javax.inject.Inject

class FlashSaleProductDetailPresenter @Inject constructor(val getFlashSaleProductDetailUseCase: GetFlashSaleProductDetailUseCase) {

    private val job = Job()

    fun getFlashSaleDetail(onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        GlobalScope.async(AppExecutors.networkContext + job) {
            getFlashSaleProductDetailUseCase.getResponse()
        }.thenOnUI {
            try {
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
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }

    fun detachView() {
        job.cancel()
    }
}