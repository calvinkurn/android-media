package com.tokopedia.loginfingerprint.utils.crypto

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.tokopedia.loginfingerprint.constant.BiometricConstant
import com.tokopedia.loginfingerprint.data.model.SignatureData
import java.security.*
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

    private val PUBLIC_KEY_PREFIX = "-----BEGIN PUBLIC KEY-----\n"
    private val PUBLIC_KEY_SUFFIX = "\n-----END PUBLIC KEY-----"

    private var signatureInitialized = false

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

    override fun generatePublicKey(): PublicKey? {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var publicKey: PublicKey? = null
            try {
                val keyStore = KeyStore.getInstance(BiometricConstant.ANDROID_KEY_STORE)
                keyStore.load(null)
                generateKeyPair(keyStore)

                publicKey = keyStore.getCertificate(BiometricConstant.FINGERPRINT).publicKey
                val factory = KeyFactory.getInstance(publicKey.algorithm)
                val spec = X509EncodedKeySpec(publicKey.encoded)
                return factory.generatePublic(spec)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return publicKey
        }
        return null
    }

    @Throws(KeyStoreException::class, NoSuchAlgorithmException::class, NoSuchProviderException::class, InvalidAlgorithmParameterException::class)
    override fun generateKeyPair(keyStore: KeyStore?) {
        //check if key is stored already, if null, create new key
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (keyStore?.getCertificate(BiometricConstant.FINGERPRINT) == null || keyStore.getCertificate(BiometricConstant.FINGERPRINT).publicKey == null) {
                val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, BiometricConstant.ANDROID_KEY_STORE)
                val builder = KeyGenParameterSpec.Builder(BiometricConstant.FINGERPRINT,
                        KeyProperties.PURPOSE_SIGN)
                        .setDigests(KeyProperties.DIGEST_SHA1)
                        .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                keyPairGenerator.initialize(builder.build())
                keyPairGenerator.generateKeyPair()
            }
        }
    }

    private fun initKeyStore() {
        try {
            keyStore = KeyStore.getInstance(BiometricConstant.ANDROID_KEY_STORE)
            keyStore?.load(null)

            generateKeyPair(keyStore)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initSignature(): Boolean {
        return try {
            keyStore?.load(null)
            val key = keyStore?.getKey(BiometricConstant.FINGERPRINT, null) as? PrivateKey
            signature = Signature.getInstance(BiometricConstant.SHA_1_WITH_RSA)
            signature?.initSign(key)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun getPublicKey(): String {
        val pubKey = generatePublicKey()
        pubKey?.run {
            val encoded = Base64.encodeToString(this.encoded, Base64.NO_WRAP)
            return "$PUBLIC_KEY_PREFIX$encoded$PUBLIC_KEY_SUFFIX"
        }
        return ""
    }

    override fun generateFingerprintSignature(adsId: String, deviceId: String): SignatureData {
        val datetime = (System.currentTimeMillis()/1000).toString()
        return SignatureData(signature = getSignature(adsId + datetime + deviceId, BiometricConstant.SHA_1_WITH_RSA), datetime = datetime)
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
                e.printStackTrace()
            }

            return signText
        }
        return ""
    }

}