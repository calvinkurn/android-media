package com.tokopedia.devicefingerprint.datavisor.workmanager

import android.content.Context
import android.util.Log
import androidx.work.*
import com.tokopedia.devicefingerprint.datavisor.instance.VisorFingerprintInstance
import com.tokopedia.devicefingerprint.datavisor.usecase.SubmitDVTokenUseCase
import com.tokopedia.devicefingerprint.di.DaggerDeviceFingerprintComponent
import com.tokopedia.devicefingerprint.di.DeviceFingerprintModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DataVisorWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    @Inject
    lateinit var submitDVTokenUseCase: SubmitDVTokenUseCase

    init {
        DaggerDeviceFingerprintComponent.builder()
                .deviceFingerprintModule(DeviceFingerprintModule(applicationContext))
                .build()
                .inject(this)
    }
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                VisorFingerprintInstance.initToken(applicationContext,
                        listener = object : VisorFingerprintInstance.onVisorInitListener {
                    override fun onSuccessInitToken(token: String) {
                        Log.w("POC Fingerprint", "success get Data DV" + token)
                        submitDVTokenUseCase.setParams(token = token)
                        submitDVTokenUseCase.execute({
                            Log.w("POC Fingerprint", "success submit DV" + it)
                            Timber.w(it.toString())
                        },{
                            Log.w("POC Fingerprint", "error submit DV" + it)
                            Timber.w(it.toString())
                        })
                    }

                    override fun onFailedInitToken(error: String) {
                        Log.w("POC Fingerprint", "failed init token DV" + error)
                        Timber.w(error)
                    }
                })
            } catch (e: Exception) {
                Log.w("POC Fingerprint", "error init Token DV" + e)
                Timber.w(e.toString())
            }
            Result.success()
        }
    }

    companion object {
        const val WORKER_NAME = "DV_WORKER"
        fun scheduleWorker(context: Context) {
            try {
                WorkManager.getInstance(context).enqueueUniqueWork(
                        WORKER_NAME,
                        ExistingWorkPolicy.REPLACE,
                        OneTimeWorkRequest
                                .Builder(DataVisorWorker::class.java)
                                .setConstraints(Constraints.Builder()
                                        .setRequiredNetworkType(NetworkType.CONNECTED)
                                        .build())
                                .build())
            } catch (ex: Exception) {
                Timber.w(ex.toString())
            }
        }
    }
}