package com.tokopedia.devicefingerprint.datavisor.workmanager

import android.content.Context
import android.util.Log
import androidx.work.*
import com.tokopedia.devicefingerprint.datavisor.instance.VisorFingerprintInstance
import com.tokopedia.devicefingerprint.datavisor.payload.DeviceInitPayload
import com.tokopedia.devicefingerprint.datavisor.usecase.SubmitDVTokenUseCase
import com.tokopedia.devicefingerprint.di.DaggerDeviceFingerprintComponent
import com.tokopedia.devicefingerprint.di.DeviceFingerprintModule
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit
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
            val result: Result
            var resultInit: Pair<String, String>?
            try {
                resultInit = suspendCancellableCoroutine { continuation ->
                    try {
                        VisorFingerprintInstance.initToken(applicationContext, listener = object : VisorFingerprintInstance.onVisorInitListener {
                            override fun onSuccessInitToken(token: String) {
                                continuation.resume(token to "")
                            }

                            override fun onFailedInitToken(error: String) {
                                continuation.resume("" to error)
                            }
                        })
                    } catch (e: Exception) {
                        continuation.resume("" to e.toString())
                    }
                }
            } catch (e: Exception) {
                resultInit = "" to e.toString()
            }
            val token = resultInit?.first?: ""
            result = if (token.isNotEmpty()) {
                val resultServer = sendDataVisorToServer(applicationContext, token, runAttemptCount, "")
                if (resultServer != null && resultServer.first) {
                    Result.success()
                } else {
                    Result.retry()
                }
            } else {
                val error = resultInit?.second?: ""
                if (isErrorReachMax(runAttemptCount)) {
                    sendErrorDataVisorToServer(applicationContext, error)
                }
                Result.retry()
            }
            result
        }
    }

    fun setTokenLocal(context: Context, token: String) {
        val sp = context.getSharedPreferences(DV_SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val now = System.currentTimeMillis()
        sp.edit().putString(KEY_TOKEN, token)
                .putLong(KEY_TS_TOKEN, now)
                .apply()
        lastToken = token
        lastTimestampToken = now
    }

    fun isErrorReachMax(runAttemptCount: Int) = runAttemptCount == MAX_RUN_ATTEMPT

    fun sendErrorDataVisorToServer(context: Context, errorMessage: String) {
        sendDataVisorToServer(context, DEFAULT_VALUE_DATAVISOR, MAX_RUN_ATTEMPT, errorMessage)
    }

    fun sendDataVisorToServer(context: Context, token: String = DEFAULT_VALUE_DATAVISOR, countAttempt: Int, errorMessage: String): Pair<Boolean, Throwable?>? {
        submitDVTokenUseCase.setParams(DeviceInitPayload(
                token,
                userSession.userId.toLong(),
                countAttempt,
                errorMessage
        ))
        var result: Pair<Boolean, Throwable?>? = null
        submitDVTokenUseCase.execute({
            result = true to null
        }, {
            result = false to it
        })
        setTokenLocal(context, token)
        return result
    }

    companion object {
        const val WORKER_NAME = "DV_WORKER"
        const val MAX_RUN_ATTEMPT = 3
        const val DEFAULT_VALUE_DATAVISOR = "DVLT_6542b775e3263c27e321b929-f52fc6e0_dFlt"
        const val DV_SHARED_PREF_NAME = "pref_dv"
        const val KEY_TOKEN = "tk"
        const val KEY_TS_TOKEN = "ts_tk"
        const val KEY_TS_WORKER = "ts_worker"
        val THRES_TOKEN_VALID_AGE = TimeUnit.DAYS.toMillis(30)
        val THRES_WORKER = TimeUnit.DAYS.toMillis(1)
        var lastToken = ""
        var lastTimestampToken = 0L
        var lastTimestampWorker = 0L
        var userSession: UserSessionInterface? = null

        fun scheduleWorker(context: Context, forceWorker: Boolean) {
            val appContext = context.applicationContext
            if (forceWorker || needToRun(appContext)) {
                runWorker(appContext)
            }
        }

        fun setTsWorker(context: Context) {
            val sp = context.getSharedPreferences(DV_SHARED_PREF_NAME, Context.MODE_PRIVATE)
            val now = System.currentTimeMillis()
            sp.edit().putLong(KEY_TS_WORKER, now)
                    .apply()
            lastTimestampWorker = now
        }

        fun needToRun(context: Context): Boolean {
            if (lastToken.isEmpty()) {
                val sp = context.getSharedPreferences(DV_SHARED_PREF_NAME, Context.MODE_PRIVATE)
                lastToken = sp.getString(KEY_TOKEN, DEFAULT_VALUE_DATAVISOR)
                        ?: DEFAULT_VALUE_DATAVISOR
                lastTimestampToken = sp.getLong(KEY_TS_TOKEN, 0L)
                lastTimestampWorker = sp.getLong(KEY_TS_WORKER, 0L)
            }
            if (lastToken != DEFAULT_VALUE_DATAVISOR) {
                //check token valid age
                if (System.currentTimeMillis() - lastTimestampToken < THRES_TOKEN_VALID_AGE) {
                    return false
                }
            }
            val userSession = getUserSession(context)
            if (!userSession.isLoggedIn) {
                return false
            }
            if (System.currentTimeMillis() - lastTimestampWorker < THRES_WORKER) {
                return false
            }
            return true
        }

        fun getUserSession(context: Context): UserSessionInterface {
            if (userSession == null) {
                userSession = UserSession(context)
            }
            return userSession!!
        }

        fun runWorker(context: Context) {
            try {
                setTsWorker(context)
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