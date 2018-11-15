package com.tokopedia.flashsale.management.product.view.presenter

import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.flashsale.management.product.data.FlashSaleDataContainer
import com.tokopedia.flashsale.management.product.data.FlashSaleMutationDeReserveResponseGQL
import com.tokopedia.flashsale.management.product.data.FlashSaleMutationReserveResponseGQL
import com.tokopedia.flashsale.management.product.domain.usecase.DereserveProductUseCase
import com.tokopedia.flashsale.management.product.domain.usecase.ReserveProductUseCase
import com.tokopedia.flashsale.management.product.domain.usecase.SubmitProductUseCase
import javax.inject.Inject

class FlashSaleProductDetailPresenter @Inject constructor(val reserveProductUseCase: ReserveProductUseCase,
                                                          val dereserveProductUseCase: DereserveProductUseCase,
                                                          val submitProductUseCase: SubmitProductUseCase,
                                                          val userSession: UserSession) {

    fun reserveProduct(campaignId: Int, criteriaId: Int, productId: Int, discountedPrice: Int, cashback: Int, customStock: Int,
                       onSuccess: (FlashSaleDataContainer) -> Unit, onError: (Throwable) -> Unit) {
        //TODO send the correct message
        reserveProductUseCase.setParams(campaignId, criteriaId, productId, discountedPrice, cashback, customStock,
                userSession.shopId.toInt())
        reserveProductUseCase.execute(
                {
                    if (it.flashSaleDataContainer.isSuccess()) {
                        onSuccess(it.flashSaleDataContainer)
                    } else {
                        onError(MessageErrorException(it.flashSaleDataContainer.message))
                    }
                }, onError)
    }

    fun dereserveProduct(campaignId: Int, productId: Int,
                         onSuccess: (FlashSaleDataContainer) -> Unit, onError: (Throwable) -> Unit) {
        //TODO send the correct message
        dereserveProductUseCase.setParams(campaignId, productId, userSession.shopId.toInt())
        dereserveProductUseCase.execute(
                {
                    if (it.flashSaleDataContainer.isSuccess()) {
                        onSuccess(it.flashSaleDataContainer)
                    } else {
                        onError(MessageErrorException(it.flashSaleDataContainer.message))
                    }
                }, onError)
    }

    fun submitProduct(campaignId: Int, productId:Int, onSuccess: (FlashSaleDataContainer) -> Unit, onError: (Throwable) -> Unit) {
        //TODO send the correct message
        submitProductUseCase.setParams(campaignId, userSession.shopId.toInt(), productId)
        submitProductUseCase.execute(
                {
                    if ( it.flashSaleDataContainer.isSuccess()) {
                        onSuccess(it.flashSaleDataContainer)
                    } else {
                        onError(MessageErrorException(it.flashSaleDataContainer.message))
                    }
                }, onError)
    }

    fun cancelJob() {
        reserveProductUseCase.cancelJobs()
        dereserveProductUseCase.cancelJobs()
        submitProductUseCase.cancelJobs()
    }
}