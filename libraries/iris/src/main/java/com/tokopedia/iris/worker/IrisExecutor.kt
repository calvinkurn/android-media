package com.tokopedia.iris.worker

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

/**
 * @author okasurya on 2019-07-23.
 */
object IrisExecutor{
    val handler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, ex ->
            IrisService.isRunning = false
            Timber.e("P1#IRIS#CoroutineExceptionHandler %s", ex.toString())
        }
    }

    val executor = Dispatchers.IO
}
