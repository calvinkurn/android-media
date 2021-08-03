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

}