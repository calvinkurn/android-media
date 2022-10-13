package com.tokopedia.loginfingerprint.utils.crypto

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import com.tokopedia.graphql.util.Const
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.loginfingerprint.constant.BiometricConstant
import com.tokopedia.loginfingerprint.data.model.SignatureData
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.Signature
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec


@TargetApi(Build.VERSION_CODES.M)
class RsaSignatureUtils {

    init {
        initKeys()
    }

    /* Check if we already have keypair within keystore with given alias */
    private fun hasKey(): Boolean {
        return try {
            val ks = getKeystore()
            ks.containsAlias(BiometricConstant.FINGERPRINT)
        } catch (e: Exception) {
            log("hasKey", e)
            false
        }
    }

    private fun initKeys() {
        if(!hasKey()) {
            try {
                val kpGenerator: KeyPairGenerator =
                    KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, BiometricConstant.ANDROID_KEY_STORE)

                val spec =
                    KeyGenParameterSpec.Builder(BiometricConstant.FINGERPRINT, KeyProperties.PURPOSE_SIGN)
                        .setDigests(KeyProperties.DIGEST_SHA256)
                        .setKeySize(KEY_SIZE)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                        .build()

                kpGenerator.initialize(spec)
                kpGenerator.generateKeyPair()
            } catch (e: Exception) {
                log("initKeys_Exception", e)
            }
        }
    }

    private fun getKeystore(): KeyStore {
        val ks: KeyStore = KeyStore.getInstance(BiometricConstant.ANDROID_KEY_STORE)
        ks.load(null)
        return ks
    }

    suspend fun signData(inputStr: String): String {
        try {
            val data = inputStr.toByteArray()

            // BEGIN_INCLUDE(sign_load_keystore)
            val ks = getKeystore()

            // Load the key pair from the Android Key Store
            val entry: KeyStore.Entry = ks.getEntry(BiometricConstant.FINGERPRINT, null)

            /* If the entry is null, keys were never stored under this alias.
             * Debug steps in this situation would be:
             * -Check the list of aliases by iterating over Keystore.aliases(), be sure the alias
             *   exists.
             * -If that's empty, verify they were both stored and pulled from the same keystore
             *   "AndroidKeyStore"
             */
            if (entry == null) {
                return ""
            }

            /* If entry is not a KeyStore.PrivateKeyEntry, it might have gotten stored in a previous
             * iteration of your application that was using some other mechanism, or been overwritten
             * by something else using the same keystore with the same alias.
             * You can determine the type using entry.getClass() and debug from there.
             */
            if (entry !is KeyStore.PrivateKeyEntry) {
                return ""
            }

            val s: Signature = Signature.getInstance(BiometricConstant.SHA_256_WITH_RSA)
            s.initSign(entry.privateKey)
            s.update(data)
            val signature: ByteArray = s.sign()
            return Base64.encodeToString(signature, Base64.NO_WRAP)
        } catch (e: Exception) {
            log("signData_Exception", e)
        }
        return ""
    }

    suspend fun generateFingerprintSignature(uniqueId: String, deviceId: String): SignatureData {
        val datetime = (System.currentTimeMillis() / DATE_DIVIDER).toString()
        val signature = signData(uniqueId + datetime + deviceId)
        return SignatureData(signature = signature, datetime = datetime)
    }

    fun getPublicKey(): String {
        try {
            val keyStore = getKeystore()
            val publicKey = keyStore.getCertificate(BiometricConstant.FINGERPRINT).publicKey as RSAPublicKey
            val factory = KeyFactory.getInstance(RSA_ALGORITHM)
            val spec = X509EncodedKeySpec(publicKey.encoded)
            val finalKey = factory.generatePublic(spec) as RSAPublicKey
            val encoded = Base64.encodeToString(finalKey.encoded, Base64.DEFAULT)
            return "${PUBLIC_KEY_PREFIX}${encoded}${PUBLIC_KEY_SUFFIX}"
        } catch (e: Exception) {
            log("getPublicKey_Exception", e)
        }
        return ""
    }

    private fun log(type: String, throwable: Exception) {
        val msg = mapOf(
            "method" to type,
            "exception" to Log.getStackTraceString(throwable).take(Const.GQL_ERROR_MAX_LENGTH),
            "caused" to Log.getStackTraceString(throwable.cause).take(Const.GQL_ERROR_MAX_LENGTH)
        )

        ServerLogger.log(
            Priority.P1,
            "CRYPTOGRAPHY_ERROR",
            msg
        )
    }

    companion object {
        private const val PUBLIC_KEY_PREFIX = "-----BEGIN PUBLIC KEY-----\n"
        private const val PUBLIC_KEY_SUFFIX = "-----END PUBLIC KEY-----"
        private const val RSA_ALGORITHM = "RSA"
        private const val DATE_DIVIDER = 1000
        private const val KEY_SIZE = 2048
    }
}
