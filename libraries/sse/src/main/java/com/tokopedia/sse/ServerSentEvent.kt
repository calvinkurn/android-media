package com.tokopedia.sse

import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * Created By : Jonathan Darwin on September 03, 2021
 */
interface ServerSentEvent {
    fun request(): Request

    fun setTimeout(timeOut: Long, unit: TimeUnit)

    fun close()

    interface Listener {
        fun onOpen(sse: ServerSentEvent, response: Response)

        fun onMessage(sse: ServerSentEvent, id: String, event: String, message: String)

        fun onComment(sse: ServerSentEvent, comment: String)

        fun onRetryTime(sse: ServerSentEvent, milliseconds: Long): Boolean

        fun onRetryError(sse: ServerSentEvent, throwable: Throwable, response: Response?): Boolean

        fun onClosed(sse: ServerSentEvent)

        fun onPreRetry(sse: ServerSentEvent, originalRequest: Request): Request?
    }
}
