package com.tokopedia.kyc_centralized.util

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

interface CipherProvider {
    fun initAesEncrypt(): Cipher

    fun initAesDecrypt(tempIv: ByteArray?): Cipher
}

class CipherProviderImpl: CipherProvider {

    private val SALT = "A%D*G-KaPdRgUkXp2s5v8y/B?E(H+MbQ"
    private val ALGORITHM = "AES/GCM/NoPadding"
    private val IV_SIZE = 128

    override fun initAesEncrypt(): Cipher {
        val aes = Cipher.getInstance(ALGORITHM)
        aes.init(Cipher.ENCRYPT_MODE, getKey())
        return aes
    }

    override fun initAesDecrypt(tempIv: ByteArray?): Cipher {
        val aes = Cipher.getInstance(ALGORITHM)
        val spec = GCMParameterSpec(IV_SIZE, tempIv)
        aes.init(Cipher.DECRYPT_MODE, getKey(), spec)

        return aes
    }

    private fun getKey(): SecretKey? {
        var secretKey: SecretKey? = null
        try {
            secretKey = SecretKeySpec(SALT.toBytes(), ALGORITHM)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return secretKey
    }

    private fun String.toBytes(): ByteArray {
        return this.toByteArray(Charsets.UTF_8)
    }
}
