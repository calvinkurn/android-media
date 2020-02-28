package com.tokopedia.shop.home.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlin.coroutines.CoroutineContext

fun <T> CoroutineScope.asyncCatchError(context: CoroutineContext = coroutineContext,
                                       block: suspend CoroutineScope.() -> T,
                                       onError: suspend (Throwable) -> T): Deferred<T?> {
    return async(context) {
        try {
            block()
        } catch (e: Exception) {
            try {
                onError(e)
            } catch (e: Exception) {
                null
            }
        }
    }
}

