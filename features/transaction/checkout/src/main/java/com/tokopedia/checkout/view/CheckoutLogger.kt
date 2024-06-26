package com.tokopedia.checkout.view

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.constant.LoggerConstant
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException

object CheckoutLogger {

    fun logOnErrorLoadCheckoutPage(
        throwable: Throwable,
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInByDropOff: Boolean
    ) {
        if (shouldTriggerLog(throwable)) {
            val errorMessage = throwable.message ?: "unknown exception"
            val mapData = mapOf(
                LoggerConstant.Key.ERROR_TYPE to LoggerConstant.Type.LOAD_CHECKOUT_PAGE_ERROR,
                LoggerConstant.Key.IS_OCS to isOneClickShipment.toString(),
                LoggerConstant.Key.IS_TRADE_IN to isTradeIn.toString(),
                LoggerConstant.Key.IS_TRADE_IN_INDOPAKET to isTradeInByDropOff.toString(),
                LoggerConstant.Key.MESSAGE to errorMessage,
                LoggerConstant.Key.STACK_TRACE to throwable.stackTraceToString()
            )
            ServerLogger.log(
                Priority.P2,
                LoggerConstant.Tag.P2_BUYER_FLOW_CART,
                mapData
            )
        }
    }

    fun logOnErrorLoadCourier(
        throwable: Throwable,
        shipmentCartItemModel: ShipmentCartItemModel,
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInByDropOff: Boolean,
        promoCode: String
    ) {
        if (shouldTriggerLog(throwable)) {
            val productIds = mutableListOf<String>()
            for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                productIds.add(cartItemModel.productId.toString())
            }

            val errorMessage = throwable.message ?: "unknown exception"
            val mapData = mapOf(
                LoggerConstant.Key.ERROR_TYPE to LoggerConstant.Type.LOAD_COURIER_ERROR,
                LoggerConstant.Key.IS_OCS to isOneClickShipment.toString(),
                LoggerConstant.Key.IS_TRADE_IN to isTradeIn.toString(),
                LoggerConstant.Key.IS_TRADE_IN_INDOPAKET to isTradeInByDropOff.toString(),
                LoggerConstant.Key.PRODUCT_ID_LIST to productIds.toString(),
                LoggerConstant.Key.MESSAGE to errorMessage,
                LoggerConstant.Key.PROMO_CODE to promoCode,
                LoggerConstant.Key.STACK_TRACE to throwable.stackTraceToString()
            )
            ServerLogger.log(
                Priority.P2,
                LoggerConstant.Tag.P2_BUYER_FLOW_CART,
                mapData
            )
        }
    }

    fun logOnErrorLoadCourierNew(
        throwable: Throwable,
        shipmentCartItemModel: CheckoutOrderModel,
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInByDropOff: Boolean,
        promoCode: String
    ) {
        if (shouldTriggerLog(throwable)) {
            val productIds = shipmentCartItemModel.orderProductIds

            val errorMessage = throwable.message ?: "unknown exception"
            val mapData = mapOf(
                LoggerConstant.Key.ERROR_TYPE to LoggerConstant.Type.LOAD_COURIER_ERROR,
                LoggerConstant.Key.IS_OCS to isOneClickShipment.toString(),
                LoggerConstant.Key.IS_TRADE_IN to isTradeIn.toString(),
                LoggerConstant.Key.IS_TRADE_IN_INDOPAKET to isTradeInByDropOff.toString(),
                LoggerConstant.Key.PRODUCT_ID_LIST to productIds.toString(),
                LoggerConstant.Key.MESSAGE to errorMessage,
                LoggerConstant.Key.PROMO_CODE to promoCode,
                LoggerConstant.Key.STACK_TRACE to throwable.stackTraceToString()
            )
            ServerLogger.log(
                Priority.P2,
                LoggerConstant.Tag.P2_BUYER_FLOW_CART,
                mapData
            )
        }
    }

    fun logOnErrorLoadCourierNew(
        throwable: Throwable,
        shipmentCartItemModel: com.tokopedia.checkout.backup.view.uimodel.CheckoutOrderModel,
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInByDropOff: Boolean,
        promoCode: String
    ) {
        if (shouldTriggerLog(throwable)) {
            val productIds = shipmentCartItemModel.orderProductIds

            val errorMessage = throwable.message ?: "unknown exception"
            val mapData = mapOf(
                LoggerConstant.Key.ERROR_TYPE to LoggerConstant.Type.LOAD_COURIER_ERROR,
                LoggerConstant.Key.IS_OCS to isOneClickShipment.toString(),
                LoggerConstant.Key.IS_TRADE_IN to isTradeIn.toString(),
                LoggerConstant.Key.IS_TRADE_IN_INDOPAKET to isTradeInByDropOff.toString(),
                LoggerConstant.Key.PRODUCT_ID_LIST to productIds.toString(),
                LoggerConstant.Key.MESSAGE to errorMessage,
                LoggerConstant.Key.PROMO_CODE to promoCode,
                LoggerConstant.Key.STACK_TRACE to throwable.stackTraceToString()
            )
            ServerLogger.log(
                Priority.P2,
                LoggerConstant.Tag.P2_BUYER_FLOW_CART,
                mapData
            )
        }
    }

    fun logOnErrorApplyBo(
        throwable: Throwable,
        shipmentCartItemModel: ShipmentCartItemModel,
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInByDropOff: Boolean,
        promoCode: String
    ) {
        if (shouldTriggerLog(throwable)) {
            val productIds = mutableListOf<String>()
            for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                productIds.add(cartItemModel.productId.toString())
            }

            val errorMessage = throwable.message ?: "unknown exception"
            val mapData = mapOf(
                LoggerConstant.Key.ERROR_TYPE to LoggerConstant.Type.APPLY_BO_ERROR,
                LoggerConstant.Key.IS_OCS to isOneClickShipment.toString(),
                LoggerConstant.Key.IS_TRADE_IN to isTradeIn.toString(),
                LoggerConstant.Key.IS_TRADE_IN_INDOPAKET to isTradeInByDropOff.toString(),
                LoggerConstant.Key.PRODUCT_ID_LIST to productIds.toString(),
                LoggerConstant.Key.MESSAGE to errorMessage,
                LoggerConstant.Key.PROMO_CODE to promoCode,
                LoggerConstant.Key.STACK_TRACE to throwable.stackTraceToString()
            )
            ServerLogger.log(
                Priority.P2,
                LoggerConstant.Tag.P2_BUYER_FLOW_CART,
                mapData
            )
        }
    }

    fun logOnErrorApplyBoNew(
        throwable: Throwable,
        orderModel: CheckoutOrderModel,
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInByDropOff: Boolean,
        promoCode: String
    ) {
        if (shouldTriggerLog(throwable)) {
            val productIds = orderModel.orderProductIds

            val errorMessage = throwable.message ?: "unknown exception"
            val mapData = mapOf(
                LoggerConstant.Key.ERROR_TYPE to LoggerConstant.Type.APPLY_BO_ERROR,
                LoggerConstant.Key.IS_OCS to isOneClickShipment.toString(),
                LoggerConstant.Key.IS_TRADE_IN to isTradeIn.toString(),
                LoggerConstant.Key.IS_TRADE_IN_INDOPAKET to isTradeInByDropOff.toString(),
                LoggerConstant.Key.PRODUCT_ID_LIST to productIds.toString(),
                LoggerConstant.Key.MESSAGE to errorMessage,
                LoggerConstant.Key.PROMO_CODE to promoCode,
                LoggerConstant.Key.STACK_TRACE to throwable.stackTraceToString()
            )
            ServerLogger.log(
                Priority.P2,
                LoggerConstant.Tag.P2_BUYER_FLOW_CART,
                mapData
            )
        }
    }

    fun logOnErrorApplyBoNew(
        throwable: Throwable,
        orderModel: com.tokopedia.checkout.backup.view.uimodel.CheckoutOrderModel,
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInByDropOff: Boolean,
        promoCode: String
    ) {
        if (shouldTriggerLog(throwable)) {
            val productIds = orderModel.orderProductIds

            val errorMessage = throwable.message ?: "unknown exception"
            val mapData = mapOf(
                LoggerConstant.Key.ERROR_TYPE to LoggerConstant.Type.APPLY_BO_ERROR,
                LoggerConstant.Key.IS_OCS to isOneClickShipment.toString(),
                LoggerConstant.Key.IS_TRADE_IN to isTradeIn.toString(),
                LoggerConstant.Key.IS_TRADE_IN_INDOPAKET to isTradeInByDropOff.toString(),
                LoggerConstant.Key.PRODUCT_ID_LIST to productIds.toString(),
                LoggerConstant.Key.MESSAGE to errorMessage,
                LoggerConstant.Key.PROMO_CODE to promoCode,
                LoggerConstant.Key.STACK_TRACE to throwable.stackTraceToString()
            )
            ServerLogger.log(
                Priority.P2,
                LoggerConstant.Tag.P2_BUYER_FLOW_CART,
                mapData
            )
        }
    }

    fun logOnErrorCheckout(
        throwable: Throwable,
        request: String,
        isOneClickShipment: Boolean,
        isTradeIn: Boolean,
        isTradeInByDropOff: Boolean
    ) {
        if (shouldTriggerLog(throwable)) {
            val errorMessage = throwable.message ?: "unknown exception"
            val mapData = mapOf(
                LoggerConstant.Key.ERROR_TYPE to LoggerConstant.Type.CHECKOUT_ERROR,
                LoggerConstant.Key.IS_OCS to isOneClickShipment.toString(),
                LoggerConstant.Key.IS_TRADE_IN to isTradeIn.toString(),
                LoggerConstant.Key.IS_TRADE_IN_INDOPAKET to isTradeInByDropOff.toString(),
                LoggerConstant.Key.REQUEST to request,
                LoggerConstant.Key.MESSAGE to errorMessage,
                LoggerConstant.Key.STACK_TRACE to throwable.stackTraceToString()
            )
            ServerLogger.log(
                Priority.P2,
                LoggerConstant.Tag.P2_BUYER_FLOW_CART,
                mapData
            )
        }
    }

    private fun shouldTriggerLog(throwable: Throwable): Boolean {
        return throwable is MessageErrorException ||
            throwable is ResponseErrorException ||
            throwable is CartResponseErrorException ||
            throwable is AkamaiErrorException
    }
}
