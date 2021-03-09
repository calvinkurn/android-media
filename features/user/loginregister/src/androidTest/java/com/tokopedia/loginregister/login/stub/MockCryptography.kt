package com.tokopedia.loginregister.login.stub

import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.tokopedia.loginfingerprint.data.model.SignatureData
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import java.security.KeyStore
import java.security.PublicKey

/**
 * Created by Yoris Prayogo on 18/02/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */
class MockCryptography: Cryptography {
    override fun generatePublicKey(): PublicKey? = null

    override fun generateKeyPair(keyStore: KeyStore?) {

    }

    override fun getPublicKey(): String = ""

    override fun generateFingerprintSignature(userId: String, deviceId: String): SignatureData {
        return SignatureData()
    }

    override fun getSignature(textToEncrypt: String, algorithm: String): String = ""

    override fun getCryptoObject(): FingerprintManagerCompat.CryptoObject? = null

    override fun isInitialized(): Boolean = false
}