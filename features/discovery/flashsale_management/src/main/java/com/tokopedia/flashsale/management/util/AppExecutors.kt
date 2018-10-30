package com.tokopedia.flashsale.management.util

import kotlinx.coroutines.experimental.Dispatchers.Default
import kotlinx.coroutines.experimental.Dispatchers.IO
import kotlinx.coroutines.experimental.Dispatchers.Main
import kotlinx.coroutines.experimental.asCoroutineDispatcher
import java.util.concurrent.Executors
import kotlin.coroutines.experimental.CoroutineContext

object AppExecutors {
    val ioContext: CoroutineContext = IO
    val uiContext: CoroutineContext = Main
    val defaultContext: CoroutineContext = Default
    val networkContext: CoroutineContext = Executors.newFixedThreadPool(3).asCoroutineDispatcher()
}
