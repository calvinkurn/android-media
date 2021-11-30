package com.tokopedia.loginfingerprint.utils.crypto

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.tokopedia.graphql.util.Const
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.loginfingerprint.constant.BiometricConstant
import com.tokopedia.loginfingerprint.data.model.SignatureData
import java.security.*
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec

/**
 * Created by Yoris Prayogo on 2020-02-05.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@TargetApi(Build.VERSION_CODES.M)
class CryptographyUtils: Cryptography {

    private var signature: Signature? = null
    private var keyStore: KeyStore? = null

    private var _cryptoObject: FingerprintManagerCompat.CryptoObject? = null

    override fun getCryptoObject(): FingerprintManagerCompat.CryptoObject? = _cryptoObject


    private var signatureInitialized = false


    companion object {
        private const val PUBLIC_KEY_PREFIX = "-----BEGIN PUBLIC KEY-----\n"
        private const val PUBLIC_KEY_SUFFIX = "-----END PUBLIC KEY-----"

        private const val KEY_SIZE = 4096
        private const val DATE_DIVIDER = 1000
    }

    init {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            initKeyStore()
            initCryptoObject()
        }
    }

    override fun isInitialized(): Boolean = signatureInitialized

    private fun initCryptoObject() {
        signatureInitialized = initSignature()
        if (signatureInitialized) {
            signature?.run {
                _cryptoObject = FingerprintManagerCompat.CryptoObject(this)
            }
        }
    }

    override fun generatePublicKey(): RSAPublicKey? {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var publicKey: RSAPublicKey? = null
            try {
                val keyStore = KeyStore.getInstance(BiometricConstant.ANDROID_KEY_STORE)
                keyStore.load(null)
                generateKeyPair(keyStore)
                publicKey = keyStore.getCertificate(BiometricConstant.FINGERPRINT).publicKey as RSAPublicKey
                val factory = KeyFactory.getInstance("RSA")
                val spec = X509EncodedKeySpec(publicKey.encoded)
                return factory.generatePublic(spec) as RSAPublicKey
            } catch (e: Exception) {
                log("GeneratePublicKey_Exception", data = "public_key=${publicKey.toNullStringIfNull()}#keystore=${keyStore.toNullStringIfNull()}", e)
            }
        }
        return null
    }

    override fun generateKeyPair(keyStore: KeyStore?) {
        //check if key is stored already, if null, create new key
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (keyStore?.getCertificate(BiometricConstant.FINGERPRINT) == null ||
                        keyStore.getCertificate(BiometricConstant.FINGERPRINT).publicKey == null) {
                    val keyPairGenerator = KeyPairGenerator.getInstance(
                        KeyProperties.KEY_ALGORITHM_RSA,
                        BiometricConstant.ANDROID_KEY_STORE
                    )
                    val builder = KeyGenParameterSpec.Builder(
                        BiometricConstant.FINGERPRINT,
                        KeyProperties.PURPOSE_SIGN
                    )
                    .setKeySize(KEY_SIZE)
                    .setDigests(KeyProperties.DIGEST_SHA256)
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                    keyPairGenerator.initialize(builder.build())
                    keyPairGenerator.generateKeyPair()
                }
            } catch (e: Exception) {
                log("GenerateKeyPair_Exception", data = "keystore=${keyStore.toNullStringIfNull()}", e)
            }
        }
    }

    private fun initKeyStore() {
        try {
            keyStore = KeyStore.getInstance(BiometricConstant.ANDROID_KEY_STORE)
            keyStore?.load(null)
            generateKeyPair(keyStore)
        } catch (e: Exception) {
            log("InitKeystore_Exception", data = "keystore=${keyStore?.toNullStringIfNull()}", e)
        }
    }

    private fun initSignature(): Boolean {
        return try {
            keyStore?.load(null)
            val key = keyStore?.getKey(BiometricConstant.FINGERPRINT, null) as? PrivateKey
            signature = Signature.getInstance(BiometricConstant.SHA_256_WITH_RSA)
            signature?.initSign(key)
            true
        } catch (e: Exception) {
            log("InitSignature_Exception", data = "signature=${signature?.toNullStringIfNull()}#keystore=${keyStore.toNullStringIfNull()}", e)
            false
        }
    }

    override fun getPublicKey(): String {
        val pubKey = generatePublicKey()
        pubKey?.run {
            val encoded = Base64.encodeToString(this.encoded, Base64.DEFAULT)
            return "$PUBLIC_KEY_PREFIX${encoded}$PUBLIC_KEY_SUFFIX"
        }
        return ""
    }

    override fun generateFingerprintSignature(uniqueId: String, deviceId: String): SignatureData {
        val datetime = (System.currentTimeMillis()/DATE_DIVIDER).toString()
        return SignatureData(signature = getSignature
            (uniqueId + datetime + deviceId,
                BiometricConstant.SHA_256_WITH_RSA
            ), datetime = datetime)
    }

    override fun getSignature(textToEncrypt: String, algorithm: String): String {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var signText = ""
            try {
                keyStore?.load(null)
                val privateKey = keyStore?.getKey(BiometricConstant.FINGERPRINT, null) as? PrivateKey
                val signature = Signature.getInstance(algorithm)
                signature.initSign(privateKey)
                signature.update(textToEncrypt.toByteArray())
                signText = Base64.encodeToString(signature.sign(),
                        Base64.NO_WRAP)
            } catch (e: Exception) {
                log("GetSignature_Exception", data =
                "text_to_encrypt=$textToEncrypt#keystore=${keyStore.toNullStringIfNull()}", e)
            }
            return signText
        }
        return ""
    }

    private fun Any?.toNullStringIfNull(): String {
        return if(this == null) "null" else "not null"
    }

    private fun log(type: String, data: String , throwable: Exception) {
        val msg = mapOf(
            "type" to type,
            "data" to data,
            "exception" to Log.getStackTraceString(throwable).take(Const.GQL_ERROR_MAX_LENGTH)
        )

        ServerLogger.log(
            Priority.P1,
            "CRYPTOGRAPHY_ERROR",
            msg
        )
    }
}