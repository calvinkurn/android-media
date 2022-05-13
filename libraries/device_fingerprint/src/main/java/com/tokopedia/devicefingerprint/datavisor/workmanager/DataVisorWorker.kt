package com.tokopedia.devicefingerprint.datavisor.workmanager

import android.app.Activity
import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import com.tokopedia.config.GlobalConfig
import com.tokopedia.devicefingerprint.datavisor.repository.DataVisorSharedPreferences
import com.tokopedia.devicefingerprint.di.DaggerDeviceFingerprintComponent
import com.tokopedia.devicefingerprint.di.DeviceFingerprintModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DataVisorWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    @Inject
    lateinit var dataVisor: DataVisor

    init {
        DaggerDeviceFingerprintComponent.builder()
            .deviceFingerprintModule(DeviceFingerprintModule(applicationContext))
            .build()
            .inject(this)
    }

    override suspend fun doWork(): Result {
        val activityName = inputData.getString(ACTIVITY_NAME) ?: ""
        val isFromLogin = inputData.getBoolean(IS_FROM_LOGIN, false)
        return dataVisor.doWork(activityName, isFromLogin)
    }

    companion object {
        private const val WORKER_NAME = "DV_WORKER"
        private const val ACTIVITY_NAME = "DV_WORKER_ACTIVITY_NAME"
        private const val IS_FROM_LOGIN = "DV_WORKER_IS_FROM_LOGIN"

        fun scheduleWorker(activity: Activity, isFromLogin: Boolean = false) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val appContext = activity.applicationContext
                    val activityName = activity::class.java.simpleName
                    if (GlobalConfig.isSellerApp()) {
                        return@launch
                    }
                    runWorker(appContext, activityName, isFromLogin)
                } catch (ignored: Exception) {
                }
            }
        }

        private fun runWorker(context: Context, activityName: String, isFromLogin: Boolean) {
            try {
                WorkManager.getInstance(context).enqueueUniqueWork(
                    WORKER_NAME,
                    ExistingWorkPolicy.KEEP,
                    workRequest(activityName, isFromLogin)
                )
            } catch (ex: Exception) {
                Timber.w(ex.toString())
            }
        }

        private fun workRequest(activityName: String, isFromLogin: Boolean): OneTimeWorkRequest =
            OneTimeWorkRequest
                .Builder(DataVisorWorker::class.java)
                .setConstraints(constraints())
                .setInputData(data(activityName, isFromLogin))
                .build()

        private fun constraints(): Constraints =
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        private fun data(activityName: String, isFromLogin: Boolean): Data =
            Data.Builder()
                .putString(ACTIVITY_NAME, activityName)
                .putBoolean(IS_FROM_LOGIN, isFromLogin)
                .build()
    }
}