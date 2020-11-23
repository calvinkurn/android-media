package com.tokopedia.devicefingerprint.crysp.workmanager

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.*
import com.crysp.sdk.CryspNetworkManager
import com.tokopedia.devicefingerprint.crysp.CryspInstance
import com.tokopedia.devicefingerprint.crysp.CryspInstance.Companion.sendToken
import com.tokopedia.devicefingerprint.crysp.usecase.SubmitCrTokenUseCase
import com.tokopedia.devicefingerprint.di.DaggerDeviceFingerprintComponent
import com.tokopedia.devicefingerprint.di.DeviceFingerprintModule
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CryspWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    @Inject
    lateinit var submitCrTokenUseCase: SubmitCrTokenUseCase

    @Inject
    lateinit var userSession: UserSession

    init {
        DaggerDeviceFingerprintComponent.builder()
                .deviceFingerprintModule(DeviceFingerprintModule(applicationContext))
                .build()
                .inject(this)
    }

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                sendToken(applicationContext, userSession.userId, onSuccess = { it ->
                    Log.w("POC Fingerprint", "success get Data Crysp" + it)
                    submitCrTokenUseCase.setParams(content = it)
                    submitCrTokenUseCase.execute({
                        Log.w("POC Fingerprint", "success submit Crysp" + it.toString())
                    }, {
                        Log.w("POC Fingerprint", "onError submit Crysp" + it.toString())
                    })
                }, onError = {
                    Log.w("POC Fingerprint", "onError Crysp" + it)
                })
            } catch (e: Exception) {
                Log.w("POC Fingerprint", "exception $e")
            }
            Result.success()
        }
    }

    companion object {
        const val WORKER_NAME = "CRYSP_WORKER"

        fun scheduleWorker(context: Context) {
            try {
                WorkManager.getInstance(context).enqueueUniqueWork(
                        WORKER_NAME,
                        ExistingWorkPolicy.REPLACE,
                        OneTimeWorkRequest
                                .Builder(CryspWorker::class.java)
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