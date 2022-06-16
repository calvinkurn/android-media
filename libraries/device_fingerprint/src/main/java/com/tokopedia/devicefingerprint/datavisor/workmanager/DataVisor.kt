package com.tokopedia.devicefingerprint.datavisor.workmanager

import android.content.Context
import androidx.work.ListenableWorker.Result
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.devicefingerprint.datavisor.instance.VisorFingerprintInstance
import com.tokopedia.devicefingerprint.datavisor.instance.VisorFingerprintInstance.Companion.DEFAULT_VALUE_DATAVISOR
import com.tokopedia.devicefingerprint.datavisor.usecase.GetDVInitStatUseCase
import com.tokopedia.devicefingerprint.datavisor.usecase.SubmitDVTokenUseCase
import com.tokopedia.devicefingerprint.datavisor.usecase.SubmitRDVTokenUseCase
import com.tokopedia.devicefingerprint.header.FingerprintModelGenerator
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume

class DataVisor @Inject constructor(
    private val context: Context,
    private val dispatchers: CoroutineDispatchers,
    private val userSession: UserSessionInterface,
    private val dataVisorRepository: DataVisorRepository,
    private val submitDVTokenUseCase: SubmitDVTokenUseCase,
    private val submitRDVTokenUseCase: SubmitRDVTokenUseCase,
    private val getDVInitStatUseCase: GetDVInitStatUseCase,
) {
    suspend fun doWork(activityName: String, isFromLogin: Boolean = false): Result {
        return withContext(dispatchers.io) {
            val currentToken = dataVisorRepository.getToken()

            when {
                isFirstInstall() -> preAuthenticationFlow(activityName)
                isFromLogin -> postAuthenticationFlow(currentToken, activityName)
                isValidToken(currentToken) -> recoveryFlowExpiry(currentToken, activityName)
                else -> recoveryFlowMaxAttempt(activityName)
            }

            Result.success()
        }
    }

    private fun isFirstInstall() =
        dataVisorRepository.getWorkerTimeStamp() == NO_TIME_STAMP

    private suspend fun preAuthenticationFlow(activityName: String) {
        saveWorkerTimeStamp()
        runWork(activityName = activityName)
    }

    private suspend fun postAuthenticationFlow(currentToken: String, activityName: String) {
        val runAttemptCount = getRunAttemptCount() + if (isValidToken(currentToken)) 0 else 1

        saveWorkerTimeStamp()
        runWork(runAttemptCount, activityName)
    }

    private fun isValidToken(currentToken: String) =
        currentToken.isNotEmpty() && currentToken != DEFAULT_VALUE_DATAVISOR

    private fun shouldCheckExpiry() = isMoreThanOneDay()

    private suspend fun recoveryFlowExpiry(currentToken: String, activityName: String) {
        if (!shouldCheckExpiry()) return

        saveWorkerTimeStamp()

        if (isExpired(currentToken))
            runWork(activityName = activityName)
    }

    private suspend fun isExpired(currentToken: String) =
        try {
            getDVInitStatUseCase
                .execute(currentToken, System.currentTimeMillis())
                .isExpire()
        } catch (exception: Exception) {
            sendLog(currentToken, ERROR_TYPE_CHECK_EXPIRY, exception.toString())
            false
        }

    private suspend fun recoveryFlowMaxAttempt(activityName: String) {
        val runAttemptCount = getRunAttemptCount()
        if (runAttemptCount < MAX_RUN_ATTEMPT) {
            val incrementedRunAttemptCount =
                runAttemptCount + if (isMoreThanOneDay()) 0 else 1

            saveWorkerTimeStamp()
            runWork(incrementedRunAttemptCount, activityName)
        }
    }

    private fun isMoreThanOneDay() = getTimeStampDifference() > TimeUnit.DAYS.toMillis(1)

    private fun getRunAttemptCount(): Int =
        if (isMoreThanOneDay()) 0
        else dataVisorRepository.getRunAttemptCount()

    private fun getTimeStampDifference(): Long =
        System.currentTimeMillis() - dataVisorRepository.getWorkerTimeStamp()

    private fun saveWorkerTimeStamp() {
        dataVisorRepository.saveWorkerTimeStamp(System.currentTimeMillis())
    }

    private suspend fun runWork(runAttemptCount: Int = 0, activityName: String) {
        val (token, error) = initToken()
        dataVisorRepository.saveRunAttemptCount(runAttemptCount)

        if (token.isNotEmpty())
            initTokenSuccess(runAttemptCount, token, error, activityName)
        else
            initTokenFailed(runAttemptCount, token, error, activityName)
    }

    private suspend fun initToken(): Pair<String, String> =
        try {
            suspendCancellableCoroutine { continuation ->
                try {
                    VisorFingerprintInstance.initToken(
                        context,
                        userSession.userId,
                        initDataVisorListener(continuation),
                    )
                } catch (e: Exception) {
                    continuation.resume("" to e.toString())
                }
            }
        } catch (e: Exception) {
            "" to e.toString()
        }

    private fun initDataVisorListener(continuation: CancellableContinuation<Pair<String, String>>) =
        object : VisorFingerprintInstance.onVisorInitListener {
            override fun onSuccessInitToken(token: String) {
                continuation.resume(token to "")
            }

            override fun onFailedInitToken(error: String) {
                continuation.resume("" to error)
            }
        }

    private suspend fun initTokenSuccess(
        runAttemptCount: Int,
        token: String,
        error: String,
        activityName: String,
    ) {
        try {
            saveToken(token)

            val resultSuccess = sendDataVisorToServerResult(
                token,
                runAttemptCount,
                error,
                activityName,
            )

            if (!resultSuccess)
                sendLog(token, ERROR_TYPE_BACKEND_RESPONSE, "")
        } catch (exception: Exception) {
            sendLog(token, ERROR_TYPE_SEND_BACKEND, exception.toString())
        }
    }

    private fun saveToken(token: String) {
        dataVisorRepository.saveToken(token)
        FingerprintModelGenerator.expireFingerprint()
    }

    private suspend fun sendDataVisorToServerResult(
        token: String,
        retryCount: Int,
        error: String,
        activityName: String,
    ): Boolean {
        val backendToken = token.ifEmpty { DEFAULT_VALUE_DATAVISOR }

        val isError =
            if (userSession.isLoggedIn)
                submitDVTokenUseCase.execute(backendToken, retryCount, error, activityName).isError()
            else
                submitRDVTokenUseCase.execute(backendToken, retryCount, error, activityName).isError()

        return !isError
    }

    private fun sendLog(token: String, errorType: String, error: String) {
        ServerLogger.log(
            Priority.P1,
            LOG_TAG,
            mapOf(
                LOG_TOKEN to token,
                LOG_ERROR_TYPE to errorType,
                LOG_ERROR to error,
            )
        )
    }

    private suspend fun initTokenFailed(
        runAttemptCount: Int,
        token: String,
        error: String,
        activityName: String,
    ) {
        try {
            sendDataVisorToServerResult(token, runAttemptCount, error, activityName)
        } catch (ignored: Exception) { }

        sendLog(token, ERROR_TYPE_INIT_SDK, error)
    }

    companion object {
        const val MAX_RUN_ATTEMPT = 3
        const val LOG_TAG = "GQL_ERROR_RISK"
        const val LOG_TOKEN = "token"
        const val LOG_ERROR_TYPE = "errorType"
        const val LOG_ERROR = "error"
        const val ERROR_TYPE_INIT_SDK = "error on init SDK"
        const val ERROR_TYPE_SEND_BACKEND = "error on send to backend"
        const val ERROR_TYPE_BACKEND_RESPONSE = "error from backend response"
        const val ERROR_TYPE_CHECK_EXPIRY = "error when check expiry"
        const val NO_TIME_STAMP = 0L
    }
}