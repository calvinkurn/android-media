package com.tokopedia.usecase.launch_cache_error

import kotlinx.coroutines.CoroutineScope
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
