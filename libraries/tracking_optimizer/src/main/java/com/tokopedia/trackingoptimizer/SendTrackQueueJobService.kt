package com.tokopedia.trackingoptimizer

import android.annotation.TargetApi
import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import com.tokopedia.trackingoptimizer.repository.TrackRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class SendTrackQueueJobService : JobService(), CoroutineScope {

    val trackRepository: TrackRepository by lazy {
        TrackRepository(this)
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
        sendTrack(this, trackRepository) {
            jobFinished(jobParameters, false)
        }
        return true
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        return true
    }
}
