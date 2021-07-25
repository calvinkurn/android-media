package com.tokopedia.broadcaster.utils

import kotlinx.coroutines.delay
import java.io.IOException

/*
* exponential retry back-off
* with initial 0.1 second for delay
* and exponential based on [@factor]
* */
suspend fun <T> retry(
    times: Int = Int.MAX_VALUE,
    initialDelay: Long = 100, // 0.1 second
    reconnectDelay: Long = 1000,    // 1 second
    factor: Double = 2.0,
    block: suspend () -> T): T
{
    var currentDelay = initialDelay

    repeat(times - 1) {
        try {
            return block()
        } catch (e: IOException) {
            // you can log an error here and/or make a more finer-grained
            // analysis of the cause to see if retry is needed
        }

        delay(currentDelay)

        currentDelay = (currentDelay * factor)
            .toLong()
            .coerceAtMost(reconnectDelay)
    }
    return block() // last attempt
}