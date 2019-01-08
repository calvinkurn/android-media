package com.tokopedia.trackingoptimizer

import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.asCoroutineDispatcher
import java.util.concurrent.Executors

/**
 * This is handler exclusively used in tracking optimizer library,
 * to handle concurrency for read and write to tracking database.
 * Do not use this executors elsewhere!
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