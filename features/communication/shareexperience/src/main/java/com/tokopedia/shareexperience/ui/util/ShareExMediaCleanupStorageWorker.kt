package com.tokopedia.shareexperience.ui.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ShareExMediaCleanupStorageWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val filesInCache = context.cacheDir.listFiles()
        if (filesInCache != null) {
            for (file in filesInCache) {
                file.delete()
            }
        }
        return Result.success()
    }

    companion object {
        private const val WORKER_NAME = "SHARE_EXPERIENCE_MEDIA_CLEANER"

        fun scheduleWorker(context: Context) {
            try {
                val worker = OneTimeWorkRequest
                    .Builder(ShareExMediaCleanupStorageWorker::class.java)
                    .setInitialDelay(1, TimeUnit.MILLISECONDS)
                    .build()
                WorkManager.getInstance(context).enqueueUniqueWork(
                    WORKER_NAME,
                    ExistingWorkPolicy.REPLACE,
                    worker
                )
            } catch (ignored: Exception) {
                Timber.d(ignored)
            }
        }
    }
}
