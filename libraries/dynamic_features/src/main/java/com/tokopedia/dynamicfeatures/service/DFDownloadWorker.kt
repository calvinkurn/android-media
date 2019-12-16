package com.tokopedia.dynamicfeatures.service

import android.content.Context
import androidx.work.*
import com.tokopedia.dynamicfeatures.DFInstaller
import com.tokopedia.dynamicfeatures.service.DFDownloadQueue.combineListAndPut
import com.tokopedia.dynamicfeatures.service.DFServiceConstant.INITIAL_DELAY_DURATION_IN_SECOND
import com.tokopedia.dynamicfeatures.service.DFServiceConstant.isServiceRunning
import java.util.concurrent.TimeUnit

class DFDownloadWorker(appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {

    companion object {
        fun start(context: Context, moduleListToDownload: List<String>? = null) {
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
            WorkManager.getInstance(context.applicationContext).enqueue(uploadWorkRequest)
        }
    }

    private fun setFlagServiceFalse() {
        isServiceRunning = false
    }

    override suspend fun doWork(): Result {
        if (isServiceRunning) {
            return Result.success()
        }
        isServiceRunning = true

        val combinedPairList = DFDownloadQueue.getDFModuleList(applicationContext)
        val combinedList = combinedPairList.map { it.first }
        if (combinedList.isEmpty()) {
            DFDownloadQueue.clear(applicationContext)
            setFlagServiceFalse()
            return Result.success()
        }
        val result = DFInstaller().installOnBackgroundDefer(applicationContext, combinedList, onSuccessInstall = {
            DFDownloadQueue.removeModuleFromQueue(applicationContext, combinedPairList)
            setFlagServiceFalse()
        }, onFailedInstall = {
            // loop all combined list
            // if not installed, flag++
            // if installed, it will be removed from queue list
            val successfulListAfterInstall = mutableListOf<Pair<String, Int>>()
            val combinedListAfterInstall = mutableListOf<Pair<String, Int>>()

            for (moduleNamePair in combinedPairList) {
                if (DFInstaller.manager?.installedModules?.contains(moduleNamePair.first) != true) {
                    combinedListAfterInstall.add(Pair(moduleNamePair.first, moduleNamePair.second + 1))
                } else {
                    successfulListAfterInstall.add(Pair(moduleNamePair.first, 1))
                }
            }
            DFDownloadQueue.updateQueue(applicationContext, combinedListAfterInstall, successfulListAfterInstall)
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