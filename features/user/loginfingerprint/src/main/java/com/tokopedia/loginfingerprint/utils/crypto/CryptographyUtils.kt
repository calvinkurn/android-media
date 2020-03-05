package com.tokopedia.loginfingerprint.utils.crypto

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.tokopedia.loginfingerprint.constant.BiometricConstant
import com.tokopedia.loginfingerprint.data.model.FingerprintSignature
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import java.security.spec.InvalidKeySpecException
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

    init {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            initKeyStore()
            initCryptoObject()
        }
    }

    private fun initCryptoObject() {
        if (initSignature()) {
            signature?.run {
                _cryptoObject = FingerprintManagerCompat.CryptoObject(this)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun generatePublicKey(): PublicKey? {
        var publicKey: PublicKey? = null
        try {
            val keyStore = KeyStore.getInstance(BiometricConstant.ANDROID_KEY_STORE)
            keyStore.load(null)
            generateKeyPair(keyStore)

            publicKey = keyStore.getCertificate(BiometricConstant.FINGERPRINT).publicKey
            val factory = KeyFactory.getInstance(publicKey.algorithm)
            val spec = X509EncodedKeySpec(publicKey.encoded)
            return factory.generatePublic(spec)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidKeySpecException) {
            e.printStackTrace()
        }
        return publicKey
    }

    @Throws(KeyStoreException::class, NoSuchAlgorithmException::class, NoSuchProviderException::class, InvalidAlgorithmParameterException::class)
    override fun generateKeyPair(keyStore: KeyStore?) {
        //check if key is stored already, if null, create new key
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

    private fun initKeyStore() {
        try {
            keyStore = KeyStore.getInstance(BiometricConstant.ANDROID_KEY_STORE)
            keyStore?.load(null)

            generateKeyPair(keyStore)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun initSignature(): Boolean {
        try {
            keyStore?.load(null)
            val key = keyStore?.getKey(BiometricConstant.FINGERPRINT, null) as PrivateKey
            signature = Signature.getInstance(BiometricConstant.SHA_1_WITH_RSA)
            signature?.initSign(key)
            return true
        } catch (e: KeyPermanentlyInvalidatedException) {
            return false
        } catch (e: KeyStoreException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: UnrecoverableKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: IOException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: CertificateException) {
            throw RuntimeException("Failed to init Cipher", e)
        }
    }

    override fun getPublicKey(): String {
        val encoded = Base64.encodeToString(generatePublicKey()?.encoded, Base64.NO_WRAP)
        val publicKeyString = "-----BEGIN PUBLIC KEY-----\n$encoded\n-----END PUBLIC KEY-----"
        return publicKeyString
    }

    override fun generateFingerprintSignature(userId: String, deviceId: String): FingerprintSignature {
        val datetime = (System.currentTimeMillis()/1000).toString()
        return FingerprintSignature(signature = getSignature(userId + datetime + deviceId), datetime = datetime)
    }

    override fun getSignature(textToEncrypt: String): String {
        var signText = ""
        try {
            keyStore?.load(null)
            val privateKey = keyStore?.getKey(BiometricConstant.FINGERPRINT, null) as PrivateKey
            val signature = Signature.getInstance(BiometricConstant.SHA_1_WITH_RSA)
            signature.initSign(privateKey)
            signature.update(textToEncrypt.toByteArray())
            signText = Base64.encodeToString(signature.sign(),
                    Base64.NO_WRAP)
        } catch (e: SignatureException) {
            e.printStackTrace()
        } catch (e: UnrecoverableKeyException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return signText
    }

}