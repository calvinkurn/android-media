package com.tokopedia.mediauploader.common.logger

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import java.io.File

const val ERROR_MAX_LENGTH = 1500

fun trackToTimber(filePath: File? = null, sourceId: String, message: String) {
    if (filePath != null && filePath.path.isNotEmpty() && message.isNotEmpty()) {
        trackToTimber(sourceId, "Error upload image ${filePath.path} because $message")
    }
}

fun trackToTimber(sourceId: String = "", errorMessage: String) {
    ServerLogger.log(Priority.P1, "MEDIA_UPLOADER_ERROR", mapOf("type" to sourceId, "err" to errorMessage))
}