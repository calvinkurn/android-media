package com.tokopedia.buyerorderdetail.domain.usecases

import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailParams
import com.tokopedia.buyerorderdetail.presentation.model.BuyerOrderDetailUiModel
import com.tokopedia.buyerorderdetail.presentation.model.OrderResolutionUIModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.usecase.coroutines.UseCase
import java.util.HashMap
import javax.inject.Inject

class GetDetailWithResolutionUseCase @Inject constructor(
    private val getBuyerOrderDetailUseCase: GetBuyerOrderDetailUseCase,
    private val getOrderResolutionUseCase: GetOrderResolutionUseCase
): UseCase<BuyerOrderDetailUiModel>() {

    companion object {
        const val ORDER_ID = "ORDER_ID"
        const val PAYMENT_ID = "PAYMENT_ID"
        const val CART = "CART"

        const val PARAM_ORDER_ID = "orderID"
    }

    suspend fun execute(params: GetBuyerOrderDetailParams): BuyerOrderDetailUiModel {
        useCaseRequestParams.parameters[ORDER_ID] = params.orderId
        useCaseRequestParams.parameters[PAYMENT_ID] = params.paymentId
        useCaseRequestParams.parameters[CART] = params.cart
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): BuyerOrderDetailUiModel {
        val orderId: String = useCaseRequestParams.parameters[ORDER_ID] as String
        val paymentId: String = useCaseRequestParams.parameters[PAYMENT_ID] as String
        val cart: String = useCaseRequestParams.parameters[CART] as String
        val detailUIModel = getBuyerOrderDetailUseCase.execute(GetBuyerOrderDetailParams(cart, orderId, paymentId))
        if (detailUIModel.hasResoStatus.orFalse()) {
            detailUIModel.orderResolutionUIModel = executeResolutionQuery(orderId)
        }
        return detailUIModel
    }

    private suspend fun executeResolutionQuery(orderId: String): OrderResolutionUIModel {
        val orderIdLong = orderId.toLongOrZero()
        val params = HashMap<String, Any?>()
        params[PARAM_ORDER_ID] = orderIdLong
        getOrderResolutionUseCase.setRequestParams(params)
        return try {
            getOrderResolutionUseCase.execute()
        } catch (e: Exception) {
            OrderResolutionUIModel()
        }
    }

}