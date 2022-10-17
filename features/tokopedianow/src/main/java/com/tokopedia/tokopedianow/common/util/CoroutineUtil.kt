package com.tokopedia.tokopedianow.common.util

import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

object CoroutineUtil {

    fun CoroutineScope.launchWithDelay(
        block: () -> Unit,
        onError: (Throwable) -> Unit = {},
        delay: Long
    ): Job {
        return launchCatchError(block = {
            delay(delay)
            block.invoke()
        }) {
            onError.invoke(it)
        }
    }
}