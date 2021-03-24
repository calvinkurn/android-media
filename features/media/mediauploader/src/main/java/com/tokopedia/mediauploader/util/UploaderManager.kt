package com.tokopedia.mediauploader.util

import com.tokopedia.mediauploader.domain.UploaderUseCase.Companion.ERROR_SOURCE_NOT_FOUND

object UploaderManager {

    /**
     * check if the exception comes from [ERROR_SOURCE_NOT_FOUND]
     */
    fun isSourceMediaNotFound(exception: Exception): Boolean {
        val exceptionMessage = exception.message.orEmpty()
        return exceptionMessage.startsWith(ERROR_SOURCE_NOT_FOUND)
    }

    /**
     * Because the error message from Backend has requestId and trackerId
     * we should filtering it for better readable message to user.
     *
     * this filter function will be getting the common error message
     * without including the BE's data.
     *
     * @example:
     * "Dimensi gambar maksimum 1000x1000 (306) <waowakb32984bkaf>"
     *
     * it should be return:
     * "Dimensi gambar maksimum 1000x1000"
     */
    fun mediaErrorMessage(errorMessage: String): String {
        val requestIdSymbol = "(" // (0-9)
        val trackerIdSymbol = "<" // <a-z0-9>

        if (!errorMessage.contains(requestIdSymbol) || !errorMessage.contains(trackerIdSymbol)) {
            return errorMessage
        }

        val requestIdIndex = when {
            errorMessage.contains(requestIdSymbol) -> {
                errorMessage.indexOf(requestIdSymbol)
            }
            errorMessage.contains(trackerIdSymbol) -> {
                errorMessage.indexOf(trackerIdSymbol)
            }
            else -> {
                -1
            }
        }

        return if (requestIdIndex != -1 && requestIdIndex > 0) {
            errorMessage.substring(0, requestIdIndex)
        } else {
            errorMessage
        }
    }

}