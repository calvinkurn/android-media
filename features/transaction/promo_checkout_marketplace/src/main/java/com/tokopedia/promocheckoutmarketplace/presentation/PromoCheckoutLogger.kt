package com.tokopedia.promocheckoutmarketplace.presentation

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.purchase_platform.common.constant.LoggerConstant

object PromoCheckoutLogger {

    fun logOnErrorLoadPromoCheckoutPage(throwable: Throwable) {
        if (shouldTriggerLog(throwable)) {
            val errorMessage = throwable.message ?: "unknown exception"
            val mapData = mapOf(
                    LoggerConstant.Key.TYPE to LoggerConstant.Type.LOAD_PROMO_CHECKOUT_PAGE_ERROR,
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

    fun logOnErrorApplyPromo(throwable: Throwable) {
        if (shouldTriggerLog(throwable)) {
            val errorMessage = throwable.message ?: "unknown exception"
            val mapData = mapOf(
                    LoggerConstant.Key.TYPE to LoggerConstant.Type.APPLY_PROMO_CHECKOUT_ERROR,
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
        return throwable is PromoErrorException || throwable is AkamaiErrorException
    }

}