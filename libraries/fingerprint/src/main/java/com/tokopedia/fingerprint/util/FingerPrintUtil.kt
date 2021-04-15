package com.tokopedia.fingerprint.util

import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import java.security.*
import java.security.spec.X509EncodedKeySpec

object FingerPrintUtil {

    fun getPublicKey(publicKey: PublicKey): String? {
        val encoded = Base64.encodeToString(publicKey.encoded, Base64.NO_WRAP)
        val publicKeyString = "-----BEGIN PUBLIC KEY-----\n$encoded\n-----END PUBLIC KEY-----"
        return Base64.encodeToString(publicKeyString.toByteArray(), Base64.NO_WRAP)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun generatePublicKey(context: Context): PublicKey? {
        val fingerprintManager = context.getSystemService(FingerprintManager::class.java)
        return if (fingerprintManager != null && fingerprintManager.isHardwareDetected && fingerprintManager.hasEnrolledFingerprints()) {
            var publicKey: PublicKey? = null
            try {
                val keyStore = KeyStore.getInstance(FingerprintConstant.ANDROID_KEY_STORE)
                keyStore.load(null)
                generateKeyPair(keyStore)
                publicKey = keyStore.getCertificate(FingerprintConstant.FINGERPRINT).publicKey
                val factory = KeyFactory.getInstance(publicKey.algorithm)
                val spec = X509EncodedKeySpec(publicKey.encoded)
                return factory.generatePublic(spec)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            publicKey
        } else {
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @Throws(KeyStoreException::class, NoSuchAlgorithmException::class, NoSuchProviderException::class, InvalidAlgorithmParameterException::class)
    fun generateKeyPair(keyStore: KeyStore) {
        //check if key is stored already, if null, create new key
        if (keyStore.getCertificate(FingerprintConstant.FINGERPRINT) == null || keyStore.getCertificate(FingerprintConstant.FINGERPRINT).publicKey == null) {
            val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, FingerprintConstant.ANDROID_KEY_STORE)
            val builder = KeyGenParameterSpec.Builder(FingerprintConstant.FINGERPRINT,
                    KeyProperties.PURPOSE_SIGN)
                    .setDigests(KeyProperties.DIGEST_SHA1)
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
            keyPairGenerator.initialize(builder.build())
            keyPairGenerator.generateKeyPair()
        }
    }
}
