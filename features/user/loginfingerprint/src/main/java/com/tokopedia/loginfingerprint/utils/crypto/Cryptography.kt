package com.tokopedia.loginfingerprint.utils.crypto

import android.annotation.TargetApi
import android.os.Build
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.tokopedia.loginfingerprint.data.model.SignatureData
import java.security.*

/**
 * Created by Yoris Prayogo on 2020-02-27.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@TargetApi(Build.VERSION_CODES.M)
interface Cryptography {

    @androidx.annotation.RequiresApi(Build.VERSION_CODES.M)
    fun generatePublicKey(): PublicKey?

    @Throws(KeyStoreException::class, NoSuchAlgorithmException::class, NoSuchProviderException::class, InvalidAlgorithmParameterException::class)
    fun generateKeyPair(keyStore: KeyStore?)

    fun getPublicKey(): String

    fun generateFingerprintSignature(userId: String, deviceId: String): SignatureData

    fun getSignature(textToEncrypt: String, algorithm: String): String

    fun getCryptoObject(): FingerprintManagerCompat.CryptoObject?

    fun isInitialized(): Boolean
}