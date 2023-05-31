package com.tokopedia.developer_options.utils

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit



class PushTokenRefreshUtil {
    private var WORKER_ID = "PUSH_TOKEN_REFRESH_WORKER"
    fun scheduleWorker(appContext: Context, time: Long) {
        try {
            val periodicWorker = PeriodicWorkRequest
                .Builder(PushRefreshWorker::class.java, time, TimeUnit.MINUTES)
                .setConstraints(Constraints.NONE)
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
