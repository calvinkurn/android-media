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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AppAuthWorker(val appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    @Inject
    lateinit var appAuthUseCase: AppAuthUseCase

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
        if (runAttemptCount > MAX_RUN_ATTEMPT) {
            return Result.failure()
        }
        return withContext(Dispatchers.IO) {
            val result = try {
                val userSession = getUserSession(appContext)
                if (userSession.userId.isEmpty()) {
                    Result.success()
                } else {
                    val encd = Base64.GetDecoder(appContext).trim()
                    val androidId = DeviceInfo.getAndroidId(appContext).trim()
                    val adsId = DeviceInfo.getAdsId(appContext).trim()
                    val uuid = DeviceInfo.getUUID(appContext).trim()
                    val content = (adsId + androidId + uuid + encd + appContext.packageName)
                    val contentSha = content.sha256()
                    val objResult = appAuthUseCase.execute(contentSha)
                    if (objResult.mutationSignDvc.isSuccess) {
                        setAlreadySuccessSend(appContext, 1)
                        Result.success()
                    } else {
                        Result.retry()
                    }
                }
            } catch (e: Exception) {
                Timber.w(e.toString())
                Result.retry()
            }
            isRunning = false
            result
        }
    }

    companion object {
        const val WORKER_NAME = "APP_AUTH_WORKER"
        const val MAX_RUN_ATTEMPT = 3
        const val LIMIT_PER_THRES = 3
        val THRES_TS = TimeUnit.DAYS.toMillis(1)

        var hasSuccessSendInt = 0 // 1 assumed it already running, means this feature is disabled.
        var PREF = "app_auth"
        var KEY_SUCCESS = "scs"
        var KEY_TS = "ts"
        var KEY_TS_TRIES = "ts_tries"

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

        private fun checkTimestamp(context: Context): Boolean {
            val sp = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            val lastTs = sp.getLong(KEY_TS, 0)
            val now = System.currentTimeMillis()
            // we check the timestamp the worker last run.
            // If the worker last run is more than 1 day, run the worker again
            if (now - lastTs > THRES_TS) {
                sp.edit().putLong(KEY_TS, now).putInt(KEY_TS_TRIES, 0).apply()
                return true
            } else {
                // last run is below 1 day.
                // We check how many times it has tried for one day
                val tries = sp.getInt(KEY_TS_TRIES, 0)
                return if (tries < LIMIT_PER_THRES) {
                    sp.edit().putInt(KEY_TS_TRIES, tries + 1).apply()
                    true
                } else {
                    false
                }
            }
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
        fun scheduleWorker(context: Context, isForce: Boolean) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    if (GlobalConfig.isSellerApp()) {
                        return@launch
                    }
                    if (!isForce && alreadySuccessSend(context)) {
                        return@launch
                    }
                    if (isRunning) {
                        return@launch
                    }
                    val userSession = getUserSession(context)
                    if (!userSession.isLoggedIn) {
                        return@launch
                    }
                    if (!checkTimestamp(context)) {
                        return@launch
                    }
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

}