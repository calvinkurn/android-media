package com.tokopedia.oneclickcheckout.order.view.processor

import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.data.checkout.*
import com.tokopedia.oneclickcheckout.order.domain.CheckoutOccUseCase
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel
import com.tokopedia.oneclickcheckout.order.view.bottomsheet.ErrorCheckoutBottomSheet
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel

class OrderSummaryPageCheckoutProcessor(private val checkoutOccUseCase: CheckoutOccUseCase,
                                        private val orderSummaryAnalytics: OrderSummaryAnalytics) {

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

    private fun doCheckout(finalPromo: ValidateUsePromoRevampUiModel?,
                           orderCart: OrderCart,
                           product: OrderProduct,
                           shop: OrderShop,
                           pref: OrderPreference,
                           orderShipment: OrderShipment,
                           orderTotal: OrderTotal,
                           onSuccessCheckout: (CheckoutOccResult) -> Unit) : Pair<String, Any> {
        val shopPromos = generateShopPromos(finalPromo, orderCart)
        val checkoutPromos = generateCheckoutPromos(finalPromo)
        val allPromoCodes = checkoutPromos.map { it.code } + shopPromos.map { it.code }
        val param = CheckoutOccRequest(Profile(pref.preference.profileId), ParamCart(data = listOf(ParamData(
                pref.preference.address.addressId,
                listOf(
                        ShopProduct(
                                shopId = shop.shopId,
                                isPreorder = product.isPreorder,
                                warehouseId = product.warehouseId,
                                finsurance = if (orderShipment.isCheckInsurance) 1 else 0,
                                productData = listOf(
                                        ProductData(
                                                product.productId,
                                                product.quantity.orderQuantity,
                                                product.notes
                                        )
                                ),
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
        )), promos = checkoutPromos, mode = if (orderTotal.isButtonChoosePayment) 1 else 0))
        OccIdlingResource.increment()
        checkoutOccUseCase.execute(param, { checkoutOccData: CheckoutOccData ->
            if (checkoutOccData.status.equals(STATUS_OK, true)) {
                if (checkoutOccData.result.success == 1 || checkoutOccData.result.paymentParameter.redirectParam.url.isNotEmpty()) {
                    onSuccessCheckout(checkoutOccData.result)
//                    orderSummaryAnalytics.eventClickBayarSuccess(orderTotal.isButtonChoosePayment, getTransactionId(checkoutOccData.result.paymentParameter.redirectParam.form), generateOspEe(OrderSummaryPageEnhanceECommerce.STEP_2, OrderSummaryPageEnhanceECommerce.STEP_2_OPTION, allPromoCodes))
                } else {
                    val error = checkoutOccData.result.error
                    val errorCode = error.code
                    orderSummaryAnalytics.eventClickBayarNotSuccess(orderTotal.isButtonChoosePayment, errorCode)
                    if (checkoutOccData.result.prompt.shouldShowPrompt()) {
//                        globalEvent.value = OccGlobalEvent.Prompt(checkoutOccData.result.prompt)
                    } else if (errorCode == ErrorCheckoutBottomSheet.ERROR_CODE_PRODUCT_STOCK_EMPTY || errorCode == ErrorCheckoutBottomSheet.ERROR_CODE_PRODUCT_ERROR || errorCode == ErrorCheckoutBottomSheet.ERROR_CODE_SHOP_CLOSED) {
//                        globalEvent.value = OccGlobalEvent.CheckoutError(error)
                    } else if (errorCode == OrderSummaryPageViewModel.ERROR_CODE_PRICE_CHANGE) {
//                        globalEvent.value = OccGlobalEvent.PriceChangeError(PriceChangeMessage(OrderSummaryPageViewModel.PRICE_CHANGE_ERROR_MESSAGE, error.message, OrderSummaryPageViewModel.PRICE_CHANGE_ACTION_MESSAGE))
                    } else if (error.message.isNotBlank()) {
//                        globalEvent.value = OccGlobalEvent.TriggerRefresh(errorMessage = error.message)
                    } else {
//                        globalEvent.value = OccGlobalEvent.TriggerRefresh(errorMessage = "Terjadi kesalahan dengan kode $errorCode")
                    }
                }
            } else {
//                globalEvent.value = OccGlobalEvent.TriggerRefresh(errorMessage = checkoutOccData.headerMessage
//                        ?: DEFAULT_ERROR_MESSAGE)
            }
            OccIdlingResource.decrement()
        }, { throwable: Throwable ->
//            globalEvent.value = OccGlobalEvent.Error(throwable)
            OccIdlingResource.decrement()
        })
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