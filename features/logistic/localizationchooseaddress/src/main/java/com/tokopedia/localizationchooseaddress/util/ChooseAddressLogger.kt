package com.tokopedia.localizationchooseaddress.util

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

object ChooseAddressLogger {

    fun logOnLocalizing(throwable: Throwable, key: String, fieldValue: String) {
        val errorMessage = throwable.message
            ?: "unknown exception"
        val mapData = mapOf(
            ChooseAddressLoggerConstant.Key.FIELD to key,
            ChooseAddressLoggerConstant.Key.FIELD_VALUE to fieldValue,
            ChooseAddressLoggerConstant.Key.MESSAGE to errorMessage,
            ChooseAddressLoggerConstant.Key.STACK_TRACE to throwable.stackTraceToString()
        )
        ServerLogger.log(
            Priority.P2,
            ChooseAddressLoggerConstant.Tag.LOCALIZING_ADDRESS_ERROR,
            mapData
        )
    }

}