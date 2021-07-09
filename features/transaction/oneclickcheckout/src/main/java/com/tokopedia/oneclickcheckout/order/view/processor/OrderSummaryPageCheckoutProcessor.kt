package com.tokopedia.oneclickcheckout.order.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryPageEnhanceECommerce
import com.tokopedia.oneclickcheckout.order.data.checkout.*
import com.tokopedia.oneclickcheckout.order.domain.CheckoutOccUseCase
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel
import com.tokopedia.oneclickcheckout.order.view.bottomsheet.ErrorCheckoutBottomSheet
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderSummaryPageCheckoutProcessor @Inject constructor(private val checkoutOccUseCase: CheckoutOccUseCase,
                                                            private val orderSummaryAnalytics: OrderSummaryAnalytics,
                                                            private val executorDispatchers: CoroutineDispatchers) {

    private fun generateShopPromos(finalPromo: ValidateUsePromoRevampUiModel?, orderCart: OrderCart): List<PromoRequest> {
        if (finalPromo != null) {
            val list: ArrayList<PromoRequest> = ArrayList()
            for (voucherOrder in finalPromo.promoUiModel.voucherOrderUiModels) {
                if (orderCart.cartString == voucherOrder?.uniqueId && voucherOrder.messageUiModel.state != "red" &&
                        voucherOrder.code.isNotEmpty() && voucherOrder.type.isNotEmpty()) {
                    list.add(PromoRequest(voucherOrder.type, voucherOrder.code))
                }
            }
            return list
        }
        return emptyList()
    }

    private fun generateCheckoutPromos(finalPromo: ValidateUsePromoRevampUiModel?): List<PromoRequest> {
        val list = ArrayList<PromoRequest>()
        if (finalPromo != null && finalPromo.promoUiModel.codes.isNotEmpty() && finalPromo.promoUiModel.messageUiModel.state != "red") {
            for (code in finalPromo.promoUiModel.codes) {
                list.add(PromoRequest("global", code))
            }
        }
        return list
    }

    suspend fun doCheckout(finalPromo: ValidateUsePromoRevampUiModel?,
                           orderCart: OrderCart,
                           products: List<OrderProduct>,
                           shop: OrderShop,
                           profile: OrderProfile,
                           orderShipment: OrderShipment,
                           orderTotal: OrderTotal,
                           userId: String,
                           orderSummaryPageEnhanceECommerce: OrderSummaryPageEnhanceECommerce): Pair<CheckoutOccResult?, OccGlobalEvent?> {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            val shopPromos = generateShopPromos(finalPromo, orderCart)
            val checkoutPromos = generateCheckoutPromos(finalPromo)
            val allPromoCodes = checkoutPromos.map { it.code } + shopPromos.map { it.code }
            val checkoutProducts: ArrayList<ProductData> = ArrayList()
            products.forEach {
                if (!it.isError) {
                    checkoutProducts.add(ProductData(
                            it.productId,
                            it.quantity.orderQuantity,
                            it.notes,
                            it.purchaseProtectionPlanData.stateChecked == PurchaseProtectionPlanData.STATE_TICKED
                    ))
                }
            }
            val param = CheckoutOccRequest(Profile(profile.profileId), ParamCart(data = listOf(ParamData(
                    profile.address.addressId,
                    listOf(
                            ShopProduct(
                                    shopId = shop.shopId,
                                    isPreorder = products.first().isPreOrder,
                                    warehouseId = products.first().warehouseId,
                                    finsurance = if (orderShipment.insurance.isCheckInsurance) 1 else 0,
                                    productData = checkoutProducts,
                                    shippingInfo = ShippingInfo(
                                            orderShipment.getRealShipperId(),
                                            orderShipment.getRealShipperProductId(),
                                            orderShipment.getRealRatesId(),
                                            orderShipment.getRealUt(),
                                            orderShipment.getRealChecksum()
                                    ),
                                    promos = shopPromos
                            )
                    )
            )), promos = checkoutPromos, mode = if (orderTotal.isButtonPay) 0 else 1))

            try {
                val checkoutOccData = checkoutOccUseCase.executeSuspend(param)
                if (checkoutOccData.status.equals(STATUS_OK, true)) {
                    if (checkoutOccData.result.success == 1 || checkoutOccData.result.paymentParameter.redirectParam.url.isNotEmpty()) {
                        var paymentType = profile.payment.gatewayName
                        if (paymentType.isBlank()) {
                            paymentType = OrderSummaryPageEnhanceECommerce.DEFAULT_EMPTY_VALUE
                        }
                        products.forEach {
                            if (!it.isError && it.purchaseProtectionPlanData.isProtectionAvailable) {
                                orderSummaryAnalytics.eventPPClickBayar(userId,
                                        it.categoryId,
                                        "",
                                        it.purchaseProtectionPlanData.protectionTitle,
                                        it.purchaseProtectionPlanData.stateChecked == PurchaseProtectionPlanData.STATE_TICKED,
                                        orderSummaryPageEnhanceECommerce.buildForPP(OrderSummaryPageEnhanceECommerce.STEP_2, OrderSummaryPageEnhanceECommerce.STEP_2_OPTION))
                            }
                        }
                        orderSummaryAnalytics.eventClickBayarSuccess(orderTotal.isButtonChoosePayment,
                                userId,
                                getTransactionId(checkoutOccData.result.paymentParameter.redirectParam.form),
                                paymentType,
                                orderSummaryPageEnhanceECommerce.apply {
                                    dataList.forEach {
                                        setPromoCode(allPromoCodes, it)
//                                        setShippingPrice(orderShipment.getRealShippingPrice().toString(), it)
                                    }
                                }.build(OrderSummaryPageEnhanceECommerce.STEP_2, OrderSummaryPageEnhanceECommerce.STEP_2_OPTION)
                        )
                        return@withContext checkoutOccData.result to null
                    }
                    return@withContext null to onCheckoutError(checkoutOccData, orderTotal)
                } else {
                    return@withContext null to OccGlobalEvent.TriggerRefresh(errorMessage = checkoutOccData.headerMessage
                            ?: DEFAULT_ERROR_MESSAGE)
                }
            } catch (t: Throwable) {
                return@withContext null to OccGlobalEvent.Error(t)
            }
        }
        OccIdlingResource.decrement()
        return result
    }

    private fun onCheckoutError(checkoutOccData: CheckoutOccData, orderTotal: OrderTotal): OccGlobalEvent {
        val error = checkoutOccData.result.error
        val errorCode = error.code
        orderSummaryAnalytics.eventClickBayarNotSuccess(orderTotal.isButtonChoosePayment, errorCode)
        return if (checkoutOccData.result.prompt.shouldShowPrompt()) {
            OccGlobalEvent.Prompt(checkoutOccData.result.prompt)
        } else if (errorCode == ErrorCheckoutBottomSheet.ERROR_CODE_PRODUCT_STOCK_EMPTY || errorCode == ErrorCheckoutBottomSheet.ERROR_CODE_PRODUCT_ERROR || errorCode == ErrorCheckoutBottomSheet.ERROR_CODE_SHOP_CLOSED) {
            OccGlobalEvent.CheckoutError(error)
        } else if (errorCode == OrderSummaryPageViewModel.ERROR_CODE_PRICE_CHANGE) {
            OccGlobalEvent.PriceChangeError(PriceChangeMessage(OrderSummaryPageViewModel.PRICE_CHANGE_ERROR_MESSAGE, error.message, OrderSummaryPageViewModel.PRICE_CHANGE_ACTION_MESSAGE))
        } else if (error.message.isNotBlank()) {
            OccGlobalEvent.TriggerRefresh(errorMessage = error.message)
        } else {
            OccGlobalEvent.TriggerRefresh(errorMessage = "Terjadi kesalahan dengan kode $errorCode")
        }
    }

    private fun getTransactionId(query: String): String {
        val keyLength = OrderSummaryPageViewModel.TRANSACTION_ID_KEY.length
        val keyIndex = query.indexOf(OrderSummaryPageViewModel.TRANSACTION_ID_KEY)
        if (keyIndex > -1 && query[keyIndex + keyLength] == '=') {
            val nextAmpersand = query.indexOf('&', keyIndex)
            val end = if (nextAmpersand > -1) nextAmpersand else query.length
            val start = keyIndex + keyLength + 1

            if (end > start) {
                return query.substring(start, end)
            }
        }
        return ""
    }
}