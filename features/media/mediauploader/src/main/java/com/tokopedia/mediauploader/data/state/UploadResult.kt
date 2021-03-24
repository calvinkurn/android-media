package com.tokopedia.mediauploader.data.state

import com.tokopedia.mediauploader.util.CrashlyticsLogger.logExceptionToCrashlytics
import com.tokopedia.mediauploader.util.MediaUplaoderException
import java.io.File

sealed class UploadResult {
    class Success(val uploadId: String): UploadResult()

    @Suppress("ThrowableNotThrown")
    class Error(
            val message: String,
            filePath: File? = null
    ): UploadResult() {
        init {
            if (filePath != null && filePath.path.isNotEmpty()) {
                val logMessage = "Error upload image %s because %s".format(filePath?.path?: "", message)
                val exception = MediaUplaoderException(logMessage)

                logExceptionToCrashlytics(exception)
            }
        }
    }
}