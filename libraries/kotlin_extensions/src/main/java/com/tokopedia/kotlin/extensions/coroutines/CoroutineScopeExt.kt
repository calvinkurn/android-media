package com.tokopedia.kotlin.extensions.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.launchCatchError(context: CoroutineContext = coroutineContext,
                                    block: suspend (()->Unit),
                                    onError: suspend (Throwable)-> Unit) =
    launch (context){
        try{
            block()
        } catch (t: Throwable){
            try {
                onError(t)
            } catch (e: Throwable){

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
