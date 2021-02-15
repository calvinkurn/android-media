package com.tokopedia.devicefingerprint.datavisor.workmanager

import android.content.Context
import android.util.Log
import androidx.work.*
import com.tokopedia.devicefingerprint.datavisor.instance.VisorFingerprintInstance
import com.tokopedia.devicefingerprint.datavisor.payload.DeviceInitPayload
import com.tokopedia.devicefingerprint.datavisor.usecase.SubmitDVTokenUseCase
import com.tokopedia.devicefingerprint.di.DaggerDeviceFingerprintComponent
import com.tokopedia.devicefingerprint.di.DeviceFingerprintModule
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume

class DataVisorWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    @Inject
    lateinit var submitDVTokenUseCase: SubmitDVTokenUseCase

    @Inject
    lateinit var userSession: UserSessionInterface

    init {
        DaggerDeviceFingerprintComponent.builder()
                .deviceFingerprintModule(DeviceFingerprintModule(applicationContext))
                .build()
                .inject(this)
    }

    override suspend fun doWork(): Result {
        if (runAttemptCount > MAX_RUN_ATTEMPT) {
            return Result.failure()
        }
        return withContext(Dispatchers.IO) {
            var result: Result
            try {
                result = suspendCancellableCoroutine { continuation ->
                    try {
                        VisorFingerprintInstance.initToken(applicationContext,
                                listener = object : VisorFingerprintInstance.onVisorInitListener {
                                    override fun onSuccessInitToken(token: String) {
                                        sendDataVisorToServer(token, runAttemptCount, "")
                                        continuation.resume(Result.success())
                                    }

                                    override fun onFailedInitToken(error: String) {
                                        Timber.w(error)
                                        if (isErrorReachMax(runAttemptCount)) sendErrorDataVisorToServer(error)
                                        continuation.resume(Result.retry())
                                    }
                                })
                    } catch (e: Exception) {
                        if (isErrorReachMax(runAttemptCount)) sendErrorDataVisorToServer(e.toString())
                        continuation.resume(Result.retry())
                    }
                }
            } catch (e: Exception) {
                Timber.w(e.toString())
                if (isErrorReachMax(runAttemptCount)) sendErrorDataVisorToServer(e.toString())
                result = Result.retry()
            }
            result
        }
    }

    fun isErrorReachMax(runAttemptCount:Int) = runAttemptCount == MAX_RUN_ATTEMPT

    fun sendErrorDataVisorToServer(errorMessage: String) {
        sendDataVisorToServer(DEFAULT_VALUE_DATAVISOR, MAX_RUN_ATTEMPT, errorMessage)
    }

    fun sendDataVisorToServer(token: String = DEFAULT_VALUE_DATAVISOR, countAttempt: Int, errorMessage: String) {
        submitDVTokenUseCase.setParams(DeviceInitPayload(
                token,
                userSession.userId.toLong(),
                countAttempt,
                errorMessage
        ))
        submitDVTokenUseCase.execute({
            Log.w("POC Fingerprint", "success submit DV" + it)
            Timber.w(it.toString())
        }, {
            Log.w("POC Fingerprint", "error submit DV" + it)
            Timber.w(it.toString())
        })
    }

    companion object {
        const val WORKER_NAME = "DV_WORKER"
        const val MAX_RUN_ATTEMPT = 3
        const val DEFAULT_VALUE_DATAVISOR = "DVLT_6542b775e3263c27e321b929-f52fc6e0_dFlt"

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