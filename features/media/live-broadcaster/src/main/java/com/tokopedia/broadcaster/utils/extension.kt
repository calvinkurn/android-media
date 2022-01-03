package com.tokopedia.broadcaster.utils

import com.wmspanel.libstream.StreamerGL
import kotlinx.coroutines.delay
import java.io.IOException

internal fun <T> StreamerGL.safeExecute(
    execute: StreamerGL.() -> T
): T? {
    return try {
        execute()
    } catch(e: Throwable) {
        null
    }
}

/*
* exponential retry back-off
* with initial 0.1 second for delay
* and exponential based on [@factor]
* */
suspend fun <T> retry(
    times: Int = Int.MAX_VALUE,
    initialDelay: Long = 100, // 0.1 second
    reconnectDelay: Long = 1000, // 1 second
    factor: Double = 2.0,
    block: suspend () -> T,
    onError: () -> Unit
): T {
    var currentDelay = initialDelay

    repeat(times - 1) {
        try {
            return block()
        } catch (e: IOException) {
            onError()
        }

        delay(currentDelay)

        currentDelay = (currentDelay * factor)
            .toLong()
            .coerceAtMost(reconnectDelay)
    }

    // last attempt
    return block()
}