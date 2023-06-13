package com.tokopedia.loginfingerprint.utils.crypto

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import com.tokopedia.graphql.util.Const
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.loginfingerprint.constant.BiometricConstant
import com.tokopedia.loginfingerprint.data.model.SignatureData
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.util.EncoderDecoder
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec

@TargetApi(Build.VERSION_CODES.M)
class KeyPairManager(val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /*
     Create new key with KeyPairGenerator and store it to the shared preference.
     Data is encrypted using AES.
     Return Public Key to be stored on BE
     */
    fun createAndStoreNewKey(): String {
        return try {
            val kpGenerator: KeyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
            val keyPair = kpGenerator.generateKeyPair()
            storeNewPrivateKey(Base64.encodeToString(keyPair.private.encoded, Base64.NO_WRAP))
            val encoded = Base64.encodeToString(keyPair.public.encoded, Base64.DEFAULT)
            "${PUBLIC_KEY_PREFIX}${encoded}${PUBLIC_KEY_SUFFIX}"
        } catch (e: Exception) {
            log("key_creation_exception", e)
            ""
        }
    }

    /*
        Key is encrypted before being stored locally.
     */
    private fun storeNewPrivateKey(privateKey: String) {
        try {
            val encryptedKey = EncoderDecoder.Encrypt(privateKey, UserSession.KEY_IV)
            sharedPreferences.edit().putString(PRIV_KEY, encryptedKey).apply()
        } catch (e: Exception) {
            log("store_new_key_exception", e)
        }
    }

    private fun getPrivateKey(): PrivateKey? {
        return try {
            val encryptedKey = sharedPreferences.getString(PRIV_KEY, "")
            if (encryptedKey?.isNotEmpty() == true) {
                val decryptedKey = EncoderDecoder.Decrypt(encryptedKey, UserSession.KEY_IV).toEmptyStringIfNull()
                val kf = KeyFactory.getInstance(RSA_ALGORITHM)
                val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.decode(decryptedKey, Base64.NO_WRAP))
                kf.generatePrivate(keySpecPKCS8)
            } else {
                null
            }
        } catch (e: Exception) {
            log("get_private_key_exception", e)
            null
        }
    }

    fun removeKeys() {
        sharedPreferences.edit().clear().apply()
    }

    suspend fun generateFingerprintSignature(uniqueId: String, deviceId: String): SignatureData {
        val datetime = (System.currentTimeMillis() / DATE_DIVIDER).toString()
        val signature = signData(uniqueId + datetime + deviceId)
        return SignatureData(signature = signature, datetime = datetime)
    }

    private fun signData(textToSign: String): String {
        val privateKey = getPrivateKey()
        privateKey?.let {
            try {
                val s: Signature = Signature.getInstance(BiometricConstant.SHA_256_WITH_RSA)
                s.initSign(it)
                s.update(textToSign.toByteArray(Charsets.UTF_8))
                val signature = s.sign()
                return Base64.encodeToString(signature, Base64.NO_WRAP)
            } catch (e: Exception) {
                log("sign_data_exception", e)
                ""
            }
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

        private const val PREF_NAME = "SIGNATURE_MANAGER"
        private const val PRIV_KEY = "stored_data"

    }
}
