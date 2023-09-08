package com.tokopedia.checkout.revamp.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.checkout.revamp.view.promo
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageToaster
import com.tokopedia.checkout.view.CheckoutLogger
import com.tokopedia.checkout.view.ShipmentViewModel
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrder
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoOrderData
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.Order
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.ProductDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.LastApplyUiMapper
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CheckoutPromoProcessor @Inject constructor(
    private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
    private val validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase,
    private val mTrackerShipment: CheckoutAnalyticsCourierSelection,
    private val toasterProcessor: CheckoutToasterProcessor,
    private val helper: CheckoutDataHelper,
    private val dispatchers: CoroutineDispatchers
) {

    var bboPromoCodes: List<String> = emptyList()
    internal var validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel? = null

    fun generateCouponListRecommendationRequest(
        listData: List<CheckoutItem>,
        isTradeIn: Boolean,
        isTradeInByDropOff: Boolean,
        isOneClickShipment: Boolean
    ): PromoRequest {
        val promoRequest = PromoRequest()
        val listOrderItem = ArrayList<Order>()
        val lastApplyUiModel = listData.promo()!!.promo
        for (shipmentCartItemModel in listData) {
            if (shipmentCartItemModel is CheckoutOrderModel) {
                val cartItemModelsGroupByOrder =
                    helper.getOrderProducts(listData, shipmentCartItemModel.cartStringGroup)
                        .filter { !it.isError }
                        .groupBy { it.cartStringOrder }
                for ((cartStringOrder, cartItemList) in cartItemModelsGroupByOrder) {
                    val ordersItem = Order()
                    val productDetailsItems = ArrayList<ProductDetail>()
                    for (cartItemModel in cartItemList) {
                        val productDetail = ProductDetail()
                        productDetail.productId = cartItemModel.productId
                        productDetail.quantity = cartItemModel.quantity
                        productDetail.bundleId = cartItemModel.bundleId.toLongOrZero()
                        productDetailsItems.add(productDetail)
                    }
                    ordersItem.product_details = productDetailsItems
                    ordersItem.isChecked = true
                    ordersItem.cartStringGroup = shipmentCartItemModel.cartStringGroup
                    ordersItem.uniqueId = cartStringOrder
                    val listCodes = ArrayList<String>()
                    for (voucher in lastApplyUiModel.voucherOrders) {
                        if (!voucher.isTypeLogistic() && voucher.cartStringGroup == ordersItem.cartStringGroup && voucher.uniqueId == ordersItem.uniqueId && !listCodes.contains(
                                voucher.code
                            )
                        ) {
                            listCodes.add(voucher.code)
                        }
                    }
                    val logPromoCode =
                        shipmentCartItemModel.shipment.courierItemData?.selectedShipper?.logPromoCode
                    if (!logPromoCode.isNullOrEmpty()) {
                        listCodes.add(logPromoCode)
                    }
                    ordersItem.codes = listCodes
                    ordersItem.shopId = cartItemList.first().shopId.toLongOrZero()
                    ordersItem.boType = shipmentCartItemModel.boMetadata.boType
                    ordersItem.isInsurancePrice = if (shipmentCartItemModel.isInsurance) 1 else 0
                    if (shipmentCartItemModel.shipment.courierItemData == null) {
                        ordersItem.shippingId = 0
                        ordersItem.spId = 0
                        ordersItem.freeShippingMetadata = ""
                        ordersItem.validationMetadata = ""
                    } else {
//                        if (isTradeInByDropOff) {
//                            if (shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff != null) {
//                                ordersItem.shippingId =
//                                    shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shipperId
//                                ordersItem.spId =
//                                    shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shipperProductId
//                                if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
//                                    ordersItem.freeShippingMetadata =
//                                        shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.freeShippingMetadata
//                                } else {
//                                    ordersItem.freeShippingMetadata = ""
//                                }
//                                ordersItem.validationMetadata =
//                                    shipmentCartItemModel.validationMetadata
//                            } else {
//                                ordersItem.shippingId = 0
//                                ordersItem.spId = 0
//                                ordersItem.freeShippingMetadata = ""
//                                ordersItem.validationMetadata = ""
//                            }
//                        } else {
//                            if (shipmentCartItemModel.shipment.courierItemData != null) {
                        ordersItem.shippingId =
                            shipmentCartItemModel.shipment.courierItemData.selectedShipper.shipperId
                        ordersItem.spId =
                            shipmentCartItemModel.shipment.courierItemData.selectedShipper.shipperProductId
                        if (!logPromoCode.isNullOrEmpty()) {
                            ordersItem.freeShippingMetadata =
                                shipmentCartItemModel.shipment.courierItemData.selectedShipper.freeShippingMetadata
                        } else {
                            ordersItem.freeShippingMetadata = ""
                        }
                        ordersItem.validationMetadata =
                            shipmentCartItemModel.validationMetadata
                    }
                    listOrderItem.add(ordersItem)
                }
            }
        }
        promoRequest.orders = listOrderItem
        promoRequest.state = CheckoutConstant.PARAM_CHECKOUT
        if (isOneClickShipment) {
            promoRequest.cartType = "ocs"
        } else {
            promoRequest.cartType = CartConstant.PARAM_DEFAULT
        }
        if (isTradeIn) {
            promoRequest.isTradeIn = 1
            promoRequest.isTradeInDropOff = if (isTradeInByDropOff) 1 else 0
        }
        val globalPromoCodes = ArrayList<String>()
        if (lastApplyUiModel.codes.isNotEmpty()) {
            globalPromoCodes.addAll(lastApplyUiModel.codes)
        }
        promoRequest.codes = globalPromoCodes
        return promoRequest
    }

    fun generateValidateUsePromoRequest(
        shipmentCartItemModelList: List<CheckoutItem>,
        isTradeIn: Boolean,
        isTradeInByDropOff: Boolean,
        isOneClickShipment: Boolean
    ): ValidateUsePromoRequest {
        val bboPromoCodes = ArrayList<String>()
        val validateUsePromoRequest = ValidateUsePromoRequest()
        val listOrderItem = ArrayList<OrdersItem>()
        val lastApplyUiModel = shipmentCartItemModelList.promo()!!.promo
        val voucherOrders = lastApplyUiModel.voucherOrders.filter { !it.isTypeLogistic() }
        for (shipmentCartItemModel in shipmentCartItemModelList) {
            if (shipmentCartItemModel is CheckoutOrderModel) {
                val cartItemModelsGroupByOrder =
                    helper.getOrderProducts(
                        shipmentCartItemModelList,
                        shipmentCartItemModel.cartStringGroup
                    )
                        .filter { !it.isError }
                        .groupBy { it.cartStringOrder }
                for ((cartStringOrder, cartItemList) in cartItemModelsGroupByOrder) {
                    val ordersItem = OrdersItem()
                    val productDetailsItems = ArrayList<ProductDetailsItem>()
                    for (cartItemModel in cartItemList) {
                        val productDetail = ProductDetailsItem()
                        productDetail.productId = cartItemModel.productId
                        productDetail.quantity = cartItemModel.quantity
                        productDetail.bundleId = cartItemModel.bundleId.toLongOrZero()
                        productDetailsItems.add(productDetail)
                    }
                    ordersItem.productDetails = productDetailsItems
                    val listOrderCodes = ArrayList<String>()
                    for (lastApplyVoucherOrdersItemUiModel in voucherOrders) {
                        if (cartStringOrder.equals(
                                lastApplyVoucherOrdersItemUiModel.uniqueId,
                                ignoreCase = true
                            )
                        ) {
                            if (!listOrderCodes.contains(lastApplyVoucherOrdersItemUiModel.code) && !lastApplyVoucherOrdersItemUiModel.isTypeLogistic()) {
                                listOrderCodes.add(lastApplyVoucherOrdersItemUiModel.code)
                            }
                        }
                    }
                    // Add data BBO
                    val logPromoCode =
                        shipmentCartItemModel.shipment.courierItemData?.selectedShipper?.logPromoCode
                    if (!logPromoCode.isNullOrEmpty()) {
                        if (!listOrderCodes.contains(logPromoCode)) {
                            listOrderCodes.add(logPromoCode)
                        }
                        if (!bboPromoCodes.contains(logPromoCode)) {
                            bboPromoCodes.add(logPromoCode)
                        }
                        ordersItem.boCode =
                            logPromoCode
                    } else {
                        ordersItem.boCode = ""
                    }
                    ordersItem.codes = listOrderCodes
                    ordersItem.uniqueId = cartStringOrder
                    ordersItem.shopId = cartItemList.first().shopId.toLongOrZero()
                    ordersItem.boType = shipmentCartItemModel.boMetadata.boType
                    ordersItem.isPo = shipmentCartItemModel.isProductIsPreorder
                    ordersItem.poDuration = shipmentCartItemModel.preOrderDurationDay
                    ordersItem.warehouseId = shipmentCartItemModel.fulfillmentId
                    ordersItem.cartStringGroup = shipmentCartItemModel.cartStringGroup
                    setValidateUseSpIdParam(shipmentCartItemModel, ordersItem)
                    listOrderItem.add(ordersItem)
                }
            }
        }
        validateUsePromoRequest.orders = listOrderItem
        validateUsePromoRequest.state = CheckoutConstant.PARAM_CHECKOUT
        validateUsePromoRequest.cartType = CartConstant.PARAM_DEFAULT
        validateUsePromoRequest.skipApply = 0
        if (isTradeIn) {
            validateUsePromoRequest.isTradeIn = 1
            validateUsePromoRequest.isTradeInDropOff = if (isTradeInByDropOff) 1 else 0
        }
        val globalPromoCodes = ArrayList<String>()
        if (lastApplyUiModel.codes.isNotEmpty()) {
            for (code in lastApplyUiModel.codes) {
                if (code.isNotEmpty() && !globalPromoCodes.contains(code)) {
                    globalPromoCodes.add(code)
                }
            }
        }
        validateUsePromoRequest.codes = globalPromoCodes
        if (isOneClickShipment) {
            validateUsePromoRequest.cartType = "ocs"
        } else {
            validateUsePromoRequest.cartType = "default"
        }
        this.bboPromoCodes = bboPromoCodes
        return validateUsePromoRequest
    }

    private fun setValidateUseSpIdParam(
        shipmentCartItemModel: CheckoutOrderModel,
        ordersItem: OrdersItem
    ) {
        if (shipmentCartItemModel.shipment.courierItemData == null) {
            ordersItem.shippingId = 0
            ordersItem.spId = 0
            ordersItem.freeShippingMetadata = ""
            ordersItem.boCampaignId = 0
            ordersItem.benefitClass = ""
            ordersItem.shippingSubsidy = 0
            ordersItem.shippingPrice = 0.0
            ordersItem.etaText = ""
            ordersItem.validationMetadata = ""
        } else {
//            if (isTradeInByDropOff) {
//                if (shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff != null) {
//                    ordersItem.shippingId =
//                        shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shipperId
//                    ordersItem.spId =
//                        shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shipperProductId
//                    if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
//                        ordersItem.freeShippingMetadata =
//                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.freeShippingMetadata
//                        ordersItem.benefitClass =
//                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.benefitClass
//                        ordersItem.shippingSubsidy =
//                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shippingSubsidy
//                        ordersItem.shippingPrice =
//                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.shippingRate.toDouble()
//                        ordersItem.etaText =
//                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.etaText.toEmptyStringIfNull()
//                        ordersItem.boCampaignId =
//                            shipmentCartItemModel.selectedShipmentDetailData!!.selectedCourierTradeInDropOff!!.boCampaignId
//                    } else {
//                        ordersItem.freeShippingMetadata = ""
//                        ordersItem.boCampaignId = 0
//                        ordersItem.benefitClass = ""
//                        ordersItem.shippingSubsidy = 0
//                        ordersItem.shippingPrice = 0.0
//                        ordersItem.etaText = ""
//                    }
//                    ordersItem.validationMetadata = shipmentCartItemModel.validationMetadata
//                } else {
//                    ordersItem.shippingId = 0
//                    ordersItem.spId = 0
//                    ordersItem.freeShippingMetadata = ""
//                    ordersItem.boCampaignId = 0
//                    ordersItem.benefitClass = ""
//                    ordersItem.shippingSubsidy = 0
//                    ordersItem.shippingPrice = 0.0
//                    ordersItem.etaText = ""
//                    ordersItem.validationMetadata = ""
//                }
//            } else {
            ordersItem.shippingId =
                shipmentCartItemModel.shipment.courierItemData.selectedShipper.shipperId
            ordersItem.spId =
                shipmentCartItemModel.shipment.courierItemData.selectedShipper.shipperProductId
            if (!shipmentCartItemModel.shipment.courierItemData.selectedShipper.logPromoCode.isNullOrEmpty()) {
                ordersItem.freeShippingMetadata =
                    shipmentCartItemModel.shipment.courierItemData.selectedShipper.freeShippingMetadata
                ordersItem.benefitClass =
                    shipmentCartItemModel.shipment.courierItemData.selectedShipper.benefitClass
                ordersItem.shippingSubsidy =
                    shipmentCartItemModel.shipment.courierItemData.selectedShipper.shippingSubsidy
                ordersItem.shippingPrice =
                    shipmentCartItemModel.shipment.courierItemData.selectedShipper.shippingRate.toDouble()
                ordersItem.etaText =
                    shipmentCartItemModel.shipment.courierItemData.selectedShipper.etaText.toEmptyStringIfNull()
                ordersItem.boCampaignId =
                    shipmentCartItemModel.shipment.courierItemData.selectedShipper.boCampaignId
            } else {
                ordersItem.freeShippingMetadata = ""
                ordersItem.boCampaignId = 0
                ordersItem.benefitClass = ""
                ordersItem.shippingSubsidy = 0
                ordersItem.shippingPrice = 0.0
                ordersItem.etaText = ""
            }
            ordersItem.validationMetadata = shipmentCartItemModel.validationMetadata
        }
    }

    suspend fun validateUseLogisticPromo(
        validateUsePromoRequest: ValidateUsePromoRequest,
        cartString: String,
        promoCode: String,
        shipmentCartItemModelList: List<CheckoutItem>,
        courierItemData: CourierItemData,
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInByDropOff: Boolean
    ): List<CheckoutItem> {
        try {
            val validateUsePromoRevampUiModel = withContext(dispatchers.io) {
                setValidateUseBoCodeInOneOrderOwoc(validateUsePromoRequest)
                validateUsePromoRevampUseCase.setParam(validateUsePromoRequest)
                    .executeOnBackground()
            }
            return onValidatePromoSuccess(
                validateUsePromoRevampUiModel,
                cartString,
                promoCode,
                shipmentCartItemModelList,
                courierItemData,
                isOneClickShipment,
                isTradeIn,
                isTradeInByDropOff
            )
        } catch (t: Throwable) {
            return onValidatePromoError(
                t,
                cartString,
                promoCode,
                shipmentCartItemModelList,
                validateUsePromoRequest,
                isOneClickShipment,
                isTradeIn,
                isTradeInByDropOff
            )
        }
    }

    private fun setValidateUseBoCodeInOneOrderOwoc(validateUsePromoRequest: ValidateUsePromoRequest) {
        validateUsePromoRequest.orders.filter { it.cartStringGroup != it.uniqueId }
            .groupBy { it.cartStringGroup }.filterValues { it.size > 1 }.values
            .forEach { ordersItems ->
                val boCode = ordersItems.first().boCode
                if (boCode.isNotEmpty()) {
                    ordersItems.forEachIndexed { index, ordersItem ->
                        if (index > 0) {
                            ordersItem.codes.remove(boCode)
                        }
                    }
                }
            }
    }

    private suspend fun onValidatePromoSuccess(
        validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel,
        cartString: String,
        promoCode: String,
        shipmentCartItemModelList: List<CheckoutItem>,
        courierItemData: CourierItemData,
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInByDropOff: Boolean
    ): List<CheckoutItem> {
        val isValidatePromoRevampSuccess =
            validateUsePromoRevampUiModel.status.equals(
                statusOK,
                ignoreCase = true
            ) && validateUsePromoRevampUiModel.errorCode == statusCode200
        if (isValidatePromoRevampSuccess) {
            this.validateUsePromoRevampUiModel =
                validateUsePromoRevampUiModel
            val checkoutItems = shipmentCartItemModelList.toMutableList()
            val promo = checkoutItems.promo()!!.copy(
                promo = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(
                    validateUsePromoRevampUiModel.promoUiModel
                )
            )
            checkoutItems[checkoutItems.size - 4] = promo
            showErrorValidateUseIfAny(validateUsePromoRevampUiModel)
            return validateBBOWithSpecificOrder(
                validateUsePromoRevampUiModel,
                cartString,
                promoCode,
                checkoutItems,
                courierItemData,
                isOneClickShipment,
                isTradeIn,
                isTradeInByDropOff
            )
        } else {
            var errMessage =
                validateUsePromoRevampUiModel.message.firstOrNull()
            if (errMessage != null) {
                mTrackerShipment.eventClickLanjutkanTerapkanPromoError(
                    errMessage
                )
                PromoRevampAnalytics.eventCheckoutViewPromoMessage(errMessage)
            } else {
                errMessage = CheckoutConstant.DEFAULT_ERROR_MESSAGE_FAIL_APPLY_BBO
            }
            toasterProcessor.commonToaster.emit(CheckoutPageToaster(Toaster.TYPE_ERROR, errMessage))
            val checkoutItems = shipmentCartItemModelList.toMutableList()
            for ((index, checkoutItem) in checkoutItems.withIndex()) {
                if (checkoutItem is CheckoutOrderModel && checkoutItem.cartStringGroup == cartString) {
                    if (checkoutItem.boCode.isNotEmpty()) {
                        doClearBoSilently(checkoutItem)
                    }
                    checkoutItems[index] = checkoutItem.copy(
                        shipment = checkoutItem.shipment.copy(
                            isLoading = false,
                            courierItemData = null
                        ),
                        boCode = "",
                        boUniqueId = ""
                    )
                }
            }
            return checkoutItems
        }
    }

    private suspend fun doClearBoSilently(checkoutItem: CheckoutOrderModel) {
        try {
            clearCacheAutoApplyStackUseCase.setParams(
                ClearPromoRequest(
                    ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                    false,
                    ClearPromoOrderData(
                        emptyList(),
                        arrayListOf(
                            ClearPromoOrder(
                                checkoutItem.boUniqueId,
                                checkoutItem.boMetadata.boType,
                                arrayListOf(checkoutItem.boCode),
                                checkoutItem.shopId,
                                checkoutItem.isProductIsPreorder,
                                checkoutItem.preOrderDurationDay.toString(),
                                checkoutItem.fulfillmentId,
                                checkoutItem.cartStringGroup
                            )
                        )
                    )
                )
            ).executeOnBackground()
        } catch (t: Throwable) {
            Timber.d(t)
        }
    }

    private fun validateBBOWithSpecificOrder(
        validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel,
        cartString: String,
        promoCode: String,
        shipmentCartItemModelList: List<CheckoutItem>,
        courierItemData: CourierItemData,
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInByDropOff: Boolean
    ): List<CheckoutItem> {
        val checkoutItems = shipmentCartItemModelList.toMutableList()
        var orderFound = false
        for (voucherOrder in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
            if (voucherOrder.cartStringGroup == cartString && voucherOrder.code.equals(
                    promoCode,
                    ignoreCase = true
                )
            ) {
                orderFound = true
                if (voucherOrder.messageUiModel.state != "red") {
                    for (item in shipmentCartItemModelList.withIndex()) {
                        val value = item.value
                        if (value is CheckoutOrderModel && value.cartStringGroup == voucherOrder.cartStringGroup) {
                            checkoutItems[item.index] = value.copy(
                                shipment = value.shipment.copy(
                                    isLoading = false,
                                    courierItemData = courierItemData
                                ),
                                boUniqueId = voucherOrder.uniqueId,
                                isShippingBorderRed = false
                            )
                        }
                    }
                }
            }
            if (voucherOrder.type.equals(
                    "logistic",
                    ignoreCase = true
                ) && voucherOrder.messageUiModel.state.equals("red", ignoreCase = true)
            ) {
                for (item in shipmentCartItemModelList.withIndex()) {
                    val value = item.value
                    if (value is CheckoutOrderModel && value.cartStringGroup == voucherOrder.cartStringGroup) {
                        checkoutItems[item.index] = value.copy(
                            shipment = value.shipment.copy(
                                isLoading = false,
                                courierItemData = null
                            )
                        )
                        CheckoutLogger.logOnErrorApplyBoNew(
                            MessageErrorException(voucherOrder.messageUiModel.text),
                            value,
                            isOneClickShipment,
                            isTradeIn,
                            isTradeInByDropOff,
                            promoCode
                        )
                    }
                }
            }
        }
        if (!orderFound) {
            // if not voucher order found for attempted apply BO order,
            // then should reset courier and not apply the BO
            // this should be a rare case
            for (item in shipmentCartItemModelList.withIndex()) {
                val value = item.value
                if (value is CheckoutOrderModel && value.cartStringGroup == cartString) {
                    checkoutItems[item.index] = value.copy(
                        shipment = value.shipment.copy(
                            isLoading = false,
                            courierItemData = null
                        )
                    )
                    CheckoutLogger.logOnErrorApplyBoNew(
                        MessageErrorException("voucher order not found"),
                        value,
                        isOneClickShipment,
                        isTradeIn,
                        isTradeInByDropOff,
                        promoCode
                    )
                }
            }
        }
        return checkoutItems
    }

    private suspend fun onValidatePromoError(
        t: Throwable,
        cartString: String,
        promoCode: String,
        shipmentCartItemModelList: List<CheckoutItem>,
        validateUsePromoRequest: ValidateUsePromoRequest,
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInByDropOff: Boolean
    ): List<CheckoutItem> {
        Timber.d(t)
        val checkoutItems = shipmentCartItemModelList.toMutableList()
        val orderIndex =
            checkoutItems.indexOfFirst { it is CheckoutOrderModel && it.cartStringGroup == cartString }
        val orderModel = checkoutItems[orderIndex] as CheckoutOrderModel
        var newOrderModel = orderModel.copy(
            shipment = orderModel.shipment.copy(
                isLoading = false,
                courierItemData = null
            ),
            isShippingBorderRed = false
        )
        mTrackerShipment.eventClickLanjutkanTerapkanPromoError(t.message)
        if (t is AkamaiErrorException) {
            clearAllPromo(validateUsePromoRequest)
            for ((index, checkoutItem) in checkoutItems.withIndex()) {
                if (checkoutItem is CheckoutOrderModel && !checkoutItem.shipment.courierItemData?.selectedShipper?.logPromoCode.isNullOrEmpty()) {
                    checkoutItems[index] =
                        checkoutItem.copy(shipment = checkoutItem.shipment.copy(courierItemData = null))
                }
            }
            checkoutItems[checkoutItems.size - 4] =
                checkoutItems.promo()!!.copy(promo = LastApplyUiModel())
            toasterProcessor.commonToaster.emit(
                CheckoutPageToaster(
                    Toaster.TYPE_ERROR,
                    t.message ?: "",
                    t
                )
            )
        } else {
            if (orderModel.boCode.isNotEmpty()) {
                doClearBoSilently(orderModel)
                newOrderModel = newOrderModel.copy(boCode = "", boUniqueId = "")
            }
            toasterProcessor.commonToaster.emit(
                CheckoutPageToaster(
                    Toaster.TYPE_ERROR,
                    t.message ?: "",
                    t
                )
            )
        }
        CheckoutLogger.logOnErrorApplyBoNew(
            t,
            orderModel,
            isOneClickShipment,
            isTradeIn,
            isTradeInByDropOff,
            promoCode
        )
        checkoutItems[orderIndex] = newOrderModel
        return checkoutItems
    }

    suspend fun clearAllBo(checkoutItems: List<CheckoutItem>) {
        val clearOrders = ArrayList<ClearPromoOrder>()
        var hasBo = false
        for (shipmentCartItemModel in checkoutItems) {
            if (shipmentCartItemModel is CheckoutOrderModel && shipmentCartItemModel.shipment.courierItemData != null && !shipmentCartItemModel.shipment.courierItemData.selectedShipper.logPromoCode.isNullOrEmpty()) {
                clearOrders.add(
                    ClearPromoOrder(
                        shipmentCartItemModel.boUniqueId,
                        shipmentCartItemModel.boMetadata.boType,
                        arrayListOf(shipmentCartItemModel.shipment.courierItemData.selectedShipper.logPromoCode!!),
                        shipmentCartItemModel.shopId,
                        shipmentCartItemModel.isProductIsPreorder,
                        shipmentCartItemModel.preOrderDurationDay.toString(),
                        shipmentCartItemModel.fulfillmentId,
                        shipmentCartItemModel.cartStringGroup
                    )
                )
                hasBo = true
            }
        }
        if (hasBo) {
            withContext(dispatchers.io) {
                try {
                    clearCacheAutoApplyStackUseCase.setParams(
                        ClearPromoRequest(
                            ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                            false,
                            ClearPromoOrderData(
                                ArrayList(),
                                clearOrders
                            )
                        )
                    ).executeOnBackground()
                } catch (t: Throwable) {
                    Timber.d(t)
                }
            }
        }
    }

    suspend fun clearPromo(clearPromoOrder: ClearPromoOrder): Boolean {
        return withContext(dispatchers.io) {
            try {
                val responseData = clearCacheAutoApplyStackUseCase.setParams(
                    ClearPromoRequest(
                        ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                        false,
                        ClearPromoOrderData(
                            emptyList(),
                            arrayListOf(clearPromoOrder)
                        )
                    )
                ).executeOnBackground()
                return@withContext onSuccessClearPromo(responseData, clearPromoOrder.codes.first())
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext false
            }
        }
    }

    private fun onSuccessClearPromo(
        responseData: ClearPromoUiModel,
        promoCode: String
    ): Boolean {
        val isLastAppliedPromo =
            isLastAppliedPromo(promoCode)
        if (isLastAppliedPromo) {
            validateUsePromoRevampUiModel = null
        }
        return isLastAppliedPromo
    }

    private fun isLastAppliedPromo(promoCode: String?): Boolean {
        if (validateUsePromoRevampUiModel != null) {
            val voucherOrders: List<PromoCheckoutVoucherOrdersItemUiModel> =
                validateUsePromoRevampUiModel!!.promoUiModel.voucherOrderUiModels
            if (voucherOrders.isNotEmpty()) {
                for (voucherOrder in voucherOrders) {
                    if (voucherOrder.code != promoCode) return false
                }
            }
            val codes: List<String> = validateUsePromoRevampUiModel!!.promoUiModel.codes
            if (codes.isNotEmpty()) {
                for (code in codes) {
                    if (code != promoCode) return false
                }
            }
        }
        return true
    }

    private fun getBBOCount(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel): Int {
        var bboCount = 0
        val cartStrings: MutableSet<String> = mutableSetOf()
        for (voucherOrder in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
            if (voucherOrder.type.equals("logistic", ignoreCase = true) && !cartStrings.contains(voucherOrder.cartStringGroup)) {
                cartStrings.add(voucherOrder.cartStringGroup)
                bboCount++
            }
        }
        return bboCount
    }

    private suspend fun showErrorValidateUseIfAny(validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel) {
        val bboCount = getBBOCount(validateUsePromoRevampUiModel)
        if (bboCount == 1) {
            for (voucherOrder in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
                if (voucherOrder.type.equals(
                        "logistic",
                        ignoreCase = true
                    ) && voucherOrder.messageUiModel.state.equals(
                            "red",
                            ignoreCase = true
                        )
                ) {
                    toasterProcessor.commonToaster.emit(
                        CheckoutPageToaster(
                            Toaster.TYPE_ERROR,
                            voucherOrder.messageUiModel.text
                        )
                    )
                    return
                }
            }
        }
        val messageInfo =
            validateUsePromoRevampUiModel.promoUiModel.additionalInfoUiModel.errorDetailUiModel.message
        if (messageInfo.isNotEmpty()) {
            toasterProcessor.commonToaster.emit(
                CheckoutPageToaster(
                    Toaster.TYPE_NORMAL,
                    messageInfo
                )
            )
        }
    }

    suspend fun validateUse(
        validateUsePromoRequest: ValidateUsePromoRequest,
        checkoutItems: List<CheckoutItem>,
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInByDropOff: Boolean
    ): List<CheckoutItem> {
        var items = checkoutItems.toMutableList()
        return withContext(dispatchers.io) {
            try {
                val validateUsePromoRevampUiModel = withContext(dispatchers.io) {
                    setValidateUseBoCodeInOneOrderOwoc(validateUsePromoRequest)
                    validateUsePromoRevampUseCase.setParam(validateUsePromoRequest)
                        .executeOnBackground()
                }
                this@CheckoutPromoProcessor.validateUsePromoRevampUiModel =
                    validateUsePromoRevampUiModel
                showErrorValidateUseIfAny(validateUsePromoRevampUiModel)
                items = validateBBO(
                    validateUsePromoRevampUiModel,
                    checkoutItems,
                    isOneClickShipment,
                    isTradeIn,
                    isTradeInByDropOff
                ).toMutableList()
                if (!validateUsePromoRevampUiModel.status.equals(
                        ShipmentViewModel.statusOK,
                        ignoreCase = true
                    ) || validateUsePromoRevampUiModel.errorCode != ShipmentViewModel.statusCode200
                ) {
                    val message: String =
                        if (validateUsePromoRevampUiModel.message.isNotEmpty()) {
                            validateUsePromoRevampUiModel.message[0]
                        } else {
                            CheckoutConstant.DEFAULT_ERROR_MESSAGE_VALIDATE_PROMO
                        }
                    val newPromo = items.promo()!!.copy(
                        promo = LastApplyUiModel()
                    )
                    items[items.size - 4] = newPromo
                    for ((index, checkoutItem) in items.withIndex()) {
                        if (checkoutItem is CheckoutOrderModel) {
                            items[index] = checkoutItem.copy(
                                shipment = checkoutItem.shipment.copy(
                                    isLoading = false,
                                    courierItemData = null
                                )
                            )
                        }
                    }
                    toasterProcessor.commonToaster.emit(
                        CheckoutPageToaster(
                            Toaster.TYPE_ERROR,
                            message
                        )
                    )
                } else {
                    val newPromo = items.promo()!!.copy(
                        promo = LastApplyUiMapper.mapValidateUsePromoUiModelToLastApplyUiModel(
                            validateUsePromoRevampUiModel.promoUiModel
                        )
                    )
                    items[items.size - 4] = newPromo
                    if (validateUsePromoRevampUiModel.promoUiModel.messageUiModel.state == "red") {
                        mTrackerShipment.eventViewPromoAfterAdjustItem(validateUsePromoRevampUiModel.promoUiModel.messageUiModel.text)
                    } else {
                        for (voucherOrder in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
                            if (voucherOrder.messageUiModel.state == "red") {
                                mTrackerShipment.eventViewPromoAfterAdjustItem(voucherOrder.messageUiModel.text)
                                break
                            }
                        }
                    }
                }
                return@withContext items
            } catch (t: Throwable) {
                Timber.d(t)
                if (t is AkamaiErrorException) {
                    clearAllPromo(validateUsePromoRequest)
                    items[items.size - 4] = items.promo()!!.copy(promo = LastApplyUiModel())
                    for ((index, checkoutItem) in items.withIndex()) {
                        if (checkoutItem is CheckoutOrderModel) {
                            items[index] = checkoutItem.copy(
                                shipment = checkoutItem.shipment.copy(
                                    isLoading = false,
                                    courierItemData = null
                                )
                            )
                        }
                    }
                    toasterProcessor.commonToaster.emit(
                        CheckoutPageToaster(
                            Toaster.TYPE_ERROR,
                            t.message ?: "",
                            t
                        )
                    )
                } else {
                    items[items.size - 4] = items.promo()!!.copy(promo = LastApplyUiModel())
                    for ((index, checkoutItem) in items.withIndex()) {
                        if (checkoutItem is CheckoutOrderModel) {
                            items[index] = checkoutItem.copy(
                                shipment = checkoutItem.shipment.copy(
                                    isLoading = false,
                                    courierItemData = null
                                )
                            )
                        }
                    }
                    toasterProcessor.commonToaster.emit(
                        CheckoutPageToaster(
                            Toaster.TYPE_ERROR,
                            throwable = t
                        )
                    )
                }
                return@withContext items
            }
        }
    }

    private fun validateBBO(
        validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel,
        checkoutItems: List<CheckoutItem>,
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInByDropOff: Boolean
    ): List<CheckoutItem> {
        val newCheckoutItems = checkoutItems.toMutableList()
        val updatedCartStringGroup: ArrayList<String> = arrayListOf()
        voucherLoop@ for (voucherOrder in validateUsePromoRevampUiModel.promoUiModel.voucherOrderUiModels) {
            if (voucherOrder.type.equals(
                    "logistic",
                    ignoreCase = true
                ) && voucherOrder.messageUiModel.state.equals(
                        "red",
                        ignoreCase = true
                    )
            ) {
                for ((index, shipmentCartItemModel) in checkoutItems.withIndex()) {
                    if (shipmentCartItemModel is CheckoutOrderModel && shipmentCartItemModel.cartStringGroup == voucherOrder.cartStringGroup) {
                        updatedCartStringGroup.add(voucherOrder.cartStringGroup)
                        newCheckoutItems[index] = shipmentCartItemModel.copy(
                            shipment = shipmentCartItemModel.shipment.copy(
                                isLoading = false,
                                courierItemData = null
                            )
                        )
                        CheckoutLogger.logOnErrorApplyBoNew(
                            MessageErrorException(voucherOrder.messageUiModel.text),
                            shipmentCartItemModel,
                            isOneClickShipment,
                            isTradeIn,
                            isTradeInByDropOff,
                            voucherOrder.code
                        )
                        break@voucherLoop
                    }
                }
            } else if (voucherOrder.type.equals(
                    "logistic",
                    ignoreCase = true
                ) && voucherOrder.messageUiModel.state.equals(
                        "green",
                        ignoreCase = true
                    )
            ) {
                updatedCartStringGroup.add(voucherOrder.cartStringGroup)
            }
        }
        // if not voucher order found for attempted apply BO order,
        // then should reset courier and not apply the BO
        // this should be a rare case
        for ((index, shipmentCartItemModel) in checkoutItems.withIndex()) {
            if (shipmentCartItemModel is CheckoutOrderModel) {
                val code = shipmentCartItemModel.shipment.courierItemData?.selectedShipper?.logPromoCode
                if (!code.isNullOrEmpty() && !updatedCartStringGroup.contains(shipmentCartItemModel.cartStringGroup)) {
                    newCheckoutItems[index] = shipmentCartItemModel.copy(
                        shipment = shipmentCartItemModel.shipment.copy(
                            isLoading = false,
                            courierItemData = null
                        )
                    )
                    CheckoutLogger.logOnErrorApplyBoNew(
                        MessageErrorException("voucher order not found"),
                        shipmentCartItemModel,
                        isOneClickShipment,
                        isTradeIn,
                        isTradeInByDropOff,
                        code
                    )
                }
            }
        }
        return newCheckoutItems
    }

    suspend fun finalValidateUse(
        validateUsePromoRequest: ValidateUsePromoRequest
    ): ValidateUsePromoRevampUiModel? {
        return try {
            val validateUsePromoRevampUiModel = withContext(dispatchers.io) {
                setValidateUseBoCodeInOneOrderOwoc(validateUsePromoRequest)
                validateUsePromoRevampUseCase.setParam(validateUsePromoRequest)
                    .executeOnBackground()
            }
            validateUsePromoRevampUiModel
        } catch (t: Throwable) {
            Timber.d(t)
            null
        }
    }

    suspend fun cancelNotEligiblePromo(
        notEligiblePromoHolderdataList: ArrayList<NotEligiblePromoHolderdata>,
        listData: List<CheckoutItem>
    ): Boolean {
        var hasPromo = false
        val globalPromoCodes = arrayListOf<String>()
        val clearOrders = arrayListOf<ClearPromoOrder>()
        for (notEligiblePromo in notEligiblePromoHolderdataList) {
            if (notEligiblePromo.iconType == NotEligiblePromoHolderdata.TYPE_ICON_GLOBAL) {
                globalPromoCodes.add(notEligiblePromo.promoCode)
                hasPromo = true
            } else {
                val clearOrder =
                    getClearPromoOrderByUniqueId(clearOrders, notEligiblePromo.uniqueId)
                if (clearOrder != null) {
                    clearOrder.codes.add(notEligiblePromo.promoCode)
                    hasPromo = true
                } else if (listData.isNotEmpty()) {
                    for (shipmentCartItemModel in listData) {
                        if (shipmentCartItemModel is CheckoutOrderModel && shipmentCartItemModel.cartStringGroup == notEligiblePromo.cartStringGroup) {
                            val codes = arrayListOf<String>()
                            codes.add(notEligiblePromo.promoCode)
                            clearOrders.add(
                                ClearPromoOrder(
                                    notEligiblePromo.uniqueId,
                                    shipmentCartItemModel.boMetadata.boType,
                                    codes,
                                    shipmentCartItemModel.shopId,
                                    shipmentCartItemModel.isProductIsPreorder,
                                    shipmentCartItemModel.preOrderDurationDay.toString(),
                                    shipmentCartItemModel.fulfillmentId,
                                    shipmentCartItemModel.cartStringGroup
                                )
                            )
                            hasPromo = true
                            break
                        }
                    }
                }
            }
        }
        if (hasPromo) {
            try {
                val response = clearCacheAutoApplyStackUseCase.setParams(
                    ClearPromoRequest(
                        ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
                        false,
                        ClearPromoOrderData(globalPromoCodes, clearOrders)
                    )
                ).executeOnBackground()
                if (response.successDataModel.success) {
                    return true
                }
            } catch (t: Throwable) {
                Timber.d(t)
                return false
            }
        }
        return true
    }

    private fun getClearPromoOrderByUniqueId(
        list: ArrayList<ClearPromoOrder>,
        uniqueId: String
    ): ClearPromoOrder? {
        for (clearPromoOrder in list) {
            if (clearPromoOrder.uniqueId == uniqueId) {
                return clearPromoOrder
            }
        }
        return null
    }

    private suspend fun clearAllPromo(validateUsePromoRequest: ValidateUsePromoRequest) {
        var hasPromo = false
        val globalCodes = ArrayList<String>()
        for (code in validateUsePromoRequest.codes) {
            globalCodes.add(code)
            hasPromo = true
        }
        validateUsePromoRequest.codes = ArrayList()
        val cloneOrders = ArrayList<OrdersItem>()
        val clearOrders = ArrayList<ClearPromoOrder>()
        for (order in validateUsePromoRequest.orders) {
            clearOrders.add(
                ClearPromoOrder(
                    order.uniqueId,
                    order.boType,
                    order.codes,
                    order.shopId,
                    order.isPo,
                    order.poDuration.toString(),
                    order.warehouseId,
                    order.cartStringGroup
                )
            )
            if (order.codes.isNotEmpty()) {
                hasPromo = true
            }
            order.codes = ArrayList()
            order.boCode = ""
            cloneOrders.add(order)
        }
        validateUsePromoRequest.orders = cloneOrders
        val params = ClearPromoRequest(
            ClearCacheAutoApplyStackUseCase.PARAM_VALUE_MARKETPLACE,
            false,
            ClearPromoOrderData(globalCodes, clearOrders)
        )
        if (hasPromo) {
            withContext(dispatchers.io) {
                try {
                    clearCacheAutoApplyStackUseCase.setParams(params).executeOnBackground()
                } catch (t: Throwable) {
                    Timber.d(t)
                }
            }
        }
        validateUsePromoRevampUiModel = null
    }

    companion object {
        private const val statusOK = "OK"

        private const val statusCode200 = "200"
    }
}
