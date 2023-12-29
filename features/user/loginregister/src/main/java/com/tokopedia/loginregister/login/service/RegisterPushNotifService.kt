package com.tokopedia.loginregister.login.service

import android.content.Context
import android.content.Intent
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import com.tokopedia.abstraction.base.service.JobIntentServiceX
import com.tokopedia.loginregister.login.data.SignResult
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import com.tokopedia.loginregister.registerpushnotif.domain.RegisterPushNotificationParamsModel
import com.tokopedia.loginregister.registerpushnotif.domain.RegisterPushNotificationUseCase
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.security.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Ade Fulki on 28/09/20.
 */

@Deprecated("move into workmanager")
class RegisterPushNotifService : JobIntentServiceX(), CoroutineScope {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var registerPushNotifUseCase: RegisterPushNotificationUseCase

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    private lateinit var keyPair: KeyPair

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
                        launch {
                            registerPushNotifUseCase(RegisterPushNotificationParamsModel(
                                publicKey = it.publicKey,
                                signature = it.signature,
                                datetime = it.datetime
                            ))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initInjector() {
        application?.let {
            ActivityComponentFactory.instance.createLoginComponent(it).inject(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun generateKey() {
        val keyPairGenerator: KeyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEY_STORE)

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

        fun startService(context: Context, jobId: Int) {
            try {
                val intent = Intent(context, RegisterPushNotifService::class.java)
                enqueueWork(context, RegisterPushNotifService::class.java, jobId, intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
