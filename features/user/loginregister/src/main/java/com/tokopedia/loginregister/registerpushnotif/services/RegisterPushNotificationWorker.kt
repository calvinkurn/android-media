package com.tokopedia.loginregister.registerpushnotif.services

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.work.*
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.logger.utils.globalScopeLaunch
import com.tokopedia.loginregister.login.data.SignResult
import com.tokopedia.loginregister.registerpushnotif.domain.RegisterPushNotificationParamsModel
import com.tokopedia.loginregister.registerpushnotif.domain.RegisterPushNotificationUseCase
import com.tokopedia.loginregister.login.domain.pojo.RegisterPushNotifData
import com.tokopedia.loginregister.registerpushnotif.di.DaggerRegisterPushNotificationComponent
import com.tokopedia.loginregister.registerpushnotif.di.RegisterPushNotificationModule
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.user.session.UserSessionInterface
import java.security.*
import javax.inject.Inject
import javax.inject.Named

class RegisterPushNotificationWorker(
        val context: Context,
        params: WorkerParameters
) : CoroutineWorker(context, params) {

    @Named(SessionModule.SESSION_MODULE)
    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var registerPushNotificationUseCase: RegisterPushNotificationUseCase

    private var keyPair: KeyPair? = null

    private val sharedPreferences by lazy {
        context.getSharedPreferences(REGISTER_PUSH_NOTIFICATION_PREFERENCE, Context.MODE_PRIVATE)
    }

    init {
        DaggerRegisterPushNotificationComponent.builder()
                .registerPushNotificationModule(RegisterPushNotificationModule(context))
                .build()
                .inject(this)
    }

    override suspend fun doWork(): Result {
        return when {
            isRegistered() -> {
                Result.success()
            }
            runAttemptCount > MAX_RUN_ATTEMPT -> {
                saveRegisterStatus(false)
                Result.failure()
            }
            userSession.isLoggedIn -> try {
                val response = registerPushNotification()
                if (response?.isSuccess == true) {
                    saveRegisterStatus(true)
                    Result.success()
                } else {
                    recordLog(LOG_TYPE_DO_WORK, "retry count = $runAttemptCount", Throwable(response?.errorMessage))
                    Result.retry()
                }
            } catch (e: Exception) {
                recordLog(LOG_TYPE_DO_WORK, "retry count catch = $runAttemptCount", e)
                Result.retry()
            }
            else -> Result.failure()
        }
    }

    private suspend fun registerPushNotification(): RegisterPushNotifData? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            generateKey()
            if (keyPair != null) {
                signData(userSession.userId.orEmpty(), userSession.deviceId.orEmpty()).let {
                    try {
                        return registerPushNotificationUseCase(RegisterPushNotificationParamsModel(
                                publicKey = it.publicKey,
                                signature = it.signature,
                                datetime = it.datetime
                        )).data
                    } catch (e: Exception) {
                        saveRegisterStatus(false)
                        recordLog(LOG_TYPE_REGISTER_PUSH_NOTIF, "", e)
                    }
                }
            }
        }
        return null
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

    @SuppressLint("CommitPrefEdits")
    private fun saveRegisterStatus(isSuccess: Boolean) {
        sharedPreferences?.edit()?.apply {
            putBoolean(IS_REGISTERED, isSuccess)
        }?.apply()
    }

    private fun isRegistered(): Boolean {
        return sharedPreferences?.getBoolean(IS_REGISTERED, false) == true
    }

    companion object {
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val PUSH_NOTIF_ALIAS = "PushNotif"
        private const val SHA_256_WITH_RSA = "SHA256withRSA"
        private const val PUBLIC_KEY_PREFIX = "-----BEGIN PUBLIC KEY-----\n"
        private const val PUBLIC_KEY_SUFFIX = "\n-----END PUBLIC KEY-----"

        private const val WORKER_NAME = "REGISTER_PUSH_NOTIFICATION_WORKER"
        private const val MAX_RUN_ATTEMPT = 3

        private const val REGISTER_PUSH_NOTIFICATION_PREFERENCE = "registerPushNotification"
        private const val IS_REGISTERED = "isRegistered"

        private const val TAG_REGISTER_PUSH_NOTIF = "CRASH_REGISTER_PUSHNOTIF"
        private const val MAX_LENGTH_ERROR = 1000

        private const val LOG_TYPE_SCHEDULE_WORKER = "scheduleWorker()"
        private const val LOG_TYPE_DO_WORK = "doWork()"
        private const val LOG_TYPE_SIGN_DATA = "signData()"
        private const val LOG_TYPE_REGISTER_PUSH_NOTIF = "registerPushNotification()"

        @JvmStatic
        fun scheduleWorker(context: Context) {
            globalScopeLaunch({
                try {
                    runWorker(context)
                } catch (e: Exception) {
                    recordLog(LOG_TYPE_SCHEDULE_WORKER, "", e)
                }
            })
        }

        private fun runWorker(context: Context) {
            WorkManager.getInstance(context).enqueueUniqueWork(
                    WORKER_NAME,
                    ExistingWorkPolicy.KEEP,
                    OneTimeWorkRequestBuilder<RegisterPushNotificationWorker>()
                            .setConstraints(Constraints.Builder()
                                    .setRequiredNetworkType(NetworkType.CONNECTED)
                                    .build()
                            ).build()
            )
        }

        private fun recordLog(type: String, message: String, throwable: Throwable) {
            ServerLogger.log(Priority.P2, TAG_REGISTER_PUSH_NOTIF, mapOf(
                    "type" to type,
                    "message" to message,
                    "err" to throwable.toString().take(MAX_LENGTH_ERROR)
            ))
        }
    }

}
