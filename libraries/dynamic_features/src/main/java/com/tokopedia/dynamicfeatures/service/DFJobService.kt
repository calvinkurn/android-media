package com.tokopedia.dynamicfeatures.service

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
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
        const val DELAY_SERVICE_IN_SECOND = 60L
        const val SHARED_PREF_NAME = "df_job_srv"
        const val KEY_SHARED_PREF_MODULE = "module_list"
        const val DELIMITER = "#"
        const val DELIMITER_2 = ":"
        const val MAX_ATTEMPT_DOWNLOAD = 3
        val DELAY_IN_MILIS = TimeUnit.SECONDS.toMillis(DELAY_SERVICE_IN_SECOND)
        lateinit var dfGlobalHandler:Handler
        lateinit var dfGlobalRunnable:Runnable

        var isServiceRunning = false

        fun startService(context:Context, moduleListToDownload: List<String>? = null) {
            getCombineListAndPut(context, moduleListToDownload)
            if (isServiceRunning) {
                return
            }
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // put Delay, so it does not execute immediately
                    val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler
                        ?: return
                    val bundle = PersistableBundle()

                    jobScheduler.schedule(JobInfo.Builder(DFJobService.JOB_ID,
                        ComponentName(context, DFJobService::class.java))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setMinimumLatency(TimeUnit.SECONDS.toMillis(DELAY_SERVICE_IN_SECOND))
                        .setExtras(bundle)
                        .build())
                } else {
                    //it is efficient to run using Handler, rather than using AlarmManager,
                    // this only happen when app is active
                    if (!::dfGlobalHandler.isInitialized) {
                        dfGlobalHandler = Handler()
                    }
                    if (::dfGlobalRunnable.isInitialized) {
                        dfGlobalHandler.removeCallbacks(dfGlobalRunnable)
                    }
                    dfGlobalRunnable = Runnable {
                        enqueueWork(context, DFJobService::class.java, JOB_ID,
                            Intent(context, DFJobService::class.java))
                    }
                    dfGlobalHandler.postDelayed(dfGlobalRunnable, DELAY_IN_MILIS)
                }
            } catch (ignored: Exception) { }
        }

        /***
         * @param moduleList module To download, requested directly
         * @return combinedList
         *
         * moduleList = module_seller, module_travel
         * output: module_seller, 1; module_travel, 1; module_digital,2
         */
        private fun getCombineListAndPut(context:Context, moduleList:List<String>?): List<Pair<String, Int>> {
            val queueList = DFMDownloadQueue.getDFModuleList(context)
            val list = if (queueList.isEmpty()) {
                moduleList?.map { Pair(it, 1) } ?: emptyList()
            } else {
                combineList(moduleList, queueList)
            }
            DFMDownloadQueue.putDFModuleList(context, list)
            return list
        }

        /***
         * @param moduleList module To download, requested directly
         * @param queueList module To download, requested previously and get from the local storage
         * @return combinedList
         *
         * moduleList = module_seller, module_travel
         * queueList = module_seller,2 ; module_digital,2
         * output: module_seller, 1; module_travel, 1; module_digital,2
         */
        private fun combineList(moduleList:List<String>?, queueList:List<Pair<String,Int>>): List<Pair<String, Int>> {
            val list =  moduleList?.map { Pair(it, 1) }?.toMutableList() ?: mutableListOf()
            queueList.forEach {
                if (it.second < MAX_ATTEMPT_DOWNLOAD) {
                    if (moduleList?.contains(it.first) != true) {
                        list.add(Pair(it.first, it.second))
                    }
                }
            }
            return list
        }
    }
    override fun onHandleWork(intent: Intent) {
        if (isServiceRunning) {
            return
        }
        isServiceRunning = true

        val combinedPairList = DFMDownloadQueue.getDFModuleList(this)
        val combinedList = combinedPairList.map { it.first }
        if (combinedList.isEmpty()) {
            endService()
            return
        }
        DFInstaller().installOnBackground(application, combinedList, onSuccessInstall = {
            DFMDownloadQueue.clearDFModuleList(this, combinedPairList)
            endService()
        }, onFailedInstall = {
            // loop all combined list
            // if not installed, flag++
            // if installed, it will be removed from queue list
            val iterator = combinedPairList.iterator()
            val successfulListAfterInstall = mutableListOf<Pair<String, Int>>()
            val combinedListAfterInstall = mutableListOf<Pair<String, Int>>()
            while (iterator.hasNext()) {
                val moduleNamePair = iterator.next()
                if (DFInstaller.manager?.installedModules?.contains(moduleNamePair.first) != true) {
                    if (moduleNamePair.second < MAX_ATTEMPT_DOWNLOAD) {
                        combinedListAfterInstall.add(Pair(moduleNamePair.first, moduleNamePair.second + 1))
                    }
                } else {
                    successfulListAfterInstall.add(Pair(moduleNamePair.first, 1))
                }
            }
            if (combinedListAfterInstall.isNotEmpty()) {
                DFMDownloadQueue.appendDFModuleList(this, combinedListAfterInstall)
            }
            if (successfulListAfterInstall.isNotEmpty()) {
                DFMDownloadQueue.clearDFModuleList(this, successfulListAfterInstall)
            }
            endService()
        })
    }

    private fun endService(){
        isServiceRunning = false
    }

}
