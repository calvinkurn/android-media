package com.tokopedia.trackingoptimizer

import android.annotation.TargetApi
import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import com.tokopedia.trackingoptimizer.repository.TrackingRepository
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.CoroutineScope
import kotlin.coroutines.experimental.CoroutineContext

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class SendTrackQueueJobService : JobService(), CoroutineScope {

    val trackingRepository: TrackingRepository by lazy {
        TrackingRepository(this)
    }

    val trackingOptimizerRouter: TrackingOptimizerRouter? by lazy {
        val application = this.application
        if (application is TrackingOptimizerRouter) {
            application
        } else {
            null
        }
    }

    val handler: CoroutineExceptionHandler by lazy {
        CoroutineExceptionHandler { _, ex ->
            decreaseCounter()
        }
    }

    // allowing only 1 thread at a time
    override val coroutineContext: CoroutineContext by lazy {
        TrackingExecutors.executor + handler
    }

    override fun onStartJob(jobParameters: JobParameters): Boolean {
        sendTrack(this,trackingRepository, trackingOptimizerRouter) {
            jobFinished(jobParameters, true)
        }
        return true
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        return true
    }
}
