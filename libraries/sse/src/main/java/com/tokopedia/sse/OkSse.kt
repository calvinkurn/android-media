package com.tokopedia.sse

import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 * Created By : Jonathan Darwin on September 03, 2021
 */
class OkSse(
    private val client: OkHttpClient
) {
    constructor(): this(
        OkHttpClient.Builder().readTimeout(0, TimeUnit.SECONDS).retryOnConnectionFailure(true).build()
    )

    val getClient: OkHttpClient
        get() = client

    fun newServerSentEvent(request: Request, listener: ServerSentEvent.Listener): ServerSentEvent {
        val sse = ServerSentEventImpl(request, listener)
        sse.connect(client)
        return sse
    }
}
