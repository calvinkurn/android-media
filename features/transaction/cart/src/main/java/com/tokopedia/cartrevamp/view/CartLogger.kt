package com.tokopedia.cartrevamp.view

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.cartrevamp.view.uimodel.CartItemHolderData
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.exception.ResponseErrorException
import com.tokopedia.purchase_platform.common.constant.LoggerConstant
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException

object CartLogger {

    fun logOnErrorLoadCartPage(throwable: Throwable) {
        if (shouldTriggerLog(throwable)) {
            val errorMessage = throwable.message
                ?: if (throwable is ResponseErrorException) "response is null or response is error but empty message" else "unknown exception"
            val mapData = mapOf(
                LoggerConstant.Key.ERROR_TYPE to LoggerConstant.Type.LOAD_CART_PAGE_ERROR,
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

    fun logOnErrorUpdateCartForCheckout(throwable: Throwable, cartItemDataList: List<CartItemHolderData>) {
        if (shouldTriggerLog(throwable)) {
            val productIdList = mutableListOf<String>()
            cartItemDataList.forEach { cartItemData ->
                productIdList.add(cartItemData.productId)
            }
            val errorMessage = throwable.message ?: "unknown exception"
            val mapData = mapOf(
                LoggerConstant.Key.ERROR_TYPE to LoggerConstant.Type.UPDATE_CART_FOR_CHECKOUT_ERROR,
                LoggerConstant.Key.MESSAGE to errorMessage,
                LoggerConstant.Key.PRODUCT_ID_LIST to productIdList.joinToString(","),
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
