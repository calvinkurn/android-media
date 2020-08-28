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
        val worker by lazy {
            OneTimeWorkRequest
                    .Builder(LogWorker::class.java)
                    .setConstraints(Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build())
                    .setInitialDelay(LOG_SERVICE_DELAY, TimeUnit.SECONDS)
                    .build()
        }

        fun scheduleWorker(context: Context) {
            try {
                WorkManager.getInstance(context).enqueueUniqueWork(
                        WORKER_NAME,
                        ExistingWorkPolicy.KEEP,
                        worker)
            } catch (ex: Exception) {

            }
        }
    }
}