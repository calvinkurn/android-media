package com.tokopedia.checkout.view

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.constant.LoggerConstant
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException

object CheckoutLogger {

    fun logOnErrorLoadCourier(throwable: Throwable, shipmentCartItemModel: ShipmentCartItemModel, isOneClickShipment: Boolean, isTradeIn: Boolean, isTradeInByDropOff: Boolean) {
        if (shouldTriggerLog(throwable)) {
            val productIds = mutableListOf<String>()
            for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                productIds.add(cartItemModel.productId.toString())
            }

            val errorMessage = throwable.message ?: "unknown exception"
            ServerLogger.log(
                    Priority.P1,
                    LoggerConstant.Tag.P2_BUYER_FLOW_CART,
                    mapOf(
                            LoggerConstant.Key.TYPE to LoggerConstant.Type.LOAD_COURIER_ERROR,
                            LoggerConstant.Key.IS_OCS to isOneClickShipment.toString(),
                            LoggerConstant.Key.IS_TRADE_IN to isTradeIn.toString(),
                            LoggerConstant.Key.IS_TRADE_IN_INDOPAKET to isTradeInByDropOff.toString(),
                            LoggerConstant.Key.PRODUCT_ID_LIST to productIds.toString(),
                            LoggerConstant.Key.MESSAGE to errorMessage,
                            LoggerConstant.Key.STACK_TRACE to throwable.stackTrace.toString().substring(0, 50)
                    )
            )
        }
    }

    fun logOnErrorCheckout(throwable: Throwable, request: String, isOneClickShipment: Boolean, isTradeIn: Boolean, isTradeInByDropOff: Boolean) {
        if (shouldTriggerLog(throwable)) {
            val errorMessage = throwable.message ?: "unknown exception"
            ServerLogger.log(
                    Priority.P1,
                    LoggerConstant.Tag.P2_BUYER_FLOW_CART,
                    mapOf(
                            LoggerConstant.Key.TYPE to LoggerConstant.Type.CHECKOUT_ERROR,
                            LoggerConstant.Key.IS_OCS to isOneClickShipment.toString(),
                            LoggerConstant.Key.IS_TRADE_IN to isTradeIn.toString(),
                            LoggerConstant.Key.IS_TRADE_IN_INDOPAKET to isTradeInByDropOff.toString(),
                            LoggerConstant.Key.REQUEST to request,
                            LoggerConstant.Key.MESSAGE to errorMessage,
                            LoggerConstant.Key.STACK_TRACE to throwable.stackTrace.toString().substring(0, 50)
                    )
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