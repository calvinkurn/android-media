package com.tokopedia.logger.utils

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun globalScopeLaunch(block: suspend (()->Unit), onError: suspend (Throwable)-> Unit = {}, onFinish: ()->Unit = {}) =
    GlobalScope.launch {
        try{
            block()
        } catch (t: Throwable){
            try {
                onError(t)
            } catch (e: Throwable){

            }
        } finally {
            onFinish()
        }
    }
