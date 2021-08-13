package com.tokopedia.otp.common

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import com.tokopedia.otp.notif.data.SignResult
import java.security.*

object SignatureUtil {

    private const val SHA_256_WITH_RSA = "SHA256withRSA"
    private const val PUBLIC_KEY_PREFIX = "-----BEGIN PUBLIC KEY-----\n"
    private const val PUBLIC_KEY_SUFFIX = "\n-----END PUBLIC KEY-----"
    private const val ANDROID_KEY_STORE = "AndroidKeyStore"

    @RequiresApi(Build.VERSION_CODES.M)
    fun generateKey(alias: String): KeyPair? {
        val keyPairGenerator: KeyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEY_STORE)

        val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(alias,
                KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY).run {
            setDigests(KeyProperties.DIGEST_SHA256)
            setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
            build()
        }

        keyPairGenerator.initialize(parameterSpec)
        return keyPairGenerator.genKeyPair()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun signData(data: String, alias: String): SignResult {
        val signResult = SignResult()
        try {
            val datetime = (System.currentTimeMillis() / 1000).toString()
            signResult.datetime = datetime

            val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply {
                load(null)
            }

            val privateKey: PrivateKey = keyStore.getKey(alias, null) as PrivateKey

            val publicKey: PublicKey = keyStore.getCertificate(alias).publicKey
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
        return "${PUBLIC_KEY_PREFIX}$encoded${PUBLIC_KEY_SUFFIX}"
    }
}