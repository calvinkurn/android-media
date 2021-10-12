package com.tokopedia.loginregister.login.service

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.work.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.logger.utils.globalScopeLaunch
import com.tokopedia.loginregister.login.data.SignResult
import com.tokopedia.loginregister.login.domain.RegisterPushNotificationParamsModel
import com.tokopedia.loginregister.login.domain.RegisterPushNotificationUseCase
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class RegisterPushNotificationWorker(
    val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @Inject
    lateinit var registerPushNotificationUseCase: RegisterPushNotificationUseCase

    private var userSession: UserSession? = null
    private var keyPair: KeyPair? = null

    init {
        userSession = UserSession(context)
    }

    override suspend fun doWork(): Result {
        if (runAttemptCount > MAX_RUN_ATTEMPT) {
            return Result.failure()
        }

        return withContext(Dispatchers.IO) {
            val result: Result = try {
                if (userSession?.isLoggedIn == true) {
                    registerPushNotification()
                    Result.success()
                } else {
                    Result.failure()
                }
            } catch (e: Exception) {
                recordLog(LOG_TYPE_DO_WORK, "retry count = $runAttemptCount", e)
                Result.retry()
            }

            return@withContext result
        }
    }

    private suspend fun registerPushNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            generateKey()
            if (keyPair != null) {
                signData(userSession?.userId.orEmpty(), userSession?.deviceId.orEmpty()).let {
                    registerPushNotificationUseCase(RegisterPushNotificationParamsModel(
                        publicKey = it.publicKey,
                        signature = it.signature,
                        datetime = it.datetime
                    ))
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun generateKey() {
        val keyPairGenerator: KeyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_RSA,
            ANDROID_KEY_STORE
        )

        val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(PUSH_NOTIF_ALIAS,
            KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY).run {
            setDigests(KeyProperties.DIGEST_SHA256)
            setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
            build()
        }

        keyPairGenerator.initialize(parameterSpec)
        keyPair = keyPairGenerator.genKeyPair()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun signData(userId: String, deviceId: String): SignResult {
        val signResult = SignResult()
        try {
            val datetime = (System.currentTimeMillis() / 1000).toString()
            signResult.datetime = datetime

            val data = userId + datetime + deviceId

            val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply {
                load(null)
            }

            val privateKey: PrivateKey = keyStore.getKey(PUSH_NOTIF_ALIAS, null) as PrivateKey

            val publicKey: PublicKey = keyStore.getCertificate(PUSH_NOTIF_ALIAS).publicKey
            signResult.publicKey = publicKeyToString(publicKey.encoded)

            val signature: ByteArray? = Signature.getInstance(SHA_256_WITH_RSA).run {
                initSign(privateKey)
                update(data.toByteArray())
                sign()
            }

            if (signature != null) {
                signResult.signature = Base64.encodeToString(signature, Base64.DEFAULT)
            }

        } catch (e: Exception) {
            recordLog(LOG_TYPE_SIGN_DATA, "", e)
        }

        return signResult
    }

    private fun publicKeyToString(input: ByteArray): String {
        val encoded = Base64.encodeToString(input, Base64.NO_WRAP)
        return "$PUBLIC_KEY_PREFIX$encoded$PUBLIC_KEY_SUFFIX"
    }

    companion object {
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val PUSH_NOTIF_ALIAS = "PushNotif"
        private const val SHA_256_WITH_RSA = "SHA256withRSA"
        private const val PUBLIC_KEY_PREFIX = "-----BEGIN PUBLIC KEY-----\n"
        private const val PUBLIC_KEY_SUFFIX = "\n-----END PUBLIC KEY-----"

        private const val WORKER_NAME = "REGISTER_PUSH_NOTIFICATION_WORKER"
        private const val MAX_RUN_ATTEMPT = 3
        private const val DELAY_WORKER = 5L

        private val ERROR_HEADER = "${RegisterPushNotificationWorker::class.java.name} error on "
        private const val TAG_SCALYR = "CRASH_REGISTER_PUSHNOTIF"
        private const val MAX_LENGTH_ERROR = 1000

        private const val LOG_TYPE_SCHEDULE_WORKER = "scheduleWorker()"
        private const val LOG_TYPE_DO_WORK = "doWork()"
        private const val LOG_TYPE_SIGN_DATA = "signData()"

        @JvmStatic
        fun scheduleWorker(context: Context, forceWorker: Boolean) {
            globalScopeLaunch({
                try {
                    if (forceWorker) {
                        runWorker(context)
                    }
                } catch (e: Exception) {
                    recordLog(LOG_TYPE_SCHEDULE_WORKER, "", e)
                }
            })
        }

        private fun runWorker(context: Context) {
            WorkManager.getInstance(context).enqueueUniqueWork(
                WORKER_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequestBuilder<RegisterPushNotificationWorker>()
                    .setInitialDelay(DELAY_WORKER, TimeUnit.SECONDS)
                    .setConstraints(Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                    ).build()
            )
        }

        private fun recordLog(type: String, message: String, throwable: Throwable) {
            val logMessage = if (message.isEmpty()) type else "$type | $message"
            sendLogToCrashlytics(logMessage, throwable)
            ServerLogger.log(Priority.P2, TAG_SCALYR, mapOf(
                "type" to type,
                "err" to throwable.toString().take(MAX_LENGTH_ERROR))
            )
        }

        private fun sendLogToCrashlytics(message: String, throwable: Throwable) {
            try {
                val messageException = "$ERROR_HEADER $message : ${throwable.localizedMessage}"
                FirebaseCrashlytics.getInstance().recordException(
                    RuntimeException(
                        messageException,
                        throwable
                    )
                )
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

}