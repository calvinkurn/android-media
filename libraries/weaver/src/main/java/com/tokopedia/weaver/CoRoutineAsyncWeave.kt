package com.tokopedia.weaver

import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CoRoutineAsyncWeave : WeaveAsyncProvider, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    override fun executeAsync(weaveInterface: WeaveInterface) {
        this.launchCatchError(
            block = {
                weaveInterface.execute()
            },
            onError = {
                //send scalyr log
                var errorMap = mapOf("type" to "crashLog", "reason" to (it.localizedMessage))
                logError(errorMap)
            }
        )
    }

    private fun logError(messageMap: Map<String, String>){
        ServerLogger.log(Priority.P1, "WEAVER_CRASH", messageMap)
    }
}