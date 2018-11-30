package com.tokopedia.kotlin.extensions.coroutines

import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.Dispatchers.Default
import kotlinx.coroutines.experimental.Dispatchers.IO
import kotlinx.coroutines.experimental.Dispatchers.Main
import kotlin.coroutines.experimental.CoroutineContext

object AppExecutors {
    val ioContext: CoroutineContext = IO
    val uiContext: CoroutineContext = Main
    val bgContext: CoroutineContext = Default
}

fun <T> Deferred<T>.thenOnUI(scope: CoroutineScope, uiFunction: (T) -> Unit) {
    scope.launch(AppExecutors.uiContext) {
        uiFunction(this@thenOnUI.await())
    }
}
