package com.tokopedia.logger.service

import android.content.Context
import androidx.work.*
import com.tokopedia.logger.LogManager
import com.tokopedia.logger.utils.Constants.Companion.LOG_SERVICE_BACKOFF
import com.tokopedia.logger.utils.Constants.Companion.LOG_SERVICE_DELAY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class LogWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount > MAX_RETRY) {
            return Result.failure()
        }
        return withContext(Dispatchers.IO){
            try {
                LogManager.sendLogToServer()
                LogManager.deleteExpiredLogs()
            } catch (e:Exception) {
                e.printStackTrace()
            }
            Result.success()
        }
    }

    companion object {
        const val MAX_RETRY = 3
        const val WORKER_NAME = "LOG_WORKER"
        var lastSheduleTimestamp = 0L
        val worker by lazy {
            OneTimeWorkRequest
                    .Builder(LogWorker::class.java)
                    .setConstraints(Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build())
                    .setBackoffCriteria(BackoffPolicy.LINEAR, LOG_SERVICE_BACKOFF, TimeUnit.SECONDS)
                    .setInitialDelay(LOG_SERVICE_DELAY, TimeUnit.SECONDS)
                    .build()
        }

        val thresholdDiff = TimeUnit.SECONDS.toMillis(LOG_SERVICE_DELAY)

        fun scheduleWorker(context: Context) {
            try {
                if (lastSheduleTimestamp > 0L){
                    val diff = System.currentTimeMillis() - lastSheduleTimestamp
                    if (diff < thresholdDiff) {
                        return
                    }
                }
                lastSheduleTimestamp = System.currentTimeMillis()
                WorkManager.getInstance(context).enqueueUniqueWork(
                        WORKER_NAME,
                        ExistingWorkPolicy.KEEP,
                        worker)
            } catch (ex: Exception) {

            }
        }
    }
}