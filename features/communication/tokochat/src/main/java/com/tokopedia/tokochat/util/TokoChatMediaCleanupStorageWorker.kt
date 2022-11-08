package com.tokopedia.tokochat.util

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
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
        val directoryToClean = inputData.getString(TOKOCHAT_DIR_KEY)
        FileUtil.deleteFolder(directoryToClean)
        return Result.success()
    }

    companion object {
        private const val WORKER_NAME = "TOKOCHAT_MEDIA_CLEANER"
        private const val TOKOCHAT_DIR_KEY = "TOKOCHAT_DIR"
        private const val THRESHOLD_LAST_MODIFICATION_TIME = 86_400_000L //1 day

        fun scheduleWorker(context: Context, directory: String) {
            try {
                val data = Data.Builder().putString(TOKOCHAT_DIR_KEY, directory).build()
                val worker = OneTimeWorkRequest
                    .Builder(TokoChatMediaCleanupStorageWorker::class.java)
                    .setInputData(data)
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
