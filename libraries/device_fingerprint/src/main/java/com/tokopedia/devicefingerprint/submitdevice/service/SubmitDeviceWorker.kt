package com.tokopedia.devicefingerprint.submitdevice.service

import android.content.Context
import androidx.work.*
import com.tokopedia.devicefingerprint.di.DaggerDeviceFingerprintComponent
import com.tokopedia.devicefingerprint.di.DeviceFingerprintModule
import com.tokopedia.devicefingerprint.submitdevice.usecase.SubmitDeviceInfoUseCase
import com.tokopedia.devicefingerprint.submitdevice.utils.InsertDeviceInfoPayloadCreator
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class SubmitDeviceWorker(val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    @Inject
    lateinit var useCase: SubmitDeviceInfoUseCase
    @Inject
    lateinit var insertDeviceInfoPayloadCreator: InsertDeviceInfoPayloadCreator

    @Inject
    lateinit var userSession: UserSessionInterface

    init {
        DaggerDeviceFingerprintComponent.builder()
                .deviceFingerprintModule(DeviceFingerprintModule(appContext))
                .build()
                .inject(this)
    }

    override suspend fun doWork(): Result {
        if (runAttemptCount > 3) {
            return Result.failure()
        }
        return withContext(Dispatchers.IO) {
            var result = Result.success()
            try {
                useCase.setParams(insertDeviceInfoPayloadCreator.create())
                useCase.execute(
                        onSuccess = {
                            result = Result.success()
                        },
                        onError = {
                            result = Result.retry()
                        }
                )
            } catch (e: Exception) {
                result = Result.retry()
            }
            return@withContext result
        }
    }

    companion object {
        const val WORKER_NAME = "SUBMIT_DEVICE_WORKER"

        fun scheduleWorker(context: Context) {
            try {
                WorkManager.getInstance(context).enqueueUniqueWork(
                        WORKER_NAME,
                        ExistingWorkPolicy.REPLACE,
                        OneTimeWorkRequest
                                .Builder(SubmitDeviceWorker::class.java)
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