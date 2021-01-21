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
import java.util.concurrent.TimeUnit
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
        if (runAttemptCount > MAX_RUN_ATTEMPT) {
            return Result.failure()
        }
        return withContext(Dispatchers.IO) {
            var result = Result.success()
            try {
                useCase.setParams(insertDeviceInfoPayloadCreator.create())
                useCase.execute(
                        onSuccess = {
                            result = Result.success()
                            setSuccessSubmitDevice()
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

    private fun setSuccessSubmitDevice() {
        val now = System.currentTimeMillis()
        val sp = appContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        sp.edit().putLong(SHARED_PREF_KEY, now).apply()
        lastSubmit = now
    }

    companion object {
        const val WORKER_NAME = "SUBMIT_DEVICE_WORKER"
        const val MAX_RUN_ATTEMPT = 3
        const val INTERNAL_TIME_DEFAULT = 604_800_000 // 1 week to submit new device data
        const val SHARED_PREF_NAME = "pref_submit_device"
        const val SHARED_PREF_KEY = "ts"
        const val DELAY_WORKER = 5L
        var lastSubmit = 0L

        @JvmStatic
        fun scheduleWorker(context: Context, forceWorker: Boolean) {
            val appContext = context.applicationContext
            try {
                if (forceWorker || needToRunCheckLastSubmit(appContext)) {
                    runWorker(appContext)
                }
            } catch (ex: Exception) {
                Timber.w(ex.toString())
            }
        }

        fun needToRunCheckLastSubmit(context: Context): Boolean {
            if (lastSubmit == 0L) {
                val sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
                lastSubmit = sp.getLong(SHARED_PREF_KEY, 0L)
            }
            val now = System.currentTimeMillis()
            return (now - lastSubmit >= INTERNAL_TIME_DEFAULT)
        }

        private fun runWorker(context: Context) {
            WorkManager.getInstance(context).enqueueUniqueWork(
                    WORKER_NAME,
                    ExistingWorkPolicy.REPLACE,
                    OneTimeWorkRequest
                            .Builder(SubmitDeviceWorker::class.java)
                            .setConstraints(Constraints.Builder()
                                    .setRequiredNetworkType(NetworkType.CONNECTED)
                                    .build())
                            .setInitialDelay(DELAY_WORKER, TimeUnit.SECONDS)
                            .build())
        }
    }

}