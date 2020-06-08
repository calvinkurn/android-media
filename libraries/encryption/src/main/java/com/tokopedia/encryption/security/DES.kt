package com.tokopedia.encryption.security

import com.tokopedia.encryption.utils.Constants
import com.tokopedia.encryption.utils.Utils
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class DES(private val algorithm: String = Constants.DES_ALGORITHM): BaseEncryptor() {

    override fun generateKey(key: String): SecretKey {
        val kg = KeyGenerator.getInstance(algorithm)
        kg.init(Constants.DES_KEY_SIZE)
        return kg.generateKey()
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