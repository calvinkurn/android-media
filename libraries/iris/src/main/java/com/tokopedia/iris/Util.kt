package com.tokopedia.iris

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

fun CoroutineScope.launchCatchError(context: CoroutineContext = coroutineContext,
                                    block: suspend (()->Unit),
                                    onError: (Throwable)-> Unit) =
        launch (context){
            try{
                block.invoke()
            } catch (t: Throwable){
                onError(t)
            }
        }
