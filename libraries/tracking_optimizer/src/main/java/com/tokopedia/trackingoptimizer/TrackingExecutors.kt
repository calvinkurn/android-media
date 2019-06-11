package com.tokopedia.trackingoptimizer

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.asCoroutineDispatcher
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