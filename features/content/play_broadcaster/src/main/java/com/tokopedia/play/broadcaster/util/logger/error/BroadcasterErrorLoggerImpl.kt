package com.tokopedia.play.broadcaster.util.logger.error

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import timber.log.Timber
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on February 06, 2024
 */
class BroadcasterErrorLoggerImpl @Inject constructor() : BroadcasterErrorLogger {

    override fun sendLog(
        throwable: Throwable,
        additionalData: Any?
    ) {
        sendLog(throwable.stackTraceToString(), additionalData)
    }

    override fun sendLog(
        message: String,
        additionalData: Any?
    ) {
        if (message.isEmpty()) return

        sendLog(
            mapOf(
                FIELD_ERROR_MESSAGE to message,
                FIELD_ADDITIONAL_DATA to (additionalData?.toString() ?: "")
            )
        )
    }

    private fun sendLog(
        messages: Map<String, String>,
    ) {
        Timber.tag(TAG_BROADCASTER_ERROR).d(messages.toString())

        ServerLogger.log(
            Priority.P2,
            TAG_BROADCASTER_ERROR,
            messages
        )
    }

    companion object {
        private const val TAG_BROADCASTER_ERROR = "BROADCASTER_ERROR"

        private const val FIELD_ERROR_MESSAGE = "errorMessage"
        private const val FIELD_ADDITIONAL_DATA = "additionalData"
    }
}
