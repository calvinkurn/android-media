package com.tokopedia.dynamicfeatures.service

import android.content.Context
import androidx.work.*
import com.tokopedia.dynamicfeatures.DFInstaller
import com.tokopedia.dynamicfeatures.service.DFDownloadQueue.combineListAndPut
import com.tokopedia.dynamicfeatures.service.DFServiceConstant.INITIAL_DELAY_DURATION_IN_SECOND
import com.tokopedia.dynamicfeatures.service.DFServiceConstant.isServiceRunning
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import java.util.concurrent.TimeUnit

class DFDownloadWorker(appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val WORKER_NAME = "DF Download Worker"
        private const val REMOTE_CONFIG_ALLOW_RETRY = "android_retry_df_download_bg"
        fun start(context: Context, moduleListToDownload: List<String>? = null) {
            if (!allowRetryFromConfig(context)) {
                return
            }
            combineListAndPut(context, moduleListToDownload)
            if (isServiceRunning) {
                return
            }
            val delayDuration = INITIAL_DELAY_DURATION_IN_SECOND
            val uploadWorkRequest = OneTimeWorkRequest.Builder(DFDownloadWorker::class.java)
                .setInitialDelay(delayDuration, TimeUnit.SECONDS)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    delayDuration,
                    TimeUnit.MILLISECONDS)
                .setConstraints(
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .build()
            WorkManager.getInstance(context.applicationContext).enqueueUniqueWork(WORKER_NAME, ExistingWorkPolicy.KEEP, uploadWorkRequest)
        }

        fun allowRetryFromConfig(context: Context): Boolean {
            val firebaseRemoteConfig = FirebaseRemoteConfigImpl(context)
            return firebaseRemoteConfig.getBoolean(REMOTE_CONFIG_ALLOW_RETRY, false)
        }
    }

    private fun setFlagServiceFalse() {
        isServiceRunning = false
    }

    override suspend fun doWork(): Result {
        if (!allowRetryFromConfig(applicationContext)) {
            return Result.success()
        }
        if (isServiceRunning) {
            return Result.success()
        }
        isServiceRunning = true

        val moduleToDownloadPairList = DFDownloadQueue.getDFModuleList(applicationContext)
        val moduleToDownloadList = moduleToDownloadPairList.map { it.first }
        if (moduleToDownloadList.isEmpty()) {
            DFDownloadQueue.clear(applicationContext)
            setFlagServiceFalse()
            return Result.success()
        }
        val result = DFInstaller().installOnBackgroundDefer(applicationContext, moduleToDownloadList, onSuccessInstall = {
            DFDownloadQueue.removeModuleFromQueue(applicationContext, moduleToDownloadPairList)
            setFlagServiceFalse()
        }, onFailedInstall = {
            // loop all combined list
            // if not installed, flag++
            // if installed, it will be removed from queue list
            val successfulListAfterInstall = mutableListOf<Pair<String, Int>>()
            val failedListAfterInstall = mutableListOf<Pair<String, Int>>()

            for (moduleNamePair in moduleToDownloadPairList) {
                if (DFInstaller.manager?.installedModules?.contains(moduleNamePair.first) != true) {
                    failedListAfterInstall.add(Pair(moduleNamePair.first, moduleNamePair.second + 1))
                } else {
                    successfulListAfterInstall.add(Pair(moduleNamePair.first, 1))
                }
            }
            DFDownloadQueue.updateQueue(applicationContext, failedListAfterInstall, successfulListAfterInstall)
            setFlagServiceFalse()
        } , isInitial = false)
        val remainingList = DFDownloadQueue.getDFModuleList(applicationContext)
        return if (result) {
            if (remainingList.isNotEmpty()) {
                DFInstaller().installOnBackground(applicationContext, remainingList.map { it.first })
            }
            Result.success()
        } else {
            if (remainingList.isEmpty()) {
                Result.success()
            } else {
                Result.retry()
            }
        }
    }
}