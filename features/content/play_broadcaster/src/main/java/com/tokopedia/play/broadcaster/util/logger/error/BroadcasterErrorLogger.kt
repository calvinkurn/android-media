package com.tokopedia.play.broadcaster.util.logger.error

/**
 * Created By : Jonathan Darwin on February 06, 2024
 */
interface BroadcasterErrorLogger {

    fun sendLog(
        throwable: Throwable,
        additionalData: Any? = null,
    )

    fun sendLog(
        message: String,
        additionalData: Any? = null,
    )
}
