package com.tokopedia.iris.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.launchCatchError(context: CoroutineContext = coroutineContext,
                                    onError: (Throwable)-> Unit = {},
                                    block: suspend (()->Unit)) {
    launch(context) {
        try {
            block.invoke()
        } catch (t: Throwable) {
            onError(t)
        }
    }
}

fun logIris(cache: Cache, message: String) {
    if (cache.isEnableLogEntries()) {

    }
}