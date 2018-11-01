
package com.tokopedia.flashsale.management.product.view.presenter

import com.tokopedia.flashsale.management.product.domain.usecase.GetFlashSaleProductDetailUseCase
import javax.inject.Inject

class FlashSaleProductDetailPresenter @Inject constructor(val getFlashSaleProductDetailUseCase: GetFlashSaleProductDetailUseCase) {

    fun getFlashSaleDetail(onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        getFlashSaleProductDetailUseCase.execute(onSuccess){
            //TODO use this = onError(it)
            onSuccess("aaa")
        }
    }

    fun detachView() {
        getFlashSaleProductDetailUseCase.unsubscribe()
    }
}