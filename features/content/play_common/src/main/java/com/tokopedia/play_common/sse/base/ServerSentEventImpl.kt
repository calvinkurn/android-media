package com.tokopedia.play_common.sse.base

import okhttp3.*
import okio.BufferedSource
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * Created By : Jonathan Darwin on September 03, 2021
 */
class ServerSentEventImpl(
    private val originalRequest: Request,
    private val listener: ServerSentEvent.Listener
): ServerSentEvent {

    private var client: OkHttpClient? = null
    private var lastEventId: String? = null
    private var call: Call? = null

    private var sseReader: Reader? = null
    private var reconnectTime = TimeUnit.SECONDS.toMillis(3)
    private var readTimeoutMillis = 0L

    fun connect(client: OkHttpClient) {
        this.client = client
        prepareCall(originalRequest)
        enqueue()
    }

    private fun prepareCall(request: Request) {
        client?.let {
            val requestBuilder = request.newBuilder()
                .header("Accept-Encoding", "")
                .header("Accept", "text/event-stream")
                .header("Cache-Control", "no-cache")

            lastEventId?.let { eventId ->
                requestBuilder.header("Last-Event-Id", eventId)
            }

            call = it.newCall(requestBuilder.build())

        } ?: throw AssertionError("Client is null")
    }

    private fun enqueue() {
        call?.enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                notifyFailure(e, null)
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) openSse(response)
                else notifyFailure(IOException(response.message), response)
            }
        })
    }

    private fun openSse(response: Response) {
        val body = response.body
        body?.let {
            sseReader = Reader(body.source())
            sseReader?.let {
                it.setTimeout(readTimeoutMillis, TimeUnit.MILLISECONDS)
                listener.onOpen(this, response)

                while (call != null && call?.isCanceled() == false && it.read()) {
                }
            }
        }
    }

    private fun notifyFailure(throwable: Throwable, response: Response?) {
        if(!retry(throwable, response)) {
            listener.onClosed(this)
            close()
        }
    }

    private fun retry(throwable: Throwable, response: Response?): Boolean {
        if(!Thread.currentThread().isInterrupted && call?.isCanceled() == false &&
                listener.onRetryError(this, throwable, response)) {
            val request = listener.onPreRetry(this, originalRequest) ?: return false
            prepareCall(request)

            try {
                Thread.sleep(reconnectTime)
            }
            catch (e: InterruptedException) {
                return false
            }

            if(!Thread.currentThread().isInterrupted && call?.isCanceled() == false) {
                enqueue()
                return true
            }
        }
        return false
    }

    override fun request(): Request = originalRequest

    override fun setTimeout(timeOut: Long, unit: TimeUnit) {
        sseReader?.setTimeout(timeOut, unit)
        readTimeoutMillis = unit.toMillis(timeOut)
    }

    override fun close() {
        if(call != null && call?.isCanceled() == false) call?.cancel()
    }

    inner class Reader(
        private val source: BufferedSource
    ) {
        private val COLON_DIVIDER = ":"
        private val UTF8_BOM = "\uFEFF"
        private val DATA = "data"
        private val ID = "id"
        private val EVENT = "event"
        private val RETRY = "retry"
        private val DEFAULT_EVENT = "message"
        private val EMPTY_STRING = ""

        private val DIGITS_ONLY = Pattern.compile("^[\\\\d]+\$")
        private var eventName = "DEFAULT_EVENT"
        private val data = StringBuilder()

        fun read(): Boolean {
            return try {
                val line = source.readUtf8LineStrict()
                processLine(line)
                true
            }
            catch (e: IOException) {
                notifyFailure(e, null)
                false
            }
        }

        fun setTimeout(timeout: Long, unit: TimeUnit) {
            source.timeout().timeout(timeout, unit)
        }

        private fun processLine(line: String) {
            if(line.isNullOrEmpty()) {
                dispatchEvent()
                return
            }

            val colonIndex = line.indexOf(COLON_DIVIDER)
            if(colonIndex == 0) listener.onComment(this@ServerSentEventImpl, line.substring(1).trim())
            else if(colonIndex != -1) {
                val field = line.substring(0, colonIndex)
                var value = EMPTY_STRING

                var valueIndex = colonIndex + 1
                if(valueIndex < line.length) {
                    if(line[valueIndex] == ' ') valueIndex++
                    value = line.substring(valueIndex)
                }
                processField(field, value)
            }
            else {
                processField(line, EMPTY_STRING)
            }
        }

        private fun dispatchEvent() {
            if(data.isEmpty()) return

            var dataString = data.toString()
            if(dataString.endsWith("\n")) dataString = dataString.substring(0, dataString.length - 1)
            listener.onMessage(this@ServerSentEventImpl, lastEventId ?: "", eventName, dataString)
            data.setLength(0)
            eventName = DEFAULT_EVENT
        }

        private fun processField(field: String, value: String) {
            if(DATA == field) data.append(value).append("\n")
            else if (ID == field) lastEventId = value
            else if (EVENT == field) eventName = value
            else if (RETRY == field && DIGITS_ONLY.matcher(value).matches()) {
                val timeout = value.toLong()
                if(listener.onRetryTime(this@ServerSentEventImpl, timeout)) {
                   reconnectTime = timeout
                }
            }
        }
    }
}