package com.tokopedia.devicefingerprint.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.crashlytics.android.Crashlytics
import com.tokopedia.devicefingerprint.di.DaggerDeviceFingerprintComponent
import com.tokopedia.devicefingerprint.di.DeviceFingerprintModule
import com.tokopedia.devicefingerprint.usecase.SubmitDeviceInfoUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SubmitDeviceInfoService: JobIntentService(), CoroutineScope {

    companion object {
        private const val JOB_ID = 4444

        fun startService(context: Context) {
            try {
                val work = Intent(context, SubmitDeviceInfoService::class.java)
                enqueueWork(context, SubmitDeviceInfoService::class.java, JOB_ID, work)
            } catch (e: Exception) {
                e.printStackTrace()
                logExceptionToCrashlytics(e.fillInStackTrace())
            }
        }

        private fun logExceptionToCrashlytics(t: Throwable) {
            try {
                Crashlytics.logException(t)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    @Inject lateinit var useCase: SubmitDeviceInfoUseCase

    override fun onCreate() {
        super.onCreate()
        DaggerDeviceFingerprintComponent.builder()
                .deviceFingerprintModule(DeviceFingerprintModule(applicationContext))
                .build()
                .inject(this)
    }

    override fun onHandleWork(intent: Intent) {
        launchCatchError(block = {
            useCase.executeOnBackground()
        }, onError = {
            it.printStackTrace()
        })
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

}
