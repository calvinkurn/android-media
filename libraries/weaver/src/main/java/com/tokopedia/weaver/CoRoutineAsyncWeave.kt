package com.tokopedia.weaver

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CoRoutineAsyncWeave : WeaveAsyncProvider, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default

    override fun executeAsync(weaveInterface: WeaveInterface) {
        launch {
            weaveInterface.execute()
        }
    }
}