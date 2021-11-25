package com.tokopedia.devicefingerprint.datavisor.workmanager

import android.content.Context
import androidx.work.*
import com.tokopedia.config.GlobalConfig
import com.tokopedia.devicefingerprint.datavisor.instance.VisorFingerprintInstance
import com.tokopedia.devicefingerprint.datavisor.instance.VisorFingerprintInstance.Companion.DEFAULT_VALUE_DATAVISOR
import com.tokopedia.devicefingerprint.datavisor.instance.VisorFingerprintInstance.Companion.DV_SHARED_PREF_NAME
import com.tokopedia.devicefingerprint.datavisor.response.SubmitDeviceInitResponse
import com.tokopedia.devicefingerprint.datavisor.usecase.SubmitDVTokenUseCase
import com.tokopedia.devicefingerprint.di.DaggerDeviceFingerprintComponent
import com.tokopedia.devicefingerprint.di.DeviceFingerprintModule
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.math.min

class DataVisorWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    @Inject
    lateinit var submitDVTokenUseCase: SubmitDVTokenUseCase

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
            val userSession = getUserSession(applicationContext)
            if (userSession.userId.isEmpty()) {
                return@withContext Result.success()
            }
            try {
                resultInit = suspendCancellableCoroutine { continuation ->
                    try {
                        if (isTokenExpired) {
                            VisorFingerprintInstance.initToken(applicationContext,
                                userSession.userId,
                                listener = object : VisorFingerprintInstance.onVisorInitListener {
                                    override fun onSuccessInitToken(token: String) {
                                        lastToken = token
                                        continuation.resume(token to "")
                                    }

                                    override fun onFailedInitToken(error: String) {
                                        continuation.resume("" to error)
                                    }
                                })
                        } else {
                            continuation.resume(lastToken to "")
                        }
                    } catch (e: Exception) {
                        continuation.resume("" to e.toString())
                    }
                }
            } catch (e: Exception) {
                resultInit = "" to e.toString()
            }
            val token = resultInit?.first ?: ""
            result = if (token.isNotEmpty()) {
                try {
                    val resultServer = sendDataVisorToServer(token, runAttemptCount, "", isTokenExpired)
                    if (!resultServer.subDvcIntlEvent.isError) {
                        setToken(applicationContext, token)
                        if (resultServer.subDvcIntlEvent.dvData.isExpire) {
                            setTokenExpired(applicationContext, true)
                            Result.retry()
                        } else {
                            setTokenExpired(applicationContext, false)
                            Result.success()
                        }
                    } else {
                        sendLog(token, true, "")
                        Result.retry()
                    }
                } catch (e: Exception) {
                    sendLog(token, false, e.toString())
                    Result.retry()
                }
            } else {
                val error = resultInit?.second ?: ""
                sendLog(token, false, error)
                sendErrorDataVisorToServer(runAttemptCount, error, isTokenExpired)
                Result.retry()
            }
            result
        }
    }

    private fun sendLog(token: String = "", isError: Boolean = false, throwableString: String) {
        ServerLogger.log(
            Priority.P1, LOG_TAG,
            mapOf(
                "token" to token,
                "isError" to isError.toString(),
                "error" to throwableString
            )
        )
    }

    suspend fun sendErrorDataVisorToServer(
        runAttemptCount: Int,
        errorMessage: String,
        checkForce: Boolean
    ) {
        sendDataVisorToServer(DEFAULT_VALUE_DATAVISOR, runAttemptCount, errorMessage, checkForce)
    }

    private suspend fun sendDataVisorToServer(
        token: String = DEFAULT_VALUE_DATAVISOR,
        countAttempt: Int, errorMessage: String,
        checkForce: Boolean
    ): SubmitDeviceInitResponse {
        return submitDVTokenUseCase.execute(
            token,
            countAttempt,
            errorMessage,
            checkForce = checkForce
        )
    }

    companion object {
        const val WORKER_NAME = "DV_WORKER"
        const val MAX_RUN_ATTEMPT = 3
        const val KEY_TOKEN = "tk"
        const val KEY_EXPIRED = "is_exp"
        const val KEY_TS_WORKER = "ts_worker"
        const val LOG_TAG = "GQL_ERROR_RISK"
        val THRES_WORKER = TimeUnit.DAYS.toMillis(1)
        var lastToken = ""
        var lastTimestampWorker = 0L
        var isTokenExpired = false
        var userSession: UserSessionInterface? = null

        fun scheduleWorker(context: Context, forceWorker: Boolean) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val appContext = context.applicationContext
                    if (GlobalConfig.isSellerApp()) {
                        return@launch
                    }
                    if (forceWorker || needToRun(appContext)) {
                        runWorker(appContext)
                    }
                } catch (ignored: Exception) {
                }
            }
        }

        fun setToken(context: Context, token: String) {
            if (lastToken != token) {
                val sp = context.getSharedPreferences(DV_SHARED_PREF_NAME, Context.MODE_PRIVATE)
                lastToken = token
                sp.edit().putString(KEY_TOKEN, token).apply()
                FingerprintModelGenerator.expireFingerprint()
            }
        }

        fun setTsWorker(context: Context) {
            val sp = context.getSharedPreferences(DV_SHARED_PREF_NAME, Context.MODE_PRIVATE)
            val now = System.currentTimeMillis()
            sp.edit().putLong(KEY_TS_WORKER, now)
                .apply()
            lastTimestampWorker = now
        }

        fun setTokenExpired(context: Context, isExpired: Boolean) {
            if (isTokenExpired != isExpired) {
                val sp = context.getSharedPreferences(DV_SHARED_PREF_NAME, Context.MODE_PRIVATE)
                sp.edit().putBoolean(KEY_EXPIRED, isExpired).apply()
                isTokenExpired = isExpired
            }
        }

        fun needToRun(context: Context): Boolean {
            if (lastToken.isEmpty()) {
                val sp = context.getSharedPreferences(DV_SHARED_PREF_NAME, Context.MODE_PRIVATE)
                lastToken = sp.getString(KEY_TOKEN, DEFAULT_VALUE_DATAVISOR)
                    ?: DEFAULT_VALUE_DATAVISOR
                lastTimestampWorker = sp.getLong(KEY_TS_WORKER, 0L)
                isTokenExpired = sp.getBoolean(KEY_EXPIRED, false)
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
                userSession = UserSession(context.applicationContext)
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
                        .setConstraints(
                            Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                .build()
                        )
                        .build()
                )
            } catch (ex: Exception) {
                Timber.w(ex.toString())
            }
        }
    }
}