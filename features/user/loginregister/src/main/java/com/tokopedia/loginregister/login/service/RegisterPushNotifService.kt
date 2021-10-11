package com.tokopedia.loginregister.login.service

import android.content.Context
import android.content.Intent
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.core.app.JobIntentService
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.loginregister.login.data.SignResult
import com.tokopedia.loginregister.login.di.LoginComponentBuilder
import com.tokopedia.loginregister.login.domain.RegisterPushNotifParamsModel
import com.tokopedia.loginregister.login.domain.RegisterPushNotifUseCase
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.security.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

/**
 * Created by Ade Fulki on 28/09/20.
 */

class RegisterPushNotifService : JobIntentService(), CoroutineScope {

    @field:Named(SessionModule.SESSION_MODULE)
    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var registerPushNotifUseCase: RegisterPushNotifUseCase

    private val job = SupervisorJob()

    private lateinit var keyPair: KeyPair

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    override fun onHandleWork(intent: Intent) {
        try {
            if (userSession.isLoggedIn && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                generateKey()
                if (::keyPair.isInitialized) {
                    signData(userSession.userId, userSession.deviceId).let {
                        doRegisterPushNotif(it)
                    }
                }
            }
        } catch (e: Exception) {
            recordLog(ON_HANDLE_WORK, "", e)
            e.printStackTrace()
        }
    }

    private fun initInjector() {
        application?.let {
            LoginComponentBuilder.getComponent(it).inject(this)
        }
    }

    private fun doRegisterPushNotif(signResult: SignResult) {
        launchCatchError(coroutineContext, {
            registerPushNotifUseCase(RegisterPushNotifParamsModel(
                publicKey = signResult.publicKey,
                signature = signResult.signature,
                datetime = signResult.datetime
            ))
        }, {
            recordLog(DO_REGISTER_PUSH_NOTIF, "", it)
        })
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
            recordLog(SIGN_DATA, "", e)
            e.printStackTrace()
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

        private val ERROR_HEADER = "${RegisterPushNotifService::class.java.name} error on "
        private const val TAG_SCALYR = "CRASH_REGISTER_PUSHNOTIF"
        private const val MAX_LENGTH_ERROR = 1000
        private const val ON_HANDLE_WORK = "onHandlerWork()"
        private const val START_SERVICE = "startServices()"
        private const val SIGN_DATA = "signData()"
        private const val DO_REGISTER_PUSH_NOTIF = "doRegisterPushNotif()"

        fun startService(context: Context, jobId: Int) {
            try {
                val intent = Intent(context, RegisterPushNotifService::class.java)
                enqueueWork(context, RegisterPushNotifService::class.java, jobId, intent)
            } catch (e: Exception) {
                recordLog(START_SERVICE, "JOB_ID = $jobId", e)
                e.printStackTrace()
            }
        }

        fun recordLog(type: String, message: String, throwable: Throwable) {
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