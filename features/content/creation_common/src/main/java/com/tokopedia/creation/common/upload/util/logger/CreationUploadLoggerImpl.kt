package com.tokopedia.creation.common.upload.util.logger

import android.util.Log
import com.tokopedia.config.GlobalConfig
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on October 26, 2023
 */
class CreationUploadLoggerImpl @Inject constructor() : CreationUploadLogger {

    override fun sendLog(uploadData: CreationUploadData, throwable: Throwable) {
        sendLog(uploadData.toString(), throwable)
    }

    override fun sendLog(uploadData: String, throwable: Throwable) {
        sendLog(
            mapOf(
                FIELD_UPLOAD_DATA to uploadData,
                FIELD_STACK_TRACE to throwable.stackTraceToString()
            )
        )
    }

    override fun sendLog(throwable: Throwable) {
        sendLog(
            mapOf(
                FIELD_STACK_TRACE to throwable.stackTraceToString()
            )
        )
    }

    private fun sendLog(messages: Map<String, String>) {
        if (GlobalConfig.DEBUG) {
            Log.d(TAG_CONTENT_UPLOAD_ERROR, "Upload Data : ${messages[FIELD_UPLOAD_DATA]}")
            Log.d(TAG_CONTENT_UPLOAD_ERROR, "Stack Trace : ${messages[FIELD_STACK_TRACE]?.replace("\n", " ")}")
        }

        ServerLogger.log(
            Priority.P2,
            TAG_CONTENT_UPLOAD_ERROR,
            messages
        )
    }

    companion object {
        private const val TAG_CONTENT_UPLOAD_ERROR = "CONTENT_UPLOAD_ERROR"

        private const val FIELD_UPLOAD_DATA = "uploadData"
        private const val FIELD_STACK_TRACE = "stackTrace"
    }
}
