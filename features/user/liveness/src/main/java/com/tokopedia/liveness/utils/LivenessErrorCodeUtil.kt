package com.tokopedia.liveness.utils

import com.tokopedia.network.exception.MessageErrorException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object LivenessErrorCodeUtil {

    private const val NUMBER_EXCEPTION = -99

    fun getErrorCode(e: Throwable): Int {
        if (e is UnknownHostException) {
            return LivenessConstants.LIVENESS_UNKNOWNHOST
        } else if (e is SocketTimeoutException) {
            return LivenessConstants.LIVENESS_SOCKET_TIMEOUT
        } else if (e is RuntimeException && e.getLocalizedMessage() != null && e.getLocalizedMessage() != "" && e.getLocalizedMessage().length <= 3) {
            return try {
                when (e.localizedMessage.toInt()) {
                    400 -> LivenessConstants.LIVENESS_400
                    401 -> LivenessConstants.LIVENESS_401
                    403 -> LivenessConstants.LIVENESS_403
                    408 -> LivenessConstants.LIVENESS_408
                    500 -> LivenessConstants.LIVENESS_500
                    502 -> LivenessConstants.LIVENESS_502
                    504-> LivenessConstants.LIVENESS_504
                    else -> LivenessConstants.LIVENESS_ERROR_CODE_DEFAULT
                }
            } catch (ex: NumberFormatException) {
                NUMBER_EXCEPTION
            }
        } else if (e is MessageErrorException && e.message?.isNotEmpty() == true) {
            return LivenessConstants.LIVENESS_MESSAGE_ERROR
        } else if (e is IOException) {
            return LivenessConstants.LIVENESS_IO_EXCEPTION
        }
        return LivenessConstants.LIVENESS_ERROR_CODE_DEFAULT
    }
}