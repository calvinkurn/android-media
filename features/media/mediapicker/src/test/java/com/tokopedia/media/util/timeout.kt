package com.tokopedia.media.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

suspend fun <T> withTimeout(timeout: Float, block: suspend CoroutineScope.() -> T): T {
    checkTimeout(timeout)
    return withContext(FlowTimeoutElement(timeout), block)
}

internal fun checkTimeout(timeout: Float) {
    check(timeout > 0) {
        "the timeout must be greater than 0: $timeout"
    }
}

internal class FlowTimeoutElement(
    val timeout: Float,
) : CoroutineContext.Element {
    companion object Key : CoroutineContext.Key<FlowTimeoutElement>

    override val key: CoroutineContext.Key<*> = Key
}

internal suspend fun contextTimeout(): Float {
    return currentCoroutineContext()[FlowTimeoutElement.Key]?.timeout ?: DEFAULT_TIMEOUT
}

internal fun assertCallingContextIsNotSuspended() {
    val stackTrace = Exception().stackTraceToString()
    if (REFLECT_INVOKE_SUSPEND in stackTrace) {
        error("Calling context is suspending; use a suspending method instead")
    }
}

const val REFLECT_INVOKE_SUSPEND = "invokeSuspend"
const val DEFAULT_TIMEOUT = 1000f
