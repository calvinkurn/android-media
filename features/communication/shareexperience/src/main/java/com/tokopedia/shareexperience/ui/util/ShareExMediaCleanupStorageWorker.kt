package com.tokopedia.shareexperience.ui.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.tokopedia.shareexperience.domain.util.ShareExLogger
import com.tokopedia.user.session.UserSession
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit

class ShareExMediaCleanupStorageWorker(private val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val shareFolder = File("${context.cacheDir}/share")
        return if (!shareFolder.exists() || !shareFolder.isDirectory) {
            Result.success()
        } else {
            val filesInCache = shareFolder.listFiles()
            if (filesInCache != null) {
                val currentTime = System.currentTimeMillis()
                val oneHourInMillis = 60 * 60 * 1000 // 1 hour in milliseconds
                for (file in filesInCache) {
                    val lastModified = file.lastModified()
                    if ((currentTime - lastModified) > oneHourInMillis) {
                        file.delete()
                    }
                }
            }
            Result.success()
        }
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
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                ShareExLogger.logExceptionToServerLogger(
                    throwable = throwable,
                    deviceId = UserSession(context).deviceId,
                    description = ::ShareExMediaCleanupStorageWorker.name
                )
            }
        }
    }
}
