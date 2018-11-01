package com.tokopedia.flashsale.management.product.view.presenter

import com.google.gson.Gson
import com.tokopedia.flashsale.management.ekstension.toEligibleSellerProductViewModel
import com.tokopedia.flashsale.management.product.data.FlashSaleProduct
import com.tokopedia.flashsale.management.product.domain.usecase.GetFlashSaleProductUseCase
import com.tokopedia.flashsale.management.product.domain.usecase.GetSellerStatusLabelUseCase
import com.tokopedia.flashsale.management.product.model.FlashSaleProductViewModel
import javax.inject.Inject

class FlashSaleProductListPresenter @Inject constructor(val getFlashSaleProductUseCase: GetFlashSaleProductUseCase,
                                                        val getSellerStatusLabelUseCase: GetSellerStatusLabelUseCase) {

    fun getEligibleProductList(campaignId: String, offset: Int, rows: Int, q: String,
                               onSuccess: (FlashSaleProductViewModel) -> Unit, onError: (Throwable) -> Unit) {
        getFlashSaleProductUseCase.setParams(campaignId, offset, rows, q)
        getFlashSaleProductUseCase.execute({onSuccess(it.toEligibleSellerProductViewModel())}){
            //TODO use this = onError(it)
            onSuccess(Gson().fromJson("""{"id": 15233665,"shop_id": 479085}""",
                    FlashSaleProduct::class.java).toEligibleSellerProductViewModel())
        }
    }

    fun getSellerStatusLabels(onSuccess: (String) -> Unit, onError: (Throwable) -> Unit) {
        getSellerStatusLabelUseCase.execute(onSuccess){
            //TODO use this = onError(it)
            onSuccess("aaa")
        }
    }

    fun detachView() {
        getFlashSaleProductUseCase.unsubscribe()
        getSellerStatusLabelUseCase.unsubscribe()
    }
}