package com.tokopedia.tokochat.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.tokopedia.utils.file.FileUtil
import java.util.concurrent.TimeUnit

/**
 * Schedule worker to delete media after 24 hours
 */
class TokoChatMediaCleanupStorageWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val directoryToClean = TokoChatViewUtil.getInternalCacheDirectory().absolutePath
        FileUtil.deleteFolder(directoryToClean)
        return Result.success()
    }

    companion object {
        private const val WORKER_NAME = "TOKOCHAT_MEDIA_CLEANER"
        private const val THRESHOLD_LAST_MODIFICATION_TIME = 86_400_000L //1 day

        fun scheduleWorker(context: Context) {
            try {
                val worker = OneTimeWorkRequest
                    .Builder(TokoChatMediaCleanupStorageWorker::class.java)
                    .setInitialDelay(THRESHOLD_LAST_MODIFICATION_TIME, TimeUnit.MILLISECONDS)
                    .build()
                WorkManager.getInstance(context).enqueueUniqueWork(
                    WORKER_NAME,
                    ExistingWorkPolicy.REPLACE,
                    worker
                )
            } catch (ignored: Exception) {}
        }
    }
}
