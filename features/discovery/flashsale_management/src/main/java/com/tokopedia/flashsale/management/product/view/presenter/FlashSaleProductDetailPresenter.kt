package com.tokopedia.flashsale.management.product.view.presenter

import com.tokopedia.flashsale.management.product.data.FlashSaleDataContainer
import com.tokopedia.flashsale.management.product.domain.usecase.DereserveProductUseCase
import com.tokopedia.flashsale.management.product.domain.usecase.ReserveProductUseCase
import com.tokopedia.flashsale.management.product.domain.usecase.SubmitProductUseCase
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import javax.inject.Inject

class FlashSaleProductDetailPresenter @Inject constructor(val reserveProductUseCase: ReserveProductUseCase,
                                                          val dereserveProductUseCase: DereserveProductUseCase,
                                                          val submitProductUseCase: SubmitProductUseCase,
                                                          val userSession: UserSessionInterface) {

    fun reserveProduct(campaignId: Int, criteriaId: Int, productId: Int, discountedPrice: Int, cashback: Int, customStock: Int,
                       onSuccess: (FlashSaleDataContainer) -> Unit, onError: (Throwable) -> Unit) {
        reserveProductUseCase.setParams(campaignId, criteriaId, productId, discountedPrice, cashback, customStock,
                userSession.shopId.toInt())
        reserveProductUseCase.execute(
                {
                    if (it.flashSaleDataContainer.isSuccess()) {
                        onSuccess(it.flashSaleDataContainer)
                    } else {
                        onError(RuntimeException(it.flashSaleDataContainer.statusCode.toString()))
                    }
                }, onError)
    }

    fun dereserveProduct(campaignId: Int, productId: Int,
                         onSuccess: (FlashSaleDataContainer) -> Unit, onError: (Throwable) -> Unit) {
        dereserveProductUseCase.setParams(campaignId, productId, userSession.shopId.toInt())
        dereserveProductUseCase.execute(
                {
                    if (it.flashSaleDataContainer.isSuccess()) {
                        onSuccess(it.flashSaleDataContainer)
                    } else {
                        onError(RuntimeException(it.flashSaleDataContainer.statusCode.toString()))
                    }
                }, onError)
    }

    fun submitProduct(campaignId: Int, productId:Int, onSuccess: (FlashSaleDataContainer) -> Unit, onError: (Throwable) -> Unit) {
        submitProductUseCase.setParams(campaignId, userSession.shopId.toInt(), productId)
        submitProductUseCase.execute(
                {
                    if ( it.flashSaleDataContainer.isSuccess()) {
                        onSuccess(it.flashSaleDataContainer)
                    } else {
                        onError(RuntimeException(it.flashSaleDataContainer.statusCode.toString()))
                    }
                }, onError)
    }

    fun cancelJob() {
        reserveProductUseCase.cancelJobs()
        dereserveProductUseCase.cancelJobs()
        submitProductUseCase.cancelJobs()
    }
}