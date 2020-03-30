package com.tokopedia.kotlin.extensions.coroutines

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.launchCatchError(context: CoroutineContext = coroutineContext,
                                    block: suspend CoroutineScope.() -> Unit,
                                    onError: suspend (Throwable) -> Unit) =
    launch (context) {
        try{
            block()
        } catch (t: Throwable){
            if (t is CancellationException) throw t
            else {
                try {
                    onError(t)
                } catch (e: Throwable){

                }
            }
        }
    }

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
