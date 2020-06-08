package com.tokopedia.encryption.security

import com.tokopedia.encryption.utils.Constants
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class ARC4(private val algorithm: String = Constants.ARC4_ALGORITHM) : BaseEncryptor() {

    override fun generateKey(key: String): SecretKey {
        return SecretKeySpec(key.toByteArray(Charsets.UTF_8), algorithm)
    }

    override fun encrypt(message: String, secretKey: SecretKey,
                         encoder: ((ByteArray) -> (String))): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val byteArray = cipher.doFinal(message.toByteArray(Charsets.UTF_8))
        return encoder(byteArray)
    }

    override fun decrypt(message: String, secretKey: SecretKey,
                         decoder: ((String) -> (ByteArray))): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        return String(cipher.doFinal(decoder(message)), Charsets.UTF_8)
    }
}