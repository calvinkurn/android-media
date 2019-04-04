package com.tokopedia.iris

import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext

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
