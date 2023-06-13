package com.tokopedia.oneclickcheckout.order.view.processor

import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryPageEnhanceECommerce
import com.tokopedia.oneclickcheckout.order.data.checkout.AddOnItem
import com.tokopedia.oneclickcheckout.order.data.checkout.CheckoutOccRequest
import com.tokopedia.oneclickcheckout.order.data.checkout.OrderMetadata
import com.tokopedia.oneclickcheckout.order.data.checkout.OrderMetadata.Companion.FREE_SHIPPING_METADATA
import com.tokopedia.oneclickcheckout.order.data.checkout.OrderMetadata.Companion.PRESCRIPTION_IDS_METADATA
import com.tokopedia.oneclickcheckout.order.data.checkout.ParamCart
import com.tokopedia.oneclickcheckout.order.data.checkout.ParamData
import com.tokopedia.oneclickcheckout.order.data.checkout.ProductData
import com.tokopedia.oneclickcheckout.order.data.checkout.Profile
import com.tokopedia.oneclickcheckout.order.data.checkout.PromoRequest
import com.tokopedia.oneclickcheckout.order.data.checkout.ShippingInfo
import com.tokopedia.oneclickcheckout.order.data.checkout.ShopProduct
import com.tokopedia.oneclickcheckout.order.domain.CheckoutOccUseCase
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccData
import com.tokopedia.oneclickcheckout.order.view.model.CheckoutOccResult
import com.tokopedia.oneclickcheckout.order.view.model.OrderCart
import com.tokopedia.oneclickcheckout.order.view.model.OrderPayment
import com.tokopedia.oneclickcheckout.order.view.model.OrderProduct
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfile
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.oneclickcheckout.order.view.model.OrderTotal
import com.tokopedia.oneclickcheckout.order.view.model.PriceChangeMessage
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderSummaryPageCheckoutProcessor @Inject constructor(
    private val checkoutOccUseCase: CheckoutOccUseCase,
    private val orderSummaryAnalytics: OrderSummaryAnalytics,
    private val executorDispatchers: CoroutineDispatchers,
    private val gson: Gson
) {

    private fun generateShopPromos(
        finalPromo: ValidateUsePromoRevampUiModel?,
        orderCart: OrderCart
    ): List<PromoRequest> {
        if (finalPromo != null) {
            val list: ArrayList<PromoRequest> = ArrayList()
            for (voucherOrder in finalPromo.promoUiModel.voucherOrderUiModels) {
                if (orderCart.cartString == voucherOrder.uniqueId &&
                    voucherOrder.messageUiModel.state != "red" &&
                    voucherOrder.code.isNotEmpty() &&
                    voucherOrder.type.isNotEmpty()
                ) {
                    list.add(PromoRequest(voucherOrder.type, voucherOrder.code))
                }
            }
            return list
        }
        return emptyList()
    }

    private fun generateCheckoutPromos(finalPromo: ValidateUsePromoRevampUiModel?): List<PromoRequest> {
        val list = ArrayList<PromoRequest>()
        if (finalPromo != null &&
            finalPromo.promoUiModel.codes.isNotEmpty() &&
            finalPromo.promoUiModel.messageUiModel.state != "red"
        ) {
            for (code in finalPromo.promoUiModel.codes) {
                list.add(PromoRequest("global", code))
            }
        }
        return list
    }

    suspend fun doCheckout(
        finalPromo: ValidateUsePromoRevampUiModel?,
        orderCart: OrderCart,
        products: List<OrderProduct>,
        shop: OrderShop,
        profile: OrderProfile,
        orderShipment: OrderShipment,
        orderPayment: OrderPayment,
        orderTotal: OrderTotal,
        userId: String,
        orderSummaryPageEnhanceECommerce: OrderSummaryPageEnhanceECommerce,
        prescriptionIds: List<String?>?
    ): Pair<CheckoutOccResult?, OccGlobalEvent?> {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            val shopPromos = generateShopPromos(finalPromo, orderCart)
            val checkoutPromos = generateCheckoutPromos(finalPromo)
            val allPromoCodes = checkoutPromos.map { it.code } + shopPromos.map { it.code }
            val checkoutProducts: ArrayList<ProductData> = ArrayList()
            products.forEach {
                if (!it.isError) {
                    val addOnProductLevelItems = mutableListOf<AddOnItem>()
                    val addOnItemModel = it.addOn.addOnsDataItemModelList.firstOrNull()
                    if (it.addOn.status == 1 && addOnItemModel != null) {
                        addOnProductLevelItems.add(
                            AddOnItem(
                                itemType = AddOnConstant.ADD_ON_LEVEL_PRODUCT,
                                itemId = addOnItemModel.addOnId,
                                itemQty = addOnItemModel.addOnQty,
                                itemMetadata = gson.toJson(addOnItemModel.addOnMetadata)
                            )
                        )
                    }
                    checkoutProducts.add(
                        ProductData(
                            productId = it.productId,
                            productQuantity = it.orderQuantity,
                            productNotes = it.notes,
                            isPPP = it.purchaseProtectionPlanData.stateChecked == PurchaseProtectionPlanData.STATE_TICKED,
                            items = addOnProductLevelItems
                        )
                    )
                }
            }

            val addOnShopLevelItems = mutableListOf<AddOnItem>()
            val addOnItemModel = shop.addOn.addOnsDataItemModelList.firstOrNull()
            if (shop.addOn.status == 1 && addOnItemModel != null) {
                addOnShopLevelItems.add(
                    AddOnItem(
                        itemType = AddOnConstant.ADD_ON_LEVEL_PRODUCT,
                        itemId = addOnItemModel.addOnId,
                        itemQty = addOnItemModel.addOnQty,
                        itemMetadata = gson.toJson(addOnItemModel.addOnMetadata)
                    )
                )
            }

            val orderMetadata = arrayListOf<OrderMetadata>()
            val logisticPromoUiModel = orderShipment.logisticPromoViewModel
            if (orderShipment.isApplyLogisticPromo &&
                orderShipment.logisticPromoShipping != null &&
                logisticPromoUiModel != null
            ) {
                if (logisticPromoUiModel.freeShippingMetadata.isNotBlank() &&
                    shopPromos.firstOrNull { it.code == logisticPromoUiModel.promoCode } != null
                ) {
                    // only add free shipping metadata if the order contains the promo logistic
                    orderMetadata.add(
                        OrderMetadata(
                            FREE_SHIPPING_METADATA,
                            logisticPromoUiModel.freeShippingMetadata
                        )
                    )
                }
            }
            if (prescriptionIds != null && prescriptionIds.isNotEmpty()) {
                orderMetadata.add(
                    OrderMetadata(
                        PRESCRIPTION_IDS_METADATA,
                        prescriptionIds.toString()
                    )
                )
            }
            val param = CheckoutOccRequest(
                Profile(profile.profileId),
                ParamCart(
                    data = listOf(
                        ParamData(
                            profile.address.addressId.toLongOrZero(),
                            listOf(
                                ShopProduct(
                                    shopId = shop.shopId.toLongOrZero(),
                                    isPreorder = products.first().isPreOrder,
                                    warehouseId = shop.warehouseId.toLongOrZero(),
                                    finsurance = if (orderShipment.insurance.isCheckInsurance) 1 else 0,
                                    productData = checkoutProducts,
                                    shippingInfo = ShippingInfo(
                                        orderShipment.getRealShipperId(),
                                        orderShipment.getRealShipperProductId(),
                                        orderShipment.getRealRatesId(),
                                        orderShipment.getRealUt(),
                                        orderShipment.getRealChecksum()
                                    ),
                                    promos = shopPromos,
                                    items = addOnShopLevelItems,
                                    orderMetadata = orderMetadata
                                )
                            )
                        )
                    ),
                    promos = checkoutPromos,
                    mode = if (orderTotal.isButtonPay) 0 else 1,
                    featureType = if (shop.isTokoNow) {
                        ParamCart.FEATURE_TYPE_TOKONOW
                    } else {
                        ParamCart.FEATURE_TYPE_OCC_MULTI_NON_TOKONOW
                    }
                )
            )

            try {
                val checkoutOccData = checkoutOccUseCase.executeSuspend(param)
                if (checkoutOccData.status.equals(STATUS_OK, true)) {
                    if (checkoutOccData.result.success == 1 ||
                        checkoutOccData.result.paymentParameter.redirectParam.url.isNotEmpty()
                    ) {
                        var paymentType = profile.payment.gatewayName
                        if (paymentType.isBlank()) {
                            paymentType = OrderSummaryPageEnhanceECommerce.DEFAULT_EMPTY_VALUE
                        }
                        val tenureType: String =
                            if (orderPayment.walletData.isGoCicil &&
                                orderPayment.walletData.goCicilData.selectedTerm != null
                            ) {
                                orderPayment.walletData.goCicilData.selectedTerm.installmentTerm.toString()
                            } else if (orderPayment.creditCard.selectedTerm != null) {
                                orderPayment.creditCard.selectedTerm.term.toString()
                            } else {
                                ""
                            }
                        products.forEach {
                            if (!it.isError && it.purchaseProtectionPlanData.isProtectionAvailable) {
                                orderSummaryAnalytics.eventPPClickBayar(
                                    userId,
                                    it.categoryId,
                                    it.purchaseProtectionPlanData.protectionTitle,
                                    it.purchaseProtectionPlanData.protectionPricePerProduct,
                                    it.cartId,
                                    if (it.purchaseProtectionPlanData.stateChecked == PurchaseProtectionPlanData.STATE_TICKED) {
                                        ConstantTransactionAnalytics.EventLabel.SUCCESS_TICKED_PPP
                                    } else {
                                        ConstantTransactionAnalytics.EventLabel.SUCCESS_UNTICKED_PPP
                                    }
                                )
                            }
                        }
                        val transactionId =
                            getTransactionId(checkoutOccData.result.paymentParameter.redirectParam.form)
                        checkoutOccData.result.paymentParameter.transactionId = transactionId
                        orderSummaryAnalytics.eventClickBayarSuccess(
                            orderTotal.isButtonChoosePayment,
                            userId,
                            transactionId,
                            paymentType,
                            tenureType,
                            orderSummaryPageEnhanceECommerce.apply {
                                dataList.forEach {
                                    setPromoCode(allPromoCodes, it)
                                }
                            }.build(
                                OrderSummaryPageEnhanceECommerce.STEP_2,
                                OrderSummaryPageEnhanceECommerce.STEP_2_OPTION
                            )
                        )
                        return@withContext checkoutOccData.result to null
                    }
                    return@withContext null to onCheckoutError(checkoutOccData, orderTotal)
                } else {
                    return@withContext null to OccGlobalEvent.TriggerRefresh(
                        errorMessage = checkoutOccData.headerMessage
                            ?: DEFAULT_ERROR_MESSAGE
                    )
                }
            } catch (t: Throwable) {
                return@withContext null to OccGlobalEvent.Error(t)
            }
        }
        OccIdlingResource.decrement()
        return result
    }

    private fun onCheckoutError(
        checkoutOccData: CheckoutOccData,
        orderTotal: OrderTotal
    ): OccGlobalEvent {
        val error = checkoutOccData.result.error
        val errorCode = error.code
        orderSummaryAnalytics.eventClickBayarNotSuccess(orderTotal.isButtonChoosePayment, errorCode)
        return when {
            checkoutOccData.result.prompt.shouldShowPrompt() -> {
                OccGlobalEvent.Prompt(checkoutOccData.result.prompt)
            }
            errorCode == OrderSummaryPageViewModel.ERROR_CODE_PRICE_CHANGE -> {
                OccGlobalEvent.PriceChangeError(
                    PriceChangeMessage(
                        OrderSummaryPageViewModel.PRICE_CHANGE_ERROR_MESSAGE,
                        error.message,
                        OrderSummaryPageViewModel.PRICE_CHANGE_ACTION_MESSAGE
                    )
                )
            }
            error.message.isNotBlank() -> {
                OccGlobalEvent.TriggerRefresh(errorMessage = error.message)
            }
            else -> {
                OccGlobalEvent.TriggerRefresh(errorMessage = "Terjadi kesalahan dengan kode $errorCode")
            }
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
