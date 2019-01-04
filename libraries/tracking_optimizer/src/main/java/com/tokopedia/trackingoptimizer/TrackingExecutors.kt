package com.tokopedia.trackingoptimizer

import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.asCoroutineDispatcher
import java.util.concurrent.Executors

/**
 * Created by hendry on 27/12/18.
 */
object TrackingExecutors{

    val handler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, ex ->
            // no op
        }
    }

    val executor by lazy {
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }
}