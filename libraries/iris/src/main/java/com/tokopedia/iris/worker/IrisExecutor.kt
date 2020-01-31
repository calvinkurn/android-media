package com.tokopedia.iris.worker

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.asCoroutineDispatcher
import timber.log.Timber
import java.util.concurrent.Executors

/**
 * @author okasurya on 2019-07-23.
 */
object IrisExecutor{
    val handler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, ex ->
            IrisService.isRunning = false
            Timber.e("P2#IRIS#CoroutineExceptionHandler %s", ex.toString())
        }
    }

    val executor by lazy {
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }
}
