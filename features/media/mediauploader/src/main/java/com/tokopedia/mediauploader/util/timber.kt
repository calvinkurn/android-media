package com.tokopedia.mediauploader.util

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import timber.log.Timber
import java.io.File

fun trackToTimber(filePath: File? = null, sourceId: String, message: String) {
    if (filePath != null && filePath.path.isNotEmpty()) {
        trackToTimber(sourceId, "Error upload image %s because %s".format(filePath.path, message))
    }
}

fun trackToTimber(sourceId: String = "", errorMessage: String) {
    ServerLogger.log(Priority.P1, "MEDIA_UPLOADER_ERROR", mapOf("type" to sourceId, "err" to errorMessage))
}