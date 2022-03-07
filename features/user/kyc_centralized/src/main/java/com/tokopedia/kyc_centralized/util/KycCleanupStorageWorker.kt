package com.tokopedia.kyc_centralized.util

import android.content.Context
import androidx.work.*
import com.tokopedia.utils.file.FileUtil
import java.util.concurrent.TimeUnit

/**
 * Schedule worker to delete KYC folder the next day (delay 1 day)
 * Case : User kill the app, onDestroy is not called
 */

class KycCleanupStorageWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val directoryToClean = inputData.getString(INPUT_DIRECTORY_TO_CLEAN)
        FileUtil.deleteFolder(directoryToClean)
        return Result.success()
    }

    companion object {
        private const val WORKER_NAME = "KYC_STORAGE_CLEANER"
        private const val INPUT_DIRECTORY_TO_CLEAN = "KYC_DIR"
        private const val THRESHOLD_LAST_MODIFICATION_TIME = 86_400_000L //1 day

        fun scheduleWorker(context: Context, directory: String) {
            try {
                val data = Data.Builder().putString(INPUT_DIRECTORY_TO_CLEAN, directory).build()
                val worker = OneTimeWorkRequest
                    .Builder(KycCleanupStorageWorker::class.java)
                    .setInputData(data)
                    .setInitialDelay(THRESHOLD_LAST_MODIFICATION_TIME, TimeUnit.MILLISECONDS)
                    .build()
                WorkManager.getInstance(context).enqueueUniqueWork(
                    WORKER_NAME,
                    ExistingWorkPolicy.REPLACE,
                    worker
                )
            } catch (ignored: Exception) {
            }
        }
    }
}