package com.tokopedia.notifications.utils

import android.app.Application
import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.tokopedia.notifications.worker.PushAmplificationWorker
import com.tokopedia.notifications.worker.PushTokenRefreshWorker
import java.util.concurrent.TimeUnit

class PushAmplificationRefreshUtil {
    private var WORKER_ID = "PUSH_AMPLIFICATION_REFRESH_WORKER"
    private var WORKER_INITIAL_DELAY: Long = 3
    companion object{
        lateinit var application :Application
    }
    fun scheduleWorker(appContext: Context, time: Long) {
        try {
            val constraints = Constraints.Builder().run {
                setRequiredNetworkType(NetworkType.UNMETERED)    //Wifi
                setRequiredNetworkType(NetworkType.METERED)      //data
                build()
            }
            val periodicWorker = PeriodicWorkRequest
                .Builder(PushAmplificationWorker::class.java, time, TimeUnit.HOURS)
                .setConstraints(constraints)
                .setInitialDelay(WORKER_INITIAL_DELAY, TimeUnit.HOURS)
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
