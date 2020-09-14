package com.tokopedia.oneclickcheckout.order.view.processor

import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.common.dispatchers.ExecutorDispatchers
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel
import com.tokopedia.oneclickcheckout.order.view.model.OrderCart
import com.tokopedia.oneclickcheckout.order.view.model.OrderPromo
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.ProductDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderSummaryPagePromoProcessor @Inject constructor(private val validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase,
                                                         private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
                                                         private val orderSummaryAnalytics: OrderSummaryAnalytics,
                                                         private val executorDispatchers: ExecutorDispatchers) {

    suspend fun validateUsePromo(validateUsePromoRequest: ValidateUsePromoRequest, lastValidateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?): ValidateUsePromoRevampUiModel? {
        OccIdlingResource.increment()
        val resultValidateUse = withContext(executorDispatchers.io) {
            try {
                val result = validateUsePromoRevampUseCase.createObservable(RequestParams.create().apply {
                    putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, validateUsePromoRequest)
                }).toBlocking().single()
                var isPromoReleased = false
                if (!lastValidateUsePromoRevampUiModel?.promoUiModel?.codes.isNullOrEmpty() && result.promoUiModel.codes.isNotEmpty() && result.promoUiModel.messageUiModel.state == "red") {
                    isPromoReleased = true
                } else {
                    result.promoUiModel.voucherOrderUiModels.firstOrNull { it?.messageUiModel?.state == "red" }?.let {
                        isPromoReleased = true
                    }
                }
                if (isPromoReleased) {
                    orderSummaryAnalytics.eventViewPromoDecreasedOrReleased(true)
                } else if (lastValidateUsePromoRevampUiModel != null && result.promoUiModel.benefitSummaryInfoUiModel.finalBenefitAmount < lastValidateUsePromoRevampUiModel.promoUiModel.benefitSummaryInfoUiModel.finalBenefitAmount) {
                    orderSummaryAnalytics.eventViewPromoDecreasedOrReleased(false)
                }
                return@withContext result
            } catch (t: Throwable) {
                return@withContext null
            }
        }
        OccIdlingResource.decrement()
        return resultValidateUse
    }

    suspend fun clearOldLogisticPromo(oldPromoCode: String) {
        withContext(executorDispatchers.io) {
            try {
                clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, arrayListOf(oldPromoCode), true)
                clearCacheAutoApplyStackUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
            } catch (t: Throwable) {
                //ignore throwable
            }
        }
    }

    suspend fun validateUseLogisticPromo(validateUsePromoRequest: ValidateUsePromoRequest, logisticPromoCode: String): Triple<Boolean, ValidateUsePromoRevampUiModel?, OccGlobalEvent> {
        OccIdlingResource.increment()
        val resultValidateUse = withContext(executorDispatchers.io) {
            try {
                val response = validateUsePromoRevampUseCase.createObservable(RequestParams.create().apply {
                    putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, validateUsePromoRequest)
                }).toBlocking().single()
                if (response.status.equals(STATUS_OK, true)) {
                    val voucherOrderUiModel = response.promoUiModel.voucherOrderUiModels.firstOrNull { it?.code == logisticPromoCode }
                    if (voucherOrderUiModel != null && voucherOrderUiModel.messageUiModel.state != "red") {
                        return@withContext Triple(true, response, OccGlobalEvent.Normal)
                    }
                }
                return@withContext Triple(false, response, OccGlobalEvent.Error(errorMessage = OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE))
            } catch (t: Throwable) {
                return@withContext Triple(false, null, OccGlobalEvent.Error(t.cause ?: t))
            }
        }
        OccIdlingResource.decrement()
        return resultValidateUse
    }

    suspend fun finalValidateUse(validateUsePromoRequest: ValidateUsePromoRequest): Pair<ValidateUsePromoRevampUiModel?, OccGlobalEvent> {
        OccIdlingResource.increment()
        val resultValidateUse = withContext(executorDispatchers.io) {
            try {
                val response = validateUsePromoRevampUseCase.createObservable(RequestParams.create().apply {
                    putObject(ValidateUsePromoRevampUseCase.PARAM_VALIDATE_USE, validateUsePromoRequest)
                }).toBlocking().single()
                return@withContext response to OccGlobalEvent.Loading
            } catch (t: Throwable) {
                return@withContext null to OccGlobalEvent.TriggerRefresh(throwable = t.cause ?: t)
            }
        }
        OccIdlingResource.decrement()
        return resultValidateUse
    }

    suspend fun cancelIneligiblePromoCheckout(promoCodeList: ArrayList<String>): Pair<Boolean, OccGlobalEvent> {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                clearCacheAutoApplyStackUseCase.setParams(ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE, promoCodeList, true)
                clearCacheAutoApplyStackUseCase.createObservable(RequestParams.EMPTY).toBlocking().single()
                return@withContext true to OccGlobalEvent.Loading
            } catch (t: Throwable) {
                return@withContext false to OccGlobalEvent.Error(t.cause ?: t)
            }
        }
        OccIdlingResource.decrement()
        return result
    }

    fun generatePromoRequest(orderCart: OrderCart, _orderShipment: OrderShipment, lastValidateUsePromoRequest: ValidateUsePromoRequest?, orderPromo: OrderPromo): PromoRequest {
        val promoRequest = PromoRequest()

        val ordersItem = Order()
        ordersItem.shopId = orderCart.shop.shopId.toLong()
        ordersItem.uniqueId = orderCart.cartString
        ordersItem.product_details = listOf(ProductDetail(orderCart.product.productId.toLong(), orderCart.product.quantity.orderQuantity))
        ordersItem.isChecked = true

        val shipping = _orderShipment
        ordersItem.shippingId = shipping.getRealShipperId()
        ordersItem.spId = shipping.getRealShipperProductId()

        if (shipping.isCheckInsurance && shipping.insuranceData != null) {
            ordersItem.isInsurancePrice = 1
        } else {
            ordersItem.isInsurancePrice = 0
        }

        val lastRequest = lastValidateUsePromoRequest

        ordersItem.codes = generateOrderPromoCodes(lastRequest, ordersItem.uniqueId, shipping, orderPromo)

        promoRequest.orders = listOf(ordersItem)
        promoRequest.state = CheckoutConstant.PARAM_CHECKOUT
        promoRequest.cartType = CheckoutConstant.PARAM_OCC

        if (lastRequest != null) {
            promoRequest.codes = ArrayList(lastRequest.codes.filterNotNull())
        } else {
            val globalCodes = orderPromo.lastApply?.codes ?: emptyList()
            promoRequest.codes = ArrayList(globalCodes)
        }
        return promoRequest
    }

    private fun generateOrderPromoCodes(lastRequest: ValidateUsePromoRequest?, uniqueId: String, shipping: OrderShipment, orderPromo: OrderPromo, shouldAddLogisticPromo: Boolean = true): MutableList<String> {
        var codes: MutableList<String> = ArrayList()
        val lastRequestOrderCodes = lastRequest?.orders?.firstOrNull()?.codes
        if (lastRequestOrderCodes != null) {
            codes = lastRequestOrderCodes
        } else {
            val voucherOrders = orderPromo.lastApply?.voucherOrders ?: emptyList()
            for (voucherOrder in voucherOrders) {
                if (voucherOrder.uniqueId.equals(uniqueId, true)) {
                    if (!codes.contains(voucherOrder.code)) {
                        codes.add(voucherOrder.code)
                    }
                }
            }
        }

        if (shouldAddLogisticPromo && shipping.isApplyLogisticPromo && shipping.logisticPromoViewModel != null && shipping.logisticPromoShipping != null) {
            if (!codes.contains(shipping.logisticPromoViewModel.promoCode)) {
                codes.add(shipping.logisticPromoViewModel.promoCode)
            }
        } else if (shipping.logisticPromoViewModel?.promoCode?.isNotEmpty() == true) {
            codes.remove(shipping.logisticPromoViewModel.promoCode)
        }
        return codes
    }

    fun generateValidateUsePromoRequest(shouldAddLogisticPromo: Boolean = true, lastValidateUsePromoRequest: ValidateUsePromoRequest?, orderCart: OrderCart, _orderShipment: OrderShipment, orderPromo: OrderPromo): ValidateUsePromoRequest {
        val validateUsePromoRequest = lastValidateUsePromoRequest ?: ValidateUsePromoRequest()

        val ordersItem = OrdersItem()
        ordersItem.shopId = orderCart.shop.shopId
        ordersItem.uniqueId = orderCart.cartString

        ordersItem.productDetails = listOf(ProductDetailsItem(orderCart.product.quantity.orderQuantity, orderCart.product.productId))

        val shipping = _orderShipment
        ordersItem.shippingId = shipping.getRealShipperId()
        ordersItem.spId = shipping.getRealShipperProductId()

        val lastRequest = lastValidateUsePromoRequest

        ordersItem.codes = generateOrderPromoCodes(lastRequest, ordersItem.uniqueId, shipping, orderPromo, shouldAddLogisticPromo)

        validateUsePromoRequest.orders = listOf(ordersItem)
        validateUsePromoRequest.state = CheckoutConstant.PARAM_CHECKOUT
        validateUsePromoRequest.cartType = CheckoutConstant.PARAM_OCC

        if (lastRequest != null) {
            validateUsePromoRequest.codes = lastRequest.codes
        } else {
            val globalCodes = orderPromo.lastApply?.codes ?: emptyList()
            validateUsePromoRequest.codes = globalCodes.toMutableList()
        }
        validateUsePromoRequest.skipApply = 0
        validateUsePromoRequest.isSuggested = 0

//        lastValidateUsePromoRequest = validateUsePromoRequest

        return validateUsePromoRequest
    }

    fun generateValidateUsePromoRequestWithBbo(logisticPromoUiModel: LogisticPromoUiModel, oldCode: String? = null, lastValidateUsePromoRequest: ValidateUsePromoRequest?, orderCart: OrderCart, _orderShipment: OrderShipment, orderPromo: OrderPromo): ValidateUsePromoRequest {
        return generateValidateUsePromoRequest(false, lastValidateUsePromoRequest, orderCart, _orderShipment, orderPromo).apply {
            orders[0]?.apply {
                shippingId = logisticPromoUiModel.shipperId
                spId = logisticPromoUiModel.shipperProductId
                if (oldCode != null) {
                    codes.remove(oldCode)
                }
                codes.add(logisticPromoUiModel.promoCode)
            }
        }
    }

    fun generateBboPromoCodes(shipping: OrderShipment): ArrayList<String> {
        if (shipping.isApplyLogisticPromo && shipping.logisticPromoViewModel != null && shipping.logisticPromoShipping != null) {
            return arrayListOf(shipping.logisticPromoViewModel.promoCode)
        }
        return ArrayList()
    }
}