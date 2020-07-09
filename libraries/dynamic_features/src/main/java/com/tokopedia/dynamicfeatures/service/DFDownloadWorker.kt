package com.tokopedia.dynamicfeatures.service

import android.content.Context
import androidx.work.*
import com.tokopedia.dynamicfeatures.config.DFRemoteConfig
import java.util.concurrent.TimeUnit

class DFDownloadWorker(private val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount > getMaxRetry(appContext)) {
            return Result.failure()
        }
        return DFDownloader.startJob(appContext)
    }

    companion object {
        const val DF_DL_WORKER_NAME = "DF_DL_WORKER"
        fun scheduleOneTimeWorker(context: Context, isImmediate: Boolean,
                                   existingWorkPolicy: ExistingWorkPolicy = ExistingWorkPolicy.KEEP) {
            try {
                val retryDelay = getDefaultDelayFromConfigInMillis(context)
                val initialDelay = if (isImmediate) {
                    1000 //1s
                } else {
                    retryDelay
                }
                val worker = OneTimeWorkRequest
                        .Builder(DFDownloadWorker::class.java)
                        .setConstraints(Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                .build())
                        .setBackoffCriteria(BackoffPolicy.LINEAR, retryDelay, TimeUnit.MILLISECONDS)
                        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                        .build()
                WorkManager.getInstance(context).enqueueUniqueWork(
                        DF_DL_WORKER_NAME,
                        existingWorkPolicy,
                        worker)
            } catch (ex: Exception) {

            }
        }

        private fun getDefaultDelayFromConfigInMillis(context: Context) =
                TimeUnit.MINUTES.toMillis(DFRemoteConfig.getConfig(context).downloadInBackgroundRetryTime)

        private fun getMaxRetry(context: Context) =
                DFRemoteConfig.getConfig(context).downloadInBackgroundMaxRetry
    }
}