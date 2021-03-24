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
import com.tokopedia.user.session.UserSession
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
        if (isRunning) {
            return Result.failure()
        }
        isRunning = true
        return withContext(Dispatchers.IO) {
            try {
                val encd = Base64.GetDecoder(appContext).trim()
                val androidId = DeviceInfo.getAndroidId(appContext).trim()
                val adsId = DeviceInfo.getAdsId(appContext).trim()
                val uuid = DeviceInfo.getUUID(appContext).trim()
                val content = (adsId + androidId + uuid + encd + appContext.packageName)
                val contentSha = content.sha256()
                appAuthUseCase.setParams(contentSha)
                val objResult = appAuthUseCase.executeOnBackground()
                if (objResult.mutationSignDvc.isSuccess) {
                    setAlreadySuccessSend(appContext, 1)
                }
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
        var userSession: UserSessionInterface? = null

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
            hasSuccessSendInt = successSend
        }

        fun getUserSession(context: Context): UserSessionInterface {
            if (userSession == null) {
                userSession = UserSession(context.applicationContext)
            }
            return userSession!!
        }

        /**
         * isForce = false means it will check if already success sent before.
         * If previously already success, this will not schedule
         * isForce = true will force the scheduler, even if the previous worker was success.
         */
        fun scheduleWorker(context: Context, isForce:Boolean) {
            if (GlobalConfig.isSellerApp()) {
                return
            }
            if (!isForce && alreadySuccessSend(context)) {
                return
            }
            if (isRunning) {
                return
            }
            val userSession = getUserSession(context)
            if (!userSession.isLoggedIn) {
                return
            }
            try {
                WorkManager.getInstance(context.applicationContext).enqueueUniqueWork(
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