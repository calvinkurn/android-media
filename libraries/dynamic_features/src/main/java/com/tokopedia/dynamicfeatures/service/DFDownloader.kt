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
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

object DFDownloader {
    const val SHARED_PREF_NAME = "df_job_srv"
    const val KEY_SHARED_PREF_MODULE = "module_list"
    const val DELIMITER = "#"
    const val DELIMITER_2 = ":"
    const val MAX_ATTEMPT_DOWNLOAD = 3
    const val DEFAULT_DELAY = 10L
    const val JOB_ID = 953

    var isServiceRunning = false

    private const val REMOTE_CONFIG_ALLOW_RETRY = "android_retry_df_download_bg"
    private const val REMOTE_CONFIG_DELAY_IN_MINUTE = "android_retry_df_download_bg_delay"

    private var defaultDelay = -1L

    @SuppressLint("NewApi")
    fun startSchedule(context: Context, moduleListToDownload: List<String>? = null, isImmediate:Boolean = false) {
        if (!allowRetryFromConfig(context)) {
            return
        }
        // no changes in module list, so no need to update the queue
        if (moduleListToDownload?.isNotEmpty() == true) {
            DFQueue.combineListAndPut(context, moduleListToDownload)
        }
        if (isServiceRunning) {
            return
        }
        val isAboveM = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
        if (isAboveM) {
            setScheduleJob(context, isImmediate)
        } else {
            setAlarm(context, isImmediate = isImmediate)
        }
    }

    private fun setAlarm(context: Context, isAlarmOn: Boolean = true, isImmediate: Boolean = false) {
        if (isImmediate) {
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

    fun cancelAlarm(context: Context){
        setAlarm(context, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setScheduleJob(context: Context, isImmediate: Boolean) {
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler
            ?: return
        val bundle = PersistableBundle()
        val delay = if (isImmediate) {
            TimeUnit.SECONDS.toMillis(5)
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

    private fun allowRetryFromConfig(context: Context): Boolean {
        val firebaseRemoteConfig = FirebaseRemoteConfigImpl(context)
        return firebaseRemoteConfig.getBoolean(REMOTE_CONFIG_ALLOW_RETRY, false)
    }

    private fun getDefaultDelayFromConfigInMillis(context: Context): Long {
        return if (defaultDelay < 0) {
            val firebaseRemoteConfig = FirebaseRemoteConfigImpl(context)
            TimeUnit.MINUTES.toMillis(firebaseRemoteConfig.getLong(REMOTE_CONFIG_DELAY_IN_MINUTE, DEFAULT_DELAY))
        } else {
            defaultDelay
        }
    }

    suspend fun startJob(applicationContext: Context): Boolean {
        return withContext(Dispatchers.Default) {
            if (!allowRetryFromConfig(applicationContext)) {
                return@withContext true
            }
            if (isServiceRunning) {
                return@withContext true
            }
            isServiceRunning = true

            // only download the first module in the list
            val moduleToDownloadPair = DFQueue.getDFModuleList(applicationContext).firstOrNull()
            val moduleToDownload = moduleToDownloadPair?.first ?: ""
            if (moduleToDownload.isEmpty()) {
                DFQueue.clear(applicationContext)
                setServiceFlagFalse()
                return@withContext true
            }
            val result = DFInstaller().installOnBackgroundDefer(applicationContext, listOf(moduleToDownload), onSuccessInstall = {
                DFQueue.removeModuleFromQueue(applicationContext, listOf(moduleToDownload))
                setServiceFlagFalse()
            }, onFailedInstall = {
                // loop all combined list
                // if not installed, flag++
                // if installed, it will be removed from queue list
                val successfulListAfterInstall = mutableListOf<String>()
                val failedListAfterInstall = mutableListOf<Pair<String, Int>>()

                if (DFInstaller.manager?.installedModules?.contains(moduleToDownload) != true) {
                    // the reason all failed is because this module is failed
                    failedListAfterInstall.add(Pair(moduleToDownload, (moduleToDownloadPair?.second ?: 0) + 1))
                } else {
                    successfulListAfterInstall.add(moduleToDownload)
                }
                DFQueue.updateQueue(applicationContext, failedListAfterInstall, successfulListAfterInstall)
                setServiceFlagFalse()
            }, isInitial = false)
            // retrieve the list again, and start the service to download the next DF in queue
            val remainingList = DFQueue.getDFModuleList(applicationContext)
            if (result) {
                // if previous download is success, will schedule to run immediately
                // the chance of successfull download is bigger.
                if (remainingList.isNotEmpty()) {
                    // this is to run immediately
                    startSchedule(applicationContext, isImmediate = true)
                }
                return@withContext true
            } else {
                if (remainingList.isEmpty()) {
                    return@withContext true
                } else {
                    //to sort the DF, so the most failed will be downloaded later.
                    val remainingListSorted = DFQueue.getDFModuleListSorted(applicationContext)
                    if (remainingListSorted[0].first != remainingList[0].first) {
                        DFQueue.putDFModuleList(applicationContext, remainingListSorted)
                    }
                    startSchedule(applicationContext, isImmediate = false)
                    return@withContext false
                }
            }
        }
    }
}