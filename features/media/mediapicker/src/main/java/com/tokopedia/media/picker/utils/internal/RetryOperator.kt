package com.tokopedia.media.picker.utils.internal

import kotlinx.coroutines.delay
import kotlin.math.pow

class RetryOperator internal constructor(
    private val retries: Int,
    private val retry: suspend RetryOperator.() -> Unit
) {
    private var tryNumber: Int = 0

    /**
     * Sample scenario:
     * retries max = 3
     * exponent number = 200
     *
     * #1 -> 400 millis
     * #2 -> 800 millis
     * #3 -> 1600 millis
     */
    private fun exponentialRetries(tryNumber: Int): Long {
        return 2.0.pow(tryNumber).toLong() * BASE_EXPONENTIAL_MILLIS
    }

    suspend fun operationFailed() {
        tryNumber++

        if (tryNumber < retries) {
            delay(exponentialRetries(tryNumber))
            retry.invoke(this)
        }
    }

    companion object {
        private const val BASE_EXPONENTIAL_MILLIS = 200
    }
}

suspend fun retryOperator(
    retries: Int = 10,
    operation: suspend RetryOperator.() -> Unit
) {
    val retryOperation = RetryOperator(
        retries,
        operation
    )

    operation.invoke(retryOperation)
}
