package com.tokopedia.flashsale.management.product.view.presenter

import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.flashsale.management.data.FlashSaleFilterProductListTypeDef
import com.tokopedia.flashsale.management.product.data.FlashSaleProductHeader
import com.tokopedia.flashsale.management.product.domain.usecase.GetFlashSaleProductUseCase
import javax.inject.Inject

class FlashSaleProductListPresenter @Inject constructor(val getFlashSaleProductUseCase: GetFlashSaleProductUseCase,
                                                        val userSession: UserSession) {

    fun getEligibleProductList(campaignId: Int, offset: Int, rows: Int, q: String,
                               @FlashSaleFilterProductListTypeDef filter: Int,
                               onSuccess: (FlashSaleProductHeader) -> Unit, onError: (Throwable) -> Unit) {
        getFlashSaleProductUseCase.setParams(campaignId, offset, rows, q, userSession.shopId.toInt(), filter)
        getFlashSaleProductUseCase.execute({
            onSuccess(it.flashSaleProductGQLData.data)
        }) {
            onError(it)
        }
    }

    fun detachView() {
        getFlashSaleProductUseCase.unsubscribe()
    }
}