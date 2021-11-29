package com.tokopedia.product.detail.view.util

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.data.util.ProductDetailConstant

object ProductDetailLogger {

    private const val STACK_TRACE_TRIM_LENGTH = 50
    private const val ERROR_CODE_RANGE_START = 400
    private const val ERROR_CODE_RANGE_END = 599

    private val GENERIC_ERROR_MESSAGES = listOf("terjadi kesalahan", "koneksi")
    private const val LOCALIZATION_FORMAT_ERROR = "localization_format_error"

    fun logLocalization(message:String) {
        log(getMap(LOCALIZATION_FORMAT_ERROR, "", "", message, ""))
    }

    fun logThrowable(throwable: Throwable, errorType: String, productId: String, deviceId: String, extras: String = "") {
        if (throwable is MessageErrorException || (throwable is RuntimeException && isServerError(throwable))) {
            log(getMap(errorType, productId, deviceId, throwable.localizedMessage
                    ?: "", getTrimmedStackTrace(throwable), extras))
        }
    }

    fun logMessage(errorMessage: String, errorType: String, productId: String, deviceId: String, extras: String = "") {
        GENERIC_ERROR_MESSAGES.forEach {
            if (errorMessage.toLowerCase().contains(it)) return
        }
        log(getMap(errorType, productId, deviceId, errorMessage, "", extras))
    }

    private fun getMap(errorType: String, productId: String, deviceId: String, errorMessage: String, stackTrace: String, extras: String = ""): Map<String, String> {
        val map = mutableMapOf(
                ProductDetailConstant.ERROR_TYPE_KEY to errorType,
                ProductDetailConstant.PRODUCT_ID_KEY to productId,
                ProductDetailConstant.DEVICE_ID_KEY to deviceId,
                ProductDetailConstant.MESSAGE_KEY to errorMessage,
                ProductDetailConstant.STACK_TRACE_KEY to stackTrace)
        if (extras.isNotBlank()) {
            map[ProductDetailConstant.EXTRAS_KEY] = extras
        }
        return map
    }

    private fun log(map: Map<String, String>) {
        ServerLogger.log(Priority.P2, ProductDetailConstant.PDP_LOG_TAG, map)
    }

    private fun getTrimmedStackTrace(throwable: Throwable): String {
        return throwable.stackTrace.toString().substring(0, STACK_TRACE_TRIM_LENGTH)
    }

    private fun isServerError(throwable: Throwable): Boolean {
        return try {
            val code = throwable.localizedMessage.toIntOrZero()
            code in ERROR_CODE_RANGE_START..ERROR_CODE_RANGE_END
        } catch (e1: NumberFormatException) {
            false
        }
    }
}