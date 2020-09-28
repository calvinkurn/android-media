package com.tokopedia.dynamicfeatures.service

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import com.tokopedia.dynamicfeatures.DFInstaller
import com.tokopedia.dynamicfeatures.config.DFRemoteConfig
import com.tokopedia.dynamicfeatures.service.DFDownloadWorker.Companion.DF_DL_WORKER_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Main Logic for DF Service
 * Function to download DF Module.
 */
object DFDownloader {
    const val SHARED_PREF_NAME = "df_job_srv"
    const val KEY_SHARED_PREF_MODULE = "module_list"
    const val DELIMITER = "#"
    const val DELIMITER_2 = ":"

    fun startSchedule(context: Context, moduleList: List<String> = emptyList(),
                      isImmediate: Boolean = false, isAppendingExisting: Boolean) {
        val moduleListToDownload = DFInstaller.getFilteredModuleList(context, moduleList)
        // no changes in module list, so no need to update the queue
        if (moduleListToDownload.isNotEmpty()) {
            DFQueue.combineListAndPut(context, moduleListToDownload)
        }
        val policy = if (isAppendingExisting) {
            ExistingWorkPolicy.APPEND
        } else {
            ExistingWorkPolicy.KEEP
        }
        DFDownloadWorker.scheduleOneTimeWorker(context, isImmediate, policy)
    }

    @SuppressLint("NewApi")
    fun stopService(context: Context) {
        try {
            WorkManager.getInstance(context).cancelUniqueWork(DF_DL_WORKER_NAME)
        } catch (ignored: Exception) {
        }
    }

    suspend fun startJob(applicationContext: Context): ListenableWorker.Result {
        return withContext(Dispatchers.Default) {
            var needRetry = false
            var isImmediate = false
            var successResult: ListenableWorker.Result
            try {
                // only download the first module in the list (that is not installed too)
                val moduleToDownloadPair = DFQueue.getDFModuleList(applicationContext).asSequence()
                        .filter { !DFInstaller.isInstalled(applicationContext, it.first) }
                        .take(1)
                        .firstOrNull()
                val moduleToDownload = moduleToDownloadPair?.first ?: ""
                if (moduleToDownload.isEmpty()) {
                    DFQueue.clear(applicationContext)
                    successResult = ListenableWorker.Result.success()
                } else {
                    val (result, immediate) = DFInstaller.startInstallInBackground(applicationContext, moduleToDownload, onSuccessInstall = {
                        DFQueue.removeModuleFromQueue(applicationContext, listOf(moduleToDownload))
                    }, onFailedInstall = {
                        // loop all combined list
                        // if not installed, flag++
                        // if installed, it will be removed from queue list
                        val successfulListAfterInstall = mutableListOf<String>()
                        val failedListAfterInstall = mutableListOf<Pair<String, Int>>()

                        if (DFInstaller.isInstalled(applicationContext, moduleToDownload)) {
                            successfulListAfterInstall.add(moduleToDownload)
                        } else {
                            failedListAfterInstall.add(Pair(moduleToDownload, (moduleToDownloadPair?.second
                                    ?: 0) + 1))
                        }

                        if (DFRemoteConfig.getConfig(applicationContext).downloadInBackgroundAllowRetry) {
                            //allowRetry will add failed list to queue again
                            DFQueue.updateQueue(applicationContext, failedListAfterInstall, successfulListAfterInstall)
                        } else {
                            // not allow retry will remove both success and failed from queue
                            DFQueue.updateQueue(applicationContext, null, successfulListAfterInstall)
                            DFQueue.updateQueue(applicationContext, null, failedListAfterInstall.map { it.first })
                        }

                    })
                    // retrieve the list again, and start the service to download the next DF in queue
                    val remainingList = DFQueue.getDFModuleList(applicationContext)
                    if (result) {
                        // if previous download is success, will schedule to run immediately
                        // the chance of successful download is bigger.
                        if (remainingList.isNotEmpty()) {
                            needRetry = true
                            isImmediate = immediate
                        }
                    } else {
                        if (remainingList.isEmpty()) {
                            successResult = ListenableWorker.Result.success()
                        } else {
                            //to sort the DF, so the most failed will be downloaded later.
                            val remainingListSorted = DFQueue.getDFModuleListSorted(applicationContext)
                            if (remainingListSorted[0].first != remainingList[0].first) {
                                DFQueue.putDFModuleList(applicationContext, remainingListSorted)
                            }
                            needRetry = true
                            isImmediate = immediate
                        }
                    }
                }
            } catch (e: Exception) {
                successResult = ListenableWorker.Result.retry()
            } finally {
                if (needRetry) {
                    if (isImmediate) {
                        startSchedule(applicationContext, isImmediate = isImmediate, isAppendingExisting = true)
                        successResult = ListenableWorker.Result.success()
                    } else {
                        successResult = ListenableWorker.Result.retry()
                    }
                } else {
                    successResult = ListenableWorker.Result.success()
                }
            }
            return@withContext successResult
        }
    }
}