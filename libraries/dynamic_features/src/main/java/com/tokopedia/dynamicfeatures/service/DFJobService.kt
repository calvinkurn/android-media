package com.tokopedia.dynamicfeatures.service

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PersistableBundle
import androidx.core.app.JobIntentService
import com.tokopedia.dynamicfeatures.DFInstaller
import java.util.concurrent.TimeUnit

/**
 * Service to retry installation of Dynamic Feature Module in background
 */
class DFJobService : JobIntentService() {
    companion object {
        const val JOB_ID = 7122
        const val KEY_MODULE_LIST = "module_list"
        const val DELAY_SERVICE_IN_SECOND = 15L
        const val SHARED_PREF_NAME = "df_job_srv"
        const val KEY_SHARED_PREF_MODULE = "module_list"
        const val DELIMITER = "#"
        const val DELIMITER_2 = ":"
        const val MAX_ATTEMPT_DOWNLOAD = 3

        var isServiceRunning = false

        fun startService(context:Context, moduleListToDownload: List<String>? = null) {
            if (isServiceRunning) {
                return
            }
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // put Delay, so it does not execute immediately
                    val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler
                        ?: return
                    val bundle = PersistableBundle().apply {
                        putString(KEY_MODULE_LIST, moduleListToDownload?.joinToString(DELIMITER) ?: "")
                    }

                    jobScheduler.schedule(JobInfo.Builder(DFJobService.JOB_ID,
                        ComponentName(context, DFJobService::class.java))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setMinimumLatency(TimeUnit.SECONDS.toMillis(DELAY_SERVICE_IN_SECOND))
                        .setExtras(bundle)
                        .build())
                } else {
                    val intent = Intent(context, DFJobService::class.java).apply {
                        putExtra (KEY_MODULE_LIST, moduleListToDownload?.joinToString(DELIMITER) ?: "")
                    }
                    enqueueWork(context, DFJobService::class.java, JOB_ID, intent)
                }
            } catch (e: Exception) {

            }
        }
    }
    override fun onHandleWork(intent: Intent) {
        if (isServiceRunning) {
            return
        }
        isServiceRunning = true

        //get module list from intent
        val moduleListString = intent.getStringExtra(KEY_MODULE_LIST)
        val moduleList = if (moduleListString.isEmpty()) {
            listOf()
        } else {
            moduleListString.split(DELIMITER)
        }

        // combine module List with the queue
        val queueList = DFMDownloadQueue.getDFModuleList(this)

        val combinedPairList = if (queueList == null || queueList.isEmpty()) {
            moduleList.map { Pair(it, 1) }
        } else {
            combineList(moduleList, queueList)
        }
        DFMDownloadQueue.putDFModuleList(this, combinedPairList)
        val combinedList = combinedPairList.map { it.first }
        if (combinedList.isEmpty()) {
            endService()
            return
        }
        DFInstaller().installOnBackground(application, combinedList, onSuccessInstall = {
            DFMDownloadQueue.putDFModuleList(this, null)
            endService()
        }, onFailedInstall = {
            // loop all combined list
            // if installed, remove from list
            // if not installed, flag++
            val iterator = combinedPairList.iterator();
            val combinedListAfterInstall = mutableListOf<Pair<String, Int>>()
            while (iterator.hasNext()) {
                val moduleNamePair = iterator.next()
                if (DFInstaller.manager?.installedModules?.contains(moduleNamePair.first) != true) {
                    if (moduleNamePair.second < MAX_ATTEMPT_DOWNLOAD) {
                        combinedListAfterInstall.add(Pair(moduleNamePair.first, moduleNamePair.second + 1))
                    }
                }
            }
            if (combinedListAfterInstall.isNotEmpty()) {
                DFMDownloadQueue.putDFModuleList(this, combinedListAfterInstall)
            }
            endService()
        })
    }

    /***
     * @param moduleList module To download, requested directly
     * @param queueList module To download, requested previously and get from the local storage
     * @return combinedList
     */
    private fun combineList(moduleList:List<String>, queueList:List<Pair<String,Int>>): List<Pair<String, Int>> {
        val list =  moduleList.map { Pair(it, 1) }.toMutableList()
        queueList.forEach {
            if (it.second < MAX_ATTEMPT_DOWNLOAD) {
                if (!moduleList.contains(it.first)) {
                    list.add(Pair(it.first, it.second))
                }
            }
        }
        return list
    }

    private fun endService(){
        isServiceRunning = false
    }

}
