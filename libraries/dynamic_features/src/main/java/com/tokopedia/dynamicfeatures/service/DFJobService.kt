package com.tokopedia.dynamicfeatures.service

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PersistableBundle
import androidx.core.app.JobIntentService
import java.util.concurrent.TimeUnit

/**
 * Created by hendry on 11/12/19.
 */
class DFJobService : JobIntentService() {
    companion object {
        const val JOB_ID = 7122
        const val KEY_MODULE_LIST = "module_list"
        const val DELAY_SERVICE_IN_SECOND = 15L
        const val SHARED_PREF_NAME = "df_job_srv"
        const val KEY_SHARED_PREF_MODULE = "module_list"
        const val DELIMITER = ";"

        var isServiceRunning = false

        private fun getDFModuleList(context: Context):List<String>?{
            try {
                val sp = context.getSharedPreferences(
                    SHARED_PREF_NAME,
                    Context.MODE_PRIVATE
                )
                val moduleList = sp.getString(KEY_SHARED_PREF_MODULE, "")
                return moduleList?.split(DELIMITER)
            } catch (e:Exception) {
                return null
            }
        }

        fun startService(context:Context, moduleListToDownload: List<String>) {
            if (isServiceRunning) {
                return
            }
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // put Delay, so it does not execute immediately
                    val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler
                        ?: return
                    val bundle = PersistableBundle().apply {
                        putString(KEY_MODULE_LIST, moduleListToDownload.joinToString(DELIMITER))
                    }

                    jobScheduler.schedule(JobInfo.Builder(DFJobService.JOB_ID,
                        ComponentName(context, DFJobService::class.java))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setMinimumLatency(TimeUnit.SECONDS.toMillis(DELAY_SERVICE_IN_SECOND))
                        .setExtras(bundle)
                        .build())
                } else {
                    val intent = Intent(context, DFJobService::class.java).apply {
                        putExtra (KEY_MODULE_LIST, moduleListToDownload.joinToString(DELIMITER))
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

        //DO WORK HERE

        isServiceRunning = false
    }

}
