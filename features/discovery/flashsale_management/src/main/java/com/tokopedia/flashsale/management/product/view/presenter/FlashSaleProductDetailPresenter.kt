
package com.tokopedia.flashsale.management.product.view.presenter

import javax.inject.Inject

class FlashSaleProductDetailPresenter @Inject constructor(/*val getFlashSaleProductDetailUseCase: GetFlashSaleProductDetailUseCase*/) {

//    fun getFlashSaleDetail(onSuccess: (FlashSaleProductHeader) -> Unit, onError: (Throwable) -> Unit) {
//        getFlashSaleProductDetailUseCase.execute(onSuccess){
//            //TODO use this = onError(it)
//            onSuccess(FlashSaleProductHeader())
//        }
//    }

    fun detachView() {
        /*getFlashSaleProductDetailUseCase.unsubscribe()*/
    }
}