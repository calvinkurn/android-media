package com.tokopedia.kyc_centralized.ui.gotoKyc.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.gojek.kyc.plus.OneKycConstants
import com.gojek.kyc.plus.getKycSdkDocumentDirectoryPath
import com.gojek.kyc.plus.getKycSdkFrameDirectoryPath
import com.gojek.kyc.plus.getKycSdkLogDirectoryPath
import com.tokopedia.utils.file.FileUtil
import java.util.concurrent.TimeUnit

class GotoKycCleanupStorageWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        removeGotoKycImage()
        removeGotoKycPreference()
        return Result.success()
    }

    private fun removeGotoKycPreference() {
        try {
            val preferenceName = inputData.getString(INPUT_PREFERENCE_NAME_TO_CLEAN)
            applicationContext.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).edit().clear().apply()
        } catch (ignored: Exception) {}
    }

    private fun removeGotoKycImage() {
        val directoryToClean1 = inputData.getString(INPUT_DIRECTORY_TO_CLEAN_1)
        val directoryToClean2 = inputData.getString(INPUT_DIRECTORY_TO_CLEAN_2)
        val directoryToClean3 = inputData.getString(INPUT_DIRECTORY_TO_CLEAN_3)
        FileUtil.deleteFolder(directoryToClean1)
        FileUtil.deleteFolder(directoryToClean2)
        FileUtil.deleteFolder(directoryToClean3)
    }

    companion object {
        private const val WORKER_NAME = "GOTO_KYC_STORAGE_CLEANER"
        private const val INPUT_DIRECTORY_TO_CLEAN_1 = "GOTO_KYC_DIR_1"
        private const val INPUT_DIRECTORY_TO_CLEAN_2 = "GOTO_KYC_DIR_2"
        private const val INPUT_DIRECTORY_TO_CLEAN_3 = "GOTO_KYC_DIR_3"
        private const val INPUT_PREFERENCE_NAME_TO_CLEAN = "GOTO_KYC_PREF"
        private const val THRESHOLD_LAST_MODIFICATION_TIME = 86_400_000L //1 day

        fun scheduleWorker(context: Context) {
            try {
                val directory1 = getKycSdkDocumentDirectoryPath(context)
                val directory2 = getKycSdkFrameDirectoryPath(context)
                val directory3 = getKycSdkLogDirectoryPath(context)
                val preferenceName = OneKycConstants.KYC_SDK_PREFERENCE_NAME
                val data = Data.Builder().apply {
                    putString(INPUT_DIRECTORY_TO_CLEAN_1, directory1)
                    putString(INPUT_DIRECTORY_TO_CLEAN_2, directory2)
                    putString(INPUT_DIRECTORY_TO_CLEAN_3, directory3)
                    putString(INPUT_PREFERENCE_NAME_TO_CLEAN, preferenceName)
                }.build()
                val worker = OneTimeWorkRequest
                    .Builder(GotoKycCleanupStorageWorker::class.java)
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

        fun cancelWorker(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORKER_NAME)
        }
    }
}
