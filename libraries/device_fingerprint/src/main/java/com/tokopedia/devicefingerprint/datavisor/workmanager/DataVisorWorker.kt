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
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
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
            val userSession = getUserSession(applicationContext)
            if (userSession.userId.isEmpty()) {
                return@withContext Result.success()
            }
            try {
                resultInit = suspendCancellableCoroutine { continuation ->
                    try {
                        VisorFingerprintInstance.initToken(applicationContext,
                                userSession.userId,
                                listener = object : VisorFingerprintInstance.onVisorInitListener {
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
            val token = resultInit?.first ?: ""
            result = if (token.isNotEmpty()) {
                try {
                    val resultServer = sendDataVisorToServer(token, runAttemptCount, "")
                    if (!resultServer.subDvcIntlEvent.isError) {
                        setTokenLocal(applicationContext, token)
                        Result.success()
                    } else {
                        Result.retry()
                    }
                } catch (e: Exception) {
                    Result.retry()
                }
            } else {
                val error = resultInit?.second ?: ""
                sendErrorDataVisorToServer(runAttemptCount, error)
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

    suspend fun sendErrorDataVisorToServer(runAttemptCount: Int, errorMessage: String) {
        sendDataVisorToServer(DEFAULT_VALUE_DATAVISOR, runAttemptCount, errorMessage)
    }

    private suspend fun sendDataVisorToServer(token: String = DEFAULT_VALUE_DATAVISOR,
                                              countAttempt: Int, errorMessage: String): SubmitDeviceInitResponse {
        submitDVTokenUseCase.setParams(
                token,
                countAttempt,
                errorMessage
        )
        return submitDVTokenUseCase.executeOnBackground()
    }

    companion object {
        const val WORKER_NAME = "DV_WORKER"
        const val MAX_RUN_ATTEMPT = 3
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
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val appContext = context.applicationContext
                    if (GlobalConfig.isSellerApp()) {
                        return@launch
                    }
                    if (forceWorker || needToRun(appContext)) {
                        runWorker(appContext)
                    }
                } catch (ignored:Exception) { }
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