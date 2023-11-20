package com.scp.auth.authentication

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import kotlinx.coroutines.delay

class GlobalToasterWorker(val appContext: Context, val params: WorkerParameters) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        // Wait 1 second before send the broadcast to make sure the activity is fully launched.
        delay(1000L)
        LocalBroadcastManager.getInstance(appContext).sendBroadcast(Intent(BaseActivity.SUCCESS_REGISTER_TOSTER).apply {
            putExtra(BaseActivity.SUCCESS_REGISTER_TOSTER, inputData.getString(PARAM_MESSAGE))
        })
        return Result.success()
    }

    companion object {
        private const val WORKER_NAME = "GLOBAL_TOASTER_WORKER"
        private const val PARAM_MESSAGE = "param_msg"
        @JvmStatic
        fun scheduleWorker(context: Context, msg: String) {
            try {
                val data = Data.Builder().apply { putString(PARAM_MESSAGE, msg) }.build()
                val periodicWorker = OneTimeWorkRequest
                    .Builder(GlobalToasterWorker::class.java)
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )
                    .setInputData(data)
                    .build()
                WorkManager.getInstance(context).enqueueUniqueWork(
                    WORKER_NAME,
                    ExistingWorkPolicy.REPLACE,
                    periodicWorker
                )
            } catch (ex: Exception) { }
        }
    }
}
