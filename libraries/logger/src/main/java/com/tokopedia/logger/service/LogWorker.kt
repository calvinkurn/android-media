package com.tokopedia.logger.service

import android.content.Context
import androidx.work.*
import com.tokopedia.logger.LogManager
import com.tokopedia.logger.utils.Constants.Companion.LOG_SERVICE_DELAY
import com.tokopedia.logger.utils.Constants.Companion.SCHEDULE_MIN_GAP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class LogWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                LogManager.sendLogToServer()
                LogManager.deleteExpiredLogs()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Result.success()
        }
    }

    companion object {
        const val WORKER_NAME = "LOG_WORKER"
        var lastSchedule = 0L

        val worker by lazy {
            OneTimeWorkRequest
                    .Builder(LogWorker::class.java)
                    .setConstraints(Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build())
                    .setInitialDelay(LOG_SERVICE_DELAY, TimeUnit.SECONDS)
                    .build()
        }

        suspend fun scheduleWorker(context: Context) {
            try {
                //Schedule min gap is to prevent burst scheduling
                if (System.currentTimeMillis() - lastSchedule > SCHEDULE_MIN_GAP) {
                    //to allow the previous running worker update the state
                    delay(500)

                    WorkManager.getInstance(context).enqueueUniqueWork(
                            WORKER_NAME,
                            ExistingWorkPolicy.KEEP,
                            worker)
                    lastSchedule = System.currentTimeMillis()
                }
            } catch (ex: Exception) {

            }
        }
    }
}