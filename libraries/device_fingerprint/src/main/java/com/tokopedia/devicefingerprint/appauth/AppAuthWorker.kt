package com.tokopedia.devicefingerprint.appauth

import android.content.Context
import androidx.work.*
import com.tkpd.util.Base64
import com.tokopedia.config.GlobalConfig
import com.tokopedia.device.info.DeviceInfo
import com.tokopedia.devicefingerprint.appauth.usecase.AppAuthUseCase
import com.tokopedia.devicefingerprint.di.DaggerDeviceFingerprintComponent
import com.tokopedia.devicefingerprint.di.DeviceFingerprintModule
import com.tokopedia.encryption.security.sha256
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AppAuthWorker(val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    @Inject
    lateinit var appAuthUseCase: AppAuthUseCase

    @Inject
    lateinit var userSession: UserSessionInterface

    init {
        DaggerDeviceFingerprintComponent.builder()
                .deviceFingerprintModule(DeviceFingerprintModule(appContext))
                .build()
                .inject(this)
    }

    override suspend fun doWork(): Result {
        isRunning = true
        return withContext(Dispatchers.IO) {
            try {
                val encd = Base64.GetDecoder(appContext)
                val androidId = DeviceInfo.getAndroidId(appContext)
                val adsId = DeviceInfo.getAdsId(appContext)
                val uuid = DeviceInfo.getUUID(appContext)
                val content = (adsId + androidId + uuid + encd + appContext.packageName + GlobalConfig.VERSION_CODE)
                val contentSha = content.sha256()
                appAuthUseCase.setParams(contentSha)
                appAuthUseCase.execute({
                    // success
                    if (it.mutationSignDvc.isSuccess) {
                        setAlreadySuccessSend(appContext, 1)
                    }
                }, {
                    Timber.w(it.toString())
                })
            } catch (e: Exception) {
                Timber.w(e.toString())
            }
            isRunning = false
            Result.success()
        }
    }

    companion object {
        const val WORKER_NAME = "APP_AUTH_WORKER"

        var hasSuccessSendInt = 1 // 1 assumed it already running, means this feature is disabled.
        var PREF = "app_auth"
        var KEY_SUCCESS = "scs"

        var isRunning = false

        private fun alreadySuccessSend(context: Context): Boolean {
            if (hasSuccessSendInt == 0) {
                val sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                hasSuccessSendInt = sp.getInt(KEY_SUCCESS, -1)
            }
            return hasSuccessSendInt == 1
        }

        private fun setAlreadySuccessSend(context: Context, successSend: Int) {
            val sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            sp.edit().putInt(KEY_SUCCESS, successSend).apply()
        }

        fun scheduleWorker(context: Context) {
            if (alreadySuccessSend(context)) {
                return
            }
            if (isRunning) {
                return
            }
            try {
                WorkManager.getInstance(context).enqueueUniqueWork(
                        WORKER_NAME,
                        ExistingWorkPolicy.REPLACE,
                        OneTimeWorkRequest
                                .Builder(AppAuthWorker::class.java)
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