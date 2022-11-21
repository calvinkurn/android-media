package com.tokopedia.devicefingerprint.integrityapi

import android.content.Context
import android.util.Base64
import androidx.work.*
import com.google.android.play.core.integrity.IntegrityManager
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.IntegrityServiceException
import com.google.android.play.core.integrity.IntegrityTokenRequest
import com.tokopedia.config.GlobalConfig
import com.tokopedia.devicefingerprint.di.DaggerDeviceFingerprintComponent
import com.tokopedia.devicefingerprint.di.DeviceFingerprintModule
import com.tokopedia.devicefingerprint.integrityapi.model.IntegrityParam
import com.tokopedia.devicefingerprint.integrityapi.usecase.SubmitIntegrityUseCase
import com.tokopedia.encryption.security.AESEncryptorGCM
import com.tokopedia.remoteconfig.RemoteConfigInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.toLongOrDefault
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

    private fun getCloudProjectNumber(): Long {
        return try {
            val aesEncryptor = AESEncryptorGCM(IntegrityApiConstant.NONCE, true)
            val secretKey = aesEncryptor.generateKey(IntegrityApiConstant.KEY)
            aesEncryptor.decrypt(IntegrityApiConstant.CLOUD_PROJECT_NUMBER, secretKey).toLongOrDefault(0L)
        } catch (e: Exception) {
            0L
        }
    }

    private fun initiateIntegrityApi() {
        val integrityManager: IntegrityManager = IntegrityManagerFactory.create(appContext)

        val nonce: String = Base64.encodeToString(
            UUID.randomUUID().toString().toByteArray(),
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )

        val itr: IntegrityTokenRequest = IntegrityTokenRequest.builder()
            .setCloudProjectNumber(getCloudProjectNumber())
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

        private const val CONFIG_INTEGRITY = "and_play_integrity"
        private const val SELLER_CONFIG_INTEGRITY = "sel_play_integrity"

        fun isEnable(): Boolean {
            val rollence = if(GlobalConfig.isSellerApp()) {
                RemoteConfigInstance.getInstance().abTestPlatform.getString(SELLER_CONFIG_INTEGRITY, "")
            } else {
                RemoteConfigInstance.getInstance().abTestPlatform.getString(CONFIG_INTEGRITY, "")
            }
            return rollence.isNotEmpty()
        }

        @JvmStatic
        fun scheduleWorker(context: Context, event: String) {
            if (isEnable()) {
                try {
                    val data = Data.Builder().apply { putString(EVENT_PARAM, event) }.build()
                    val periodicWorker = OneTimeWorkRequest
                        .Builder(IntegrityApiWorker::class.java)
                        .setConstraints(
                            Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                .build()
                        )
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
}
