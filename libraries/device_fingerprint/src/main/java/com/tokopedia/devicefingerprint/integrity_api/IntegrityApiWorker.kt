package com.tokopedia.devicefingerprint.integrity_api

import android.content.Context
import android.util.Base64
import androidx.work.*
import com.google.android.play.core.integrity.IntegrityManager
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.IntegrityServiceException
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.tokopedia.devicefingerprint.di.DaggerDeviceFingerprintComponent
import com.tokopedia.devicefingerprint.di.DeviceFingerprintModule
import com.tokopedia.devicefingerprint.integrity_api.model.IntegrityParam
import com.tokopedia.devicefingerprint.integrity_api.usecase.SubmitIntegrityUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class IntegrityApiWorker(val appContext: Context, val params: WorkerParameters) : CoroutineWorker(appContext, params) {

    @Inject
    lateinit var useCase: SubmitIntegrityUseCase

    init {
        DaggerDeviceFingerprintComponent.builder()
            .deviceFingerprintModule(DeviceFingerprintModule(appContext))
            .build()
            .inject(this)
    }

    private fun initiateIntegrityApi() {
        val integrityManager: IntegrityManager = IntegrityManagerFactory.create(appContext)

        val nonce: String = Base64.encodeToString(
            UUID.randomUUID().toString().toByteArray(),
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )

        val itr: IntegrityTokenRequest = IntegrityTokenRequest.builder()
            .setCloudProjectNumber(692092518182L)
            .setNonce(nonce)
            .build()

        val eventParam = params.inputData.getString(EVENT_PARAM) ?: ""
        val param = IntegrityParam(payload = "", error = "", errorCode = "", event = eventParam)

        integrityManager.requestIntegrityToken(itr).addOnSuccessListener {
            param.payload = it.token()
            submitApi(param)
        }.addOnFailureListener {
            if (it is IntegrityServiceException) {
                param.errorCode = it.errorCode.toString()
                param.error = it.message.orEmpty()
            } else {
                param.error = it.message.orEmpty()
            }
            submitApi(param)
        }
    }

    private fun submitApi(param: IntegrityParam) {
        GlobalScope.launch {
            try {
                useCase(param)
            } catch (ignored: Exception) {}
        }
    }

    override suspend fun doWork(): Result {
        if (runAttemptCount > MAX_RETRY) {
            return Result.failure()
        }
        return withContext(Dispatchers.IO) {
            val result: Result = try {
                initiateIntegrityApi()
                Result.success()
            } catch (e: Exception) {
                Result.retry()
            }
            return@withContext result
        }
    }

    companion object {
        private const val WORKER_NAME = "SUBMIT_INTEGRITY_API_WORKER"
        private const val EVENT_PARAM = "event_param"
        private const val MAX_RETRY = 3

        @JvmStatic
        fun scheduleWorker(context: Context, event: String) {
            try {
                val data = Data.Builder().apply { putString(EVENT_PARAM, event) }.build()
                val periodicWorker = OneTimeWorkRequest
                    .Builder(IntegrityApiWorker::class.java)
                    .setConstraints(Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build())
                    .setInputData(data)
                    .build()
                WorkManager.getInstance(context).enqueueUniqueWork(
                    WORKER_NAME,
                    ExistingWorkPolicy.REPLACE,
                    periodicWorker
                )
            } catch (ex: Exception) { }
        }
    }
}