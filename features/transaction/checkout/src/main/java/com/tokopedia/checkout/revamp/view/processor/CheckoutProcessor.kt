package com.tokopedia.checkout.revamp.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.pdp.AtcBuyType
import com.tokopedia.checkout.data.model.request.checkout.Carts
import com.tokopedia.checkout.data.model.request.checkout.CheckoutRequest
import com.tokopedia.checkout.data.model.request.checkout.Data
import com.tokopedia.checkout.data.model.request.checkout.Egold
import com.tokopedia.checkout.data.model.request.checkout.FEATURE_TYPE_OCC_MULTI_NON_TOKONOW
import com.tokopedia.checkout.data.model.request.checkout.FEATURE_TYPE_OCC_MULTI_TOKONOW
import com.tokopedia.checkout.data.model.request.checkout.FEATURE_TYPE_REGULAR_PRODUCT
import com.tokopedia.checkout.data.model.request.checkout.FEATURE_TYPE_TOKONOW_PRODUCT
import com.tokopedia.checkout.data.model.request.checkout.Payment
import com.tokopedia.checkout.data.model.request.checkout.Promo
import com.tokopedia.checkout.data.model.request.checkout.TokopediaCorner
import com.tokopedia.checkout.data.model.request.checkout.cross_sell.CrossSellItemRequestModel
import com.tokopedia.checkout.data.model.request.checkout.cross_sell.CrossSellRequest
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.checkout.CheckoutData
import com.tokopedia.checkout.domain.usecase.CheckoutUseCase
import com.tokopedia.checkout.revamp.view.crossSellGroup
import com.tokopedia.checkout.revamp.view.firstOrNullInstanceOf
import com.tokopedia.checkout.revamp.view.payment
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutDonationModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEgoldModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.upsell
import com.tokopedia.checkout.view.CheckoutLogger
import com.tokopedia.checkout.view.converter.ShipmentDataRequestConverter
import com.tokopedia.checkoutpayment.domain.PaymentWidgetData
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckoutProcessor @Inject constructor(
    private val checkoutGqlUseCase: CheckoutUseCase,
    private val shipmentDataRequestConverter: ShipmentDataRequestConverter,
    private val checkoutAnalyticsCourierSelection: CheckoutAnalyticsCourierSelection,
    private val userSessionInterface: UserSessionInterface,
    private val helper: CheckoutDataHelper,
    private val dispatchers: CoroutineDispatchers
) {
    suspend fun doCheckout(
        listData: List<CheckoutItem>,
        recipientAddressModel: RecipientAddressModel,
        validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?,
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInDropOff: Boolean,
        deviceId: String,
        checkoutLeasingId: String,
        fingerprintPublicKey: String?,
        hasClearPromoBeforeCheckout: Boolean,
        cartType: String
    ): CheckoutResult {
        val checkoutRequest = generateCheckoutRequest(
            listData,
            recipientAddressModel,
            validateUsePromoRevampUiModel,
            checkoutLeasingId,
            isTradeInDropOff,
            cartType
        )
        if (checkoutRequest.data.isNotEmpty() && checkoutRequest.data.first().groupOrders.isNotEmpty()) {
            // Get additional param for trade in analytics
            var deviceModel = ""
            var devicePrice = 0L
            var diagnosticId = ""
            if (listData.isNotEmpty()) {
                val cartItemModel = listData.firstOrNullInstanceOf(CheckoutProductModel::class.java)
                if (cartItemModel != null) {
                    deviceModel = cartItemModel.deviceModel
                    devicePrice = cartItemModel.oldDevicePrice
                    diagnosticId = cartItemModel.diagnosticId
                }
            }
            val currentPayment = listData.payment()?.data?.paymentWidgetData?.firstOrNull()
            val paymentParam = generatePayment(currentPayment)
            val params = generateCheckoutParams(
                isOneClickShipment,
                isTradeIn,
                isTradeInDropOff,
                deviceId,
                checkoutRequest,
                "",
                fingerprintPublicKey,
                paymentParam,
                cartType
            )
            try {
                val checkoutData = withContext(dispatchers.io) {
                    checkoutGqlUseCase(params)
                }
                if (!checkoutData.isError) {
                    if (listData.crossSellGroup()!!.crossSellList.indexOfFirst { it is CheckoutCrossSellModel && it.isChecked } > -1) {
                        triggerCrossSellClickPilihPembayaran(listData)
                    }
                    return CheckoutResult(
                        true,
                        checkoutData,
                        checkoutData.transactionId,
                        deviceModel,
                        devicePrice,
                        diagnosticId,
                        hasClearPromoBeforeCheckout,
                        null,
                        checkoutRequest
                    )
                } else if (checkoutData.priceValidationData.isUpdated) {
                    return CheckoutResult(
                        false,
                        checkoutData,
                        checkoutData.transactionId,
                        deviceModel,
                        devicePrice,
                        diagnosticId,
                        hasClearPromoBeforeCheckout,
                        null,
                        checkoutRequest
                    )
                } else if (checkoutData.prompt.eligible) {
                    return CheckoutResult(
                        false,
                        checkoutData,
                        checkoutData.transactionId,
                        deviceModel,
                        devicePrice,
                        diagnosticId,
                        hasClearPromoBeforeCheckout,
                        null,
                        checkoutRequest
                    )
                } else {
                    return CheckoutResult(
                        false,
                        checkoutData,
                        checkoutData.transactionId,
                        deviceModel,
                        devicePrice,
                        diagnosticId,
                        hasClearPromoBeforeCheckout,
                        null,
                        checkoutRequest
                    )
                }
            } catch (e: Throwable) {
                CheckoutLogger.logOnErrorCheckout(
                    e,
                    checkoutRequest.toString(),
                    isOneClickShipment,
                    isTradeIn,
                    isTradeInDropOff
                )
                return CheckoutResult(
                    false,
                    null,
                    "",
                    deviceModel,
                    devicePrice,
                    diagnosticId,
                    hasClearPromoBeforeCheckout,
                    e,
                    checkoutRequest
                )
            }
        } else {
            return CheckoutResult(
                false,
                null,
                "",
                "",
                0,
                "",
                hasClearPromoBeforeCheckout,
                null,
                checkoutRequest
            )
        }
    }

    private fun generateCheckoutRequest(
        listData: List<CheckoutItem>,
        recipientAddressModel: RecipientAddressModel,
        validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel?,
        checkoutLeasingId: String,
        isTradeInDropOff: Boolean,
        cartType: String
    ): Carts {
        // Set promo merchant request data
        val data = removeErrorShopProduct(listData, recipientAddressModel, isTradeInDropOff)
        if (validateUsePromoRevampUiModel != null) {
            setCheckoutRequestPromoData(data, validateUsePromoRevampUiModel)
        }
        var cornerData: TokopediaCorner? = null
        if (recipientAddressModel.isCornerAddress) {
            cornerData = TokopediaCorner(
                true,
                recipientAddressModel.userCornerId,
                recipientAddressModel.cornerId
                    .toLongOrZero()
            )
        }
        val egoldData = Egold()
        val crossSellGroup = listData.crossSellGroup()
        val egoldAttribute =
            crossSellGroup?.crossSellList?.firstOrNullInstanceOf(CheckoutEgoldModel::class.java)?.egoldAttributeModel
        if (egoldAttribute != null && egoldAttribute.isEligible) {
            egoldData.isEgold = egoldAttribute.isChecked
            egoldData.goldAmount = egoldAttribute.buyEgoldValue
        }
        val listShipmentCrossSellModel =
            crossSellGroup?.crossSellList?.filterIsInstance(CheckoutCrossSellModel::class.java)
                ?: emptyList()
        val crossSellRequest = CrossSellRequest()
        val listCrossSellItemRequest = ArrayList<CrossSellItemRequestModel>()
        if (listShipmentCrossSellModel.isNotEmpty()) {
            val crossSellItemRequestModel = CrossSellItemRequestModel()
            for (shipmentCrossSellModel in listShipmentCrossSellModel) {
                if (shipmentCrossSellModel.isChecked) {
                    crossSellItemRequestModel.id =
                        shipmentCrossSellModel.crossSellModel.id
                            .toLongOrZero()
                    crossSellItemRequestModel.price = shipmentCrossSellModel.crossSellModel.price
                    crossSellItemRequestModel.additionalVerticalId =
                        shipmentCrossSellModel.crossSellModel.additionalVerticalId
                            .toLongOrZero()
                    crossSellItemRequestModel.transactionType =
                        shipmentCrossSellModel.crossSellModel.transactionType
                    listCrossSellItemRequest.add(crossSellItemRequestModel)
                }
            }
        }
        val shipmentNewUpsellModel = listData.upsell()
        if (shipmentNewUpsellModel != null && shipmentNewUpsellModel.upsell.isSelected && shipmentNewUpsellModel.upsell.isShow) {
            val crossSellItemRequestModel = CrossSellItemRequestModel()
            crossSellItemRequestModel.id = shipmentNewUpsellModel.upsell.id
                .toLongOrZero()
            crossSellItemRequestModel.price = shipmentNewUpsellModel.upsell.price.toDouble()
            crossSellItemRequestModel.additionalVerticalId =
                shipmentNewUpsellModel.upsell.additionalVerticalId
                    .toLongOrZero()
            crossSellItemRequestModel.transactionType =
                shipmentNewUpsellModel.upsell.transactionType
            listCrossSellItemRequest.add(crossSellItemRequestModel)
        }
        crossSellRequest.listItem = listCrossSellItemRequest

        val globalPromos = mutableListOf<Promo>()
        var hasPromoStackingData = false
        // Set promo global request data
        if (validateUsePromoRevampUiModel != null) {
            // Clear data first

            // Then set the data promo global
            val promoModel = validateUsePromoRevampUiModel.promoUiModel
            if (promoModel.codes.isNotEmpty() && promoModel.messageUiModel.state != "red") {
                for (promoCode in promoModel.codes) {
                    globalPromos.add(
                        Promo(
                            promoCode,
                            Promo.TYPE_GLOBAL
                        )
                    )
                }
            }
            hasPromoStackingData = true
        }
        return Carts().apply {
            promos = globalPromos
            isDonation =
                if (crossSellGroup?.crossSellList?.firstOrNullInstanceOf(CheckoutDonationModel::class.java)?.donation?.isChecked == true) 1 else 0
            egold = egoldData
            this.data = data
            tokopediaCorner = cornerData
            hasPromoStacking = hasPromoStackingData
            if (checkoutLeasingId.isNotEmpty()) {
                leasingId = checkoutLeasingId.toLongOrZero()
            }
            featureType = setCheckoutFeatureTypeData(data, cartType)
            crossSell = crossSellRequest
        }
    }

    private fun setCheckoutFeatureTypeData(dataCheckoutRequestList: List<Data>, cartType: String): Int {
        var hasTokoNowProduct = false
        loopall@ for (dataCheckoutRequest in dataCheckoutRequestList) {
            for (groupOrder in dataCheckoutRequest.groupOrders) {
                for (shopOrder in groupOrder.shopOrders) {
                    if (shopOrder.isTokoNow) {
                        hasTokoNowProduct = true
                        break@loopall
                    }
                }
            }
        }
        return if (cartType == CartShipmentAddressFormData.CART_TYPE_OCC) {
            if (hasTokoNowProduct) FEATURE_TYPE_OCC_MULTI_TOKONOW else FEATURE_TYPE_OCC_MULTI_NON_TOKONOW
        } else {
            if (hasTokoNowProduct) FEATURE_TYPE_TOKONOW_PRODUCT else FEATURE_TYPE_REGULAR_PRODUCT
        }
    }

    private fun setCheckoutRequestPromoData(
        data: List<Data>,
        validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel
    ) {
        for (dataCheckoutRequest in data) {
            for (groupOrder in dataCheckoutRequest.groupOrders) {
                var hasInsertLogisticPromoInGroupOrder = false
                for (shopOrder in groupOrder.shopOrders) {
                    // reset promo to prevent duplicate bo promo in owoc order
                    shopOrder.promos = emptyList()
                    for (voucherOrder in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
                        if (groupOrder.cartStringGroup == voucherOrder.cartStringGroup && shopOrder.cartStringOrder == voucherOrder.uniqueId) {
                            if (voucherOrder.code.isNotEmpty() && voucherOrder.type.isNotEmpty()) {
                                if (!hasInsertPromo(shopOrder.promos, voucherOrder.code)) {
                                    if (voucherOrder.isTypeLogistic() && hasInsertLogisticPromoInGroupOrder) {
                                        continue
                                    }
                                    if (voucherOrder.isTypeLogistic()) {
                                        hasInsertLogisticPromoInGroupOrder = true
                                    }
                                    val promoRequest = Promo()
                                    promoRequest.code = voucherOrder.code
                                    promoRequest.type = voucherOrder.type
                                    shopOrder.promos =
                                        shopOrder.promos.toMutableList().apply { add(promoRequest) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun hasInsertPromo(promoRequests: List<Promo>, promoCode: String): Boolean {
        for (promoRequest in promoRequests) {
            if (promoRequest.code == promoCode) {
                return true
            }
        }
        return false
    }

    private fun generateCheckoutParams(
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInDropOff: Boolean,
        deviceId: String,
        carts: Carts,
        dynamicData: String,
        fingerprintPublicKey: String?,
        payment: Payment,
        cartType: String
    ): CheckoutRequest {
        val atcBuyType = if (cartType == CartShipmentAddressFormData.CART_TYPE_OCC) AtcBuyType.INSTANT else AtcBuyType.ATC
        return CheckoutRequest(
            carts,
            isOneClickShipment.toString(),
            dynamicData,
            isTradeIn,
            isTradeIn && isTradeInDropOff,
            if (isTradeIn) deviceId else "",
            0,
            isThankyouNativeNew = true,
            isThankyouNative = true,
            isExpress = false,
            fingerprintSupport = (fingerprintPublicKey != null).toString(),
            fingerprintPublickey = fingerprintPublicKey ?: "",
            payment = payment,
            tracker = AppLogAnalytics.getEntranceInfoForCheckout(atcBuyType, carts.cartIds),
            consent = if (cartType == CartShipmentAddressFormData.CART_TYPE_OCC) 1 else 0
        )
    }

    private fun generatePayment(paymentWidgetData: PaymentWidgetData?): Payment {
        if (paymentWidgetData == null) {
            return Payment()
        }
        return Payment(gatewayCode = paymentWidgetData.gatewayCode)
    }

    private fun removeErrorShopProduct(
        listData: List<CheckoutItem>,
        recipientAddressModel: RecipientAddressModel,
        isTradeInDropOff: Boolean
    ): List<Data> {
        val newShipmentCartItemModelList: MutableList<CheckoutOrderModel> = ArrayList()
        var orderProducts = arrayListOf<CheckoutProductModel>()
        for (shipmentCartItemModel in listData) {
            if (shipmentCartItemModel is CheckoutProductModel && !shipmentCartItemModel.isError) {
                orderProducts.add(shipmentCartItemModel)
            }
            if (shipmentCartItemModel is CheckoutOrderModel) {
                if (shipmentCartItemModel.isAllItemError) {
                    orderProducts = arrayListOf()
                    continue
                }
                if (orderProducts.isEmpty()) {
                    orderProducts = arrayListOf()
                    continue
                }
                newShipmentCartItemModelList.add(shipmentCartItemModel.copy(finalCheckoutProducts = orderProducts))
                orderProducts = arrayListOf()
            }
        }
        return shipmentDataRequestConverter.createCheckoutRequestData(
            newShipmentCartItemModelList,
            recipientAddressModel,
            isTradeInDropOff
        )
    }

    private fun triggerCrossSellClickPilihPembayaran(listData: List<CheckoutItem>) {
        val shipmentCrossSellModelList: List<CheckoutCrossSellModel> = listData.crossSellGroup()!!.crossSellList.filterIsInstance(CheckoutCrossSellModel::class.java)
        var eventLabel = ""
        var digitalProductName = ""
        if (shipmentCrossSellModelList.isNotEmpty()) {
            for (i in shipmentCrossSellModelList.indices) {
                val crossSellModel = shipmentCrossSellModelList[i].crossSellModel
                val digitalCategoryName = crossSellModel.orderSummary.title
                eventLabel = "$digitalCategoryName - ${crossSellModel.id}"
                digitalProductName = crossSellModel.info.title
            }
        }
        val productList: MutableList<Any> = ArrayList()
        val orderList = listData.filterIsInstance(CheckoutOrderModel::class.java)
        for (order in orderList) {
            val orderProducts = helper.getOrderProducts(listData, order.cartStringGroup).filterIsInstance(CheckoutProductModel::class.java)
            for (cartItemModel in orderProducts) {
                productList.add(
                    DataLayer.mapOf(
                        ConstantTransactionAnalytics.Key.BRAND,
                        "",
                        ConstantTransactionAnalytics.Key.CATEGORY,
                        cartItemModel.analyticsProductCheckoutData.productCategoryId,
                        ConstantTransactionAnalytics.Key.ID,
                        "",
                        ConstantTransactionAnalytics.Key.NAME,
                        cartItemModel.analyticsProductCheckoutData.productName,
                        ConstantTransactionAnalytics.Key.PRICE,
                        cartItemModel.analyticsProductCheckoutData.productPrice,
                        ConstantTransactionAnalytics.Key.QUANTITY,
                        cartItemModel.analyticsProductCheckoutData.productQuantity,
                        ConstantTransactionAnalytics.Key.SHOP_ID,
                        cartItemModel.analyticsProductCheckoutData.productShopId,
                        ConstantTransactionAnalytics.Key.SHOP_NAME,
                        cartItemModel.analyticsProductCheckoutData.productShopName,
                        ConstantTransactionAnalytics.Key.SHOP_TYPE,
                        cartItemModel.analyticsProductCheckoutData.productShopType,
                        ConstantTransactionAnalytics.Key.VARIANT,
                        digitalProductName
                    )
                )
            }
        }
        checkoutAnalyticsCourierSelection.sendCrossSellClickPilihPembayaran(
            eventLabel,
            userSessionInterface.userId,
            productList
        )
    }

    fun checkProtectionAddOnOptIn(listProduct: List<CheckoutItem>): Boolean {
        return listProduct.any {
            it is CheckoutProductModel && it.addOnProduct.listAddOnProductData.any { addon ->
                addon.type == AddOnConstant.PRODUCT_PROTECTION_INSURANCE_TYPE &&
                    addon.status == AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK
            }
        }
    }
}

data class CheckoutResult(
    val success: Boolean,
    val checkoutData: CheckoutData?,
    val transactionId: String,
    val deviceModel: String,
    val devicePrice: Long,
    val diagnosticId: String,
    val hasClearPromoBeforeCheckout: Boolean,
    val throwable: Throwable?,
    val checkoutRequest: Carts
)
