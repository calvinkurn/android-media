package com.tokopedia.utils.file.cleaner

import android.content.Context
import androidx.work.*
import com.tokopedia.utils.file.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Work Manager to clean up File in fileDir or cache Dir
// only clean up files that at least 1 week old (defined at THRES_LAST_MODIF_FILE_TO_DELETE)
class InternalStorageWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.Default) {
            try {
                val directoryToClean = inputData.getString(INPUT_DIRECTORY_TO_CLEAN)
                val dirToClean = FileUtil.getTokopediaInternalDirectory(directoryToClean)
                val files = dirToClean.listFiles()
                val now = System.currentTimeMillis()
                if (files != null) {
                    for (file in files) {
                        if (now - file.lastModified() >= THRES_LAST_MODIF_FILE_TO_DELETE) {
                            file.delete()
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Result.success()
        }
    }

    companion object {
        const val WORKER_NAME = "STORAGE_CLEANER"
        const val INPUT_DIRECTORY_TO_CLEAN = "INPUT_DIR"
        const val THRES_LAST_MODIF_FILE_TO_DELETE = 604_800_000L // 1 week

        fun scheduleWorker(context: Context, directory: String) {
            try {
                val data = Data.Builder().putString(INPUT_DIRECTORY_TO_CLEAN, directory).build()
                val worker = OneTimeWorkRequest
                        .Builder(InternalStorageWorker::class.java)
                        .setInputData(data)
                        .build()
                WorkManager.getInstance(context).enqueueUniqueWork(
                        WORKER_NAME,
                        ExistingWorkPolicy.REPLACE,
                        worker)
            } catch (ex: Exception) {

            }
        }
    }
}