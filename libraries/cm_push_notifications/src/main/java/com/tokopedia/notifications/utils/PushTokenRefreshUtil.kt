package com.tokopedia.notifications.utils

import android.content.Context
import androidx.work.*
import com.tokopedia.notifications.worker.PushTokenRefreshWorker
import java.util.concurrent.TimeUnit



class PushTokenRefreshUtil {
    private var WORKER_ID = "PUSH_TOKEN_REFRESH_WORKER"
    fun scheduleWorker(appContext: Context, time: Long) {
        try {
            val constraints = Constraints.Builder().run {
                setRequiredNetworkType(NetworkType.UNMETERED)    //Wifi
                setRequiredNetworkType(NetworkType.METERED)      //data
                build()
            }
            val periodicWorker = PeriodicWorkRequest
                .Builder(PushTokenRefreshWorker::class.java, time, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(appContext).enqueueUniquePeriodicWork(
                WORKER_ID,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorker
            )
        } catch (_: Exception) {

        }
    }
}
