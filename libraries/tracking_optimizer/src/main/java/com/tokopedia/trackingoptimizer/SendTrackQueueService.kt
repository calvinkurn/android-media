package com.tokopedia.trackingoptimizer

import android.app.Service
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PersistableBundle
import com.tokopedia.trackingoptimizer.repository.TrackingRepository
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.CoroutineScope
import kotlin.coroutines.experimental.CoroutineContext

/**
 * Created by hendry on 27/12/18.
 */
class SendTrackQueueService : Service(), CoroutineScope {

    private val trackingRepository: TrackingRepository by lazy {
        TrackingRepository(this)
    }

    private val trackingOptimizerRouter: TrackingOptimizerRouter? by lazy {
        this.application as? TrackingOptimizerRouter
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

    companion object {
        fun start(context: Context) {
            // allowing only 1 service at a time
            if (atomicInteger.get() < 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler
                        ?: return

                    val bundle = PersistableBundle()

                    jobScheduler.schedule(JobInfo.Builder(281,
                        ComponentName(context, SendTrackQueueJobService::class.java))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setExtras(bundle)
                        .build())
                } else {
                    context.startService(Intent(context, SendTrackQueueService::class.java))
                }
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sendTrack(this, trackingRepository, trackingOptimizerRouter) {
            stopSelf()
        }
        return Service.START_NOT_STICKY
    }
}

