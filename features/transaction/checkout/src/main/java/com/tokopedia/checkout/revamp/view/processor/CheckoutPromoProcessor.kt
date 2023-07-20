package com.tokopedia.checkout.revamp.view.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutItem
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.purchase_platform.common.analytics.PromoRevampAnalytics
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ProductDetailsItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CheckoutPromoProcessor @Inject constructor(
    private val clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase,
    private val validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase,
    private val dispatchers: CoroutineDispatchers
) {

    private var lastApplyData: LastApplyUiModel = LastApplyUiModel()
    private var lastValidateUseRequest: ValidateUsePromoRequest? = null
    private var bboPromoCodes: List<String> = emptyList()
    private var validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel? = null

    fun generateValidateUsePromoRequest(shipmentCartItemModelList: List<CheckoutItem>, isTradeIn: Boolean, isTradeInByDropOff: Boolean, isOneClickShipment: Boolean): ValidateUsePromoRequest {
        val bboPromoCodes = ArrayList<String>()
        var validateUsePromoRequest = lastValidateUseRequest
        return if (validateUsePromoRequest != null) {
            // Update param if have done param data generation before
            val list = shipmentCartItemModelList
            for (shipmentCartItemModel in list) {
                if (shipmentCartItemModel is CheckoutOrderModel) {
                    for (ordersItem in validateUsePromoRequest.orders) {
                        if (ordersItem.cartStringGroup == shipmentCartItemModel.cartStringGroup) {
                            for ((cartStringOrder, cartItemList) in shipmentCartItemModel.cartItemModelsGroupByOrder) {
                                if (ordersItem.uniqueId == cartStringOrder) {
                                    val productDetailsItems = ArrayList<ProductDetailsItem>()
                                    for (cartItemModel in cartItemList) {
                                        val productDetail = ProductDetailsItem()
                                        productDetail.productId = cartItemModel.productId
                                        productDetail.quantity = cartItemModel.quantity
                                        productDetail.bundleId =
                                            cartItemModel.bundleId.toLongOrZero()
                                        productDetailsItems.add(productDetail)
                                    }
                                    ordersItem.productDetails = productDetailsItems
                                    val listOrderCodes = ordersItem.codes
                                    // Add data BBO
//                                    if (shipmentCartItemModel.selectedShipmentDetailData != null && shipmentCartItemModel.voucherLogisticItemUiModel != null) {
//                                        if (!listOrderCodes.contains(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)) {
//                                            listOrderCodes.add(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
//                                        }
//                                        if (!bboPromoCodes.contains(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)) {
//                                            bboPromoCodes.add(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
//                                        }
//                                        ordersItem.boCode =
//                                            shipmentCartItemModel.voucherLogisticItemUiModel!!.code
//                                    } else {
//                                        ordersItem.boCode = ""
//                                    }
                                    ordersItem.codes = listOrderCodes
                                    setValidateUseSpIdParam(shipmentCartItemModel, ordersItem)
                                    break
                                }
                            }
                        }
                    }
                }
            }
            if (isTradeIn) {
                validateUsePromoRequest.isTradeIn = 1
                validateUsePromoRequest.isTradeInDropOff = if (isTradeInByDropOff) 1 else 0
            }
            if (isOneClickShipment) {
                validateUsePromoRequest.cartType = "ocs"
            } else {
                validateUsePromoRequest.cartType = "default"
            }
            lastValidateUseRequest = validateUsePromoRequest
            this.bboPromoCodes = bboPromoCodes
            validateUsePromoRequest
        } else {
            // First param data generation / initialization
            validateUsePromoRequest = ValidateUsePromoRequest()
            val listOrderItem = ArrayList<OrdersItem>()
            val list = shipmentCartItemModelList
            val lastApplyUiModel = lastApplyData
            for (shipmentCartItemModel in list) {
                if (shipmentCartItemModel is CheckoutOrderModel) {
                    for ((cartStringOrder, cartItemList) in shipmentCartItemModel.cartItemModelsGroupByOrder) {
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
                        for (lastApplyVoucherOrdersItemUiModel in lastApplyUiModel.voucherOrders) {
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
                        if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
                            if (!listOrderCodes.contains(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)) {
                                listOrderCodes.add(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                            }
                            if (!bboPromoCodes.contains(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)) {
                                bboPromoCodes.add(shipmentCartItemModel.voucherLogisticItemUiModel!!.code)
                            }
                            ordersItem.boCode =
                                shipmentCartItemModel.voucherLogisticItemUiModel!!.code
                        } else {
                            ordersItem.boCode = ""
                        }
                        ordersItem.codes = listOrderCodes
                        ordersItem.uniqueId = cartStringOrder
                        ordersItem.shopId = cartItemList.first().shopId.toLongOrZero()
                        ordersItem.boType = shipmentCartItemModel.boMetadata.boType
                        ordersItem.isPo = shipmentCartItemModel.isProductIsPreorder
                        ordersItem.poDuration =
                            shipmentCartItemModel.products[0].preOrderDurationDay
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
            lastValidateUseRequest = validateUsePromoRequest
            this.bboPromoCodes = bboPromoCodes
            validateUsePromoRequest
        }
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
                if (shipmentCartItemModel.shipment.courierItemData != null) {
                    ordersItem.shippingId =
                        shipmentCartItemModel.shipment.courierItemData.selectedShipper.shipperId
                    ordersItem.spId =
                        shipmentCartItemModel.shipment.courierItemData.selectedShipper.shipperProductId
                    if (shipmentCartItemModel.voucherLogisticItemUiModel != null) {
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
                } else {
                    ordersItem.shippingId = 0
                    ordersItem.spId = 0
                    ordersItem.freeShippingMetadata = ""
                    ordersItem.boCampaignId = 0
                    ordersItem.benefitClass = ""
                    ordersItem.shippingSubsidy = 0
                    ordersItem.shippingPrice = 0.0
                    ordersItem.etaText = ""
                    ordersItem.validationMetadata = ""
                }
//            }
        }
    }

    suspend fun validateUseLogisticPromo(
        validateUsePromoRequest: ValidateUsePromoRequest, cartString: String,
        promoCode: String,
        shipmentCartItemModelList: List<CheckoutItem>,
        courierItemData: CourierItemData
    ): List<CheckoutItem> {
        try {
            val validateUsePromoRevampUiModel = withContext(dispatchers.io) {
                setValidateUseBoCodeInOneOrderOwoc(validateUsePromoRequest)
                validateUsePromoRevampUseCase.setParam(validateUsePromoRequest)
                    .executeOnBackground()
            }
//            promoQueue.remove()
//            itemToProcess = promoQueue.peek()
//            if (promoQueue.any { it.cartString == shipmentValidatePromoHolderData.cartString && it.validateUsePromoRequest != null }) {
                // ignore this, because there is a new one in the queue
//                continue@loopProcess
//            }
            return onValidatePromoSuccess(validateUsePromoRevampUiModel, cartString, promoCode, shipmentCartItemModelList, courierItemData)
        } catch (t: Throwable) {
//            promoQueue.remove()
//            itemToProcess = promoQueue.peek()
//            if (promoQueue.any { it.cartString == shipmentValidatePromoHolderData.cartString }) {
                // ignore this, because there is a new one in the queue
//                continue@loopProcess
//            }
            return onValidatePromoError(t, cartString, promoCode, shipmentCartItemModelList)
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

    private fun onValidatePromoSuccess(
        validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel,
        cartString: String,
        promoCode: String,
        shipmentCartItemModelList: List<CheckoutItem>,
        courierItemData: CourierItemData
    ): List<CheckoutItem> {
//        if (view != null) {
//            this@ShipmentViewModel.validateUsePromoRevampUiModel =
//                validateUsePromoRevampUiModel
            val isValidatePromoRevampSuccess =
                validateUsePromoRevampUiModel.status.equals(
                    statusOK,
                    ignoreCase = true
                ) && validateUsePromoRevampUiModel.errorCode == statusCode200
            if (isValidatePromoRevampSuccess) {
//                view!!.updateButtonPromoCheckout(
//                    validateUsePromoRevampUiModel.promoUiModel,
//                    true
//                )
//                view!!.setStateLoadingCourierStateAtIndex(
//                    shipmentValidatePromoHolderData.cartPosition,
//                    false
//                )
                this.validateUsePromoRevampUiModel =
                    validateUsePromoRevampUiModel
//                updateTickerAnnouncementData(validateUsePromoRevampUiModel)
//                showErrorValidateUseIfAny(validateUsePromoRevampUiModel)
                return validateBBOWithSpecificOrder(
                    validateUsePromoRevampUiModel,
                    cartString,
                    promoCode,
                    shipmentCartItemModelList,
                    courierItemData
                )
            } else {
//                view!!.setStateLoadingCourierStateAtIndex(
//                    shipmentValidatePromoHolderData.cartPosition,
//                    false
//                )
                if (validateUsePromoRevampUiModel.message.isNotEmpty()) {
                    val errMessage =
                        validateUsePromoRevampUiModel.message[0]
//                    mTrackerShipment.eventClickLanjutkanTerapkanPromoError(
//                        errMessage
//                    )
                    PromoRevampAnalytics.eventCheckoutViewPromoMessage(errMessage)
//                    view!!.showToastError(errMessage)
//                    view!!.resetCourier(shipmentValidatePromoHolderData.cartPosition)
//                    view!!.getShipmentCartItemModel(shipmentValidatePromoHolderData.cartPosition)
//                        ?.let {
//                            if (it.boCode.isNotEmpty()) {
//                                clearCacheAutoApply(
//                                    it,
//                                    shipmentValidatePromoHolderData.promoCode,
//                                    it.boUniqueId
//                                )
//                                clearOrderPromoCodeFromLastValidateUseRequest(
//                                    shipmentValidatePromoHolderData.cartString,
//                                    shipmentValidatePromoHolderData.promoCode
//                                )
//                                it.boCode = ""
//                                it.boUniqueId = ""
//                            }
//                        }
                } else {
//                    view!!.showToastError(
//                        CheckoutConstant.DEFAULT_ERROR_MESSAGE_FAIL_APPLY_BBO
//                    )
//                    view!!.resetCourier(shipmentValidatePromoHolderData.cartPosition)
//                    view!!.getShipmentCartItemModel(shipmentValidatePromoHolderData.cartPosition)
//                        ?.let {
//                            if (it.boCode.isNotEmpty()) {
//                                clearCacheAutoApply(
//                                    it,
//                                    shipmentValidatePromoHolderData.promoCode,
//                                    it.boUniqueId
//                                )
//                                clearOrderPromoCodeFromLastValidateUseRequest(
//                                    shipmentValidatePromoHolderData.cartString,
//                                    shipmentValidatePromoHolderData.promoCode
//                                )
//                                it.boCode = ""
//                                it.boUniqueId = ""
//                            }
//                        }
                }
            }
//        }
//        val shipmentScheduleDeliveryMapData =
//            getScheduleDeliveryMapData(shipmentValidatePromoHolderData.cartString)
//        if (shipmentScheduleDeliveryMapData != null && shipmentScheduleDeliveryMapData.shouldStopInValidateUsePromo) {
//            shipmentScheduleDeliveryMapData.donePublisher.onCompleted()
//        }
        return shipmentCartItemModelList
    }

    private fun validateBBOWithSpecificOrder(
        validateUsePromoRevampUiModel: ValidateUsePromoRevampUiModel,
        cartString: String,
        promoCode: String,
        shipmentCartItemModelList: List<CheckoutItem>,
        courierItemData: CourierItemData
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
                            checkoutItems[item.index] = value.copy(shipment = value.shipment.copy(isLoading = false, courierItemData = courierItemData))
//                            if (view != null && recommendedCourier != null) {
//                                view!!.setSelectedCourier(
//                                    cartItemPosition,
//                                    recommendedCourier,
//                                    true,
//                                    false
//                                )
//                                processSaveShipmentState(shipmentCartItemModel)
//                            }
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
                        checkoutItems[item.index] = value.copy(shipment = value.shipment.copy(isLoading = false, courierItemData = null))
//                        if (view != null) {
//                            view!!.resetCourier(shipmentCartItemModel)
//                            view!!.logOnErrorApplyBo(
//                                MessageErrorException(
//                                    voucherOrder.messageUiModel.text
//                                ),
//                                shipmentCartItemModel,
//                                voucherOrder.code
//                            )
//                        }
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
                    checkoutItems[item.index] = value.copy(shipment = value.shipment.copy(isLoading = false, courierItemData = null))
//                    if (view != null) {
//                        view!!.resetCourier(shipmentCartItemModel)
//                        view!!.logOnErrorApplyBo(
//                            MessageErrorException("voucher order not found"),
//                            shipmentCartItemModel,
//                            promoCode
//                        )
//                        break
//                    }
                }
            }
        }
        return checkoutItems
    }

    private fun onValidatePromoError(
        t: Throwable,
        cartString: String,
        promoCode: String,
        shipmentCartItemModelList: List<CheckoutItem>
    ): List<CheckoutItem> {
        Timber.d(t)
        val checkoutItems = shipmentCartItemModelList.toMutableList()
        val orderIndex = checkoutItems.indexOfFirst { it is CheckoutOrderModel && it.cartStringGroup == cartString }
        val orderModel = checkoutItems[orderIndex] as CheckoutOrderModel
        var newOrderModel = orderModel.copy(shipment = orderModel.shipment.copy(isLoading = false))
//        if (view != null) {
//            view!!.setStateLoadingCourierStateAtIndex(
//                shipmentValidatePromoHolderData.cartPosition,
//                false
//            )
//            mTrackerShipment.eventClickLanjutkanTerapkanPromoError(t.message)
            if (t is AkamaiErrorException) {
//                clearAllPromo()
//                view!!.showToastError(t.message)
//                view!!.resetAllCourier()
//                view!!.doResetButtonPromoCheckout()
            } else {
                newOrderModel = newOrderModel.copy(shipment = newOrderModel.shipment.copy(courierItemData = null))
//                view!!.showToastError(t.message)
//                view!!.resetCourier(shipmentValidatePromoHolderData.cartPosition)
//                view!!.getShipmentCartItemModel(shipmentValidatePromoHolderData.cartPosition)
//                    ?.let {
//                        if (it.boCode.isNotEmpty()) {
//                            clearCacheAutoApply(
//                                it,
//                                shipmentValidatePromoHolderData.promoCode,
//                                it.boUniqueId
//                            )
//                            clearOrderPromoCodeFromLastValidateUseRequest(
//                                shipmentValidatePromoHolderData.cartString,
//                                shipmentValidatePromoHolderData.promoCode
//                            )
//                            it.boCode = ""
//                            it.boUniqueId = ""
//                        }
//                    }
            }
//            view!!.logOnErrorApplyBo(
//                t,
//                shipmentValidatePromoHolderData.cartPosition,
//                shipmentValidatePromoHolderData.promoCode
//            )
//            val shipmentScheduleDeliveryMapData =
//                getScheduleDeliveryMapData(shipmentValidatePromoHolderData.cartString)
//            if (shipmentScheduleDeliveryMapData != null && shipmentScheduleDeliveryMapData.shouldStopInValidateUsePromo) {
//                shipmentScheduleDeliveryMapData.donePublisher.onCompleted()
//            }
//        }
        checkoutItems[orderIndex] = newOrderModel
        return checkoutItems
    }

    companion object {
        private const val statusOK = "OK"

        private const val statusCode200 = "200"
    }
}
