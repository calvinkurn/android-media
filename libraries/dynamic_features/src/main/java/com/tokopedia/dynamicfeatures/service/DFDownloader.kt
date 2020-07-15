package com.tokopedia.dynamicfeatures.service

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PersistableBundle
import androidx.annotation.RequiresApi
import com.tokopedia.dynamicfeatures.DFInstaller
import com.tokopedia.dynamicfeatures.config.DFRemoteConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * Main Logic for DF Service
 * Function to download DF Module.
 */
object DFDownloader {
    const val SHARED_PREF_NAME = "df_job_srv"
    const val KEY_SHARED_PREF_MODULE = "module_list"
    const val DELIMITER = "#"
    const val DELIMITER_2 = ":"
    const val JOB_ID = 953

    var isServiceRunning = false

    @SuppressLint("NewApi")
    fun startSchedule(context: Context, moduleList: List<String> = emptyList(), isImmediate: Boolean = false) {
        val moduleListToDownload = DFInstaller.getFilteredModuleList(context, moduleList)
        // no changes in module list, so no need to update the queue
        if (moduleListToDownload.isNotEmpty()) {
            DFQueue.combineListAndPut(context, moduleListToDownload)
        }
        if (isServiceRunning) {
            return
        }
        val isAboveM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        if (isAboveM) {
            setScheduleJob(context, isImmediate)
        } else {
            setAlarm(context, isAlarmOn = true, isImmediate = isImmediate)
        }
    }

    private fun setAlarm(context: Context, isAlarmOn: Boolean, isImmediate: Boolean) {
        if (isImmediate && isAlarmOn) {
            context.startService(Intent(context, DFDownloadService::class.java))
            return
        }
        val alarm = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DFBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (isAlarmOn) {
            val delay = getDefaultDelayFromConfigInMillis(context)
            alarm.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), delay, pendingIntent)
        } else {
            alarm.cancel(pendingIntent)
        }
    }

    fun cancelAlarm(context: Context) {
        setAlarm(context, isAlarmOn = false, isImmediate = false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setScheduleJob(context: Context, isImmediate: Boolean) {
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler
            ?: return
        val bundle = PersistableBundle()
        val delay = if (isImmediate) {
            TimeUnit.SECONDS.toMillis(1)
        } else {
            getDefaultDelayFromConfigInMillis(context)
        }
        jobScheduler.schedule(
            JobInfo.Builder(JOB_ID,
                ComponentName(context, DFDownloadJobService::class.java))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setMinimumLatency(delay / 4)
                .setOverrideDeadline(delay)
                .setExtras(bundle)
                .build())
    }

    private fun setServiceFlagFalse() {
        isServiceRunning = false
    }

    private fun getDefaultDelayFromConfigInMillis(context: Context) =
        TimeUnit.MINUTES.toMillis(DFRemoteConfig.getConfig(context).downloadInBackgroundRetryTime)

    suspend fun startJob(applicationContext: Context): Boolean {
        if (isServiceRunning) {
            return true
        }
        isServiceRunning = true
        return withContext(Dispatchers.Default) {
            var startService = false
            var isImmediate = false
            var successResult: Boolean
            try {
                // only download the first module in the list (that is not installed too)
                val moduleToDownloadPair = DFQueue.getDFModuleList(applicationContext).asSequence()
                    .filter { !DFInstaller.isInstalled(applicationContext, it.first) }
                    .take(1)
                    .firstOrNull()
                val moduleToDownload = moduleToDownloadPair?.first ?: ""
                if (moduleToDownload.isEmpty()) {
                    DFQueue.clear(applicationContext)
                    successResult = true
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
                            startService = true
                            isImmediate = immediate
                        }
                        successResult = true
                    } else {
                        if (remainingList.isEmpty()) {
                            successResult = true
                        } else {
                            //to sort the DF, so the most failed will be downloaded later.
                            val remainingListSorted = DFQueue.getDFModuleListSorted(applicationContext)
                            if (remainingListSorted[0].first != remainingList[0].first) {
                                DFQueue.putDFModuleList(applicationContext, remainingListSorted)
                            }
                            startService = true
                            isImmediate = immediate
                            successResult = false
                        }
                    }
                }
            } catch (e: Exception) {
                successResult = false
            } finally {
                setServiceFlagFalse()
                if (startService) {
                    startSchedule(applicationContext, isImmediate = isImmediate)
                }
            }
            return@withContext successResult
        }
    }
}