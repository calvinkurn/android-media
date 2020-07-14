package com.tokopedia.encryption.security

import com.tokopedia.encryption.utils.Constants
import com.tokopedia.encryption.utils.Utils.byteToHex
import com.tokopedia.encryption.utils.Utils.decodeHex
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class DESede(private val algorithm: String = Constants.DESede_ALGORITHM): BaseEncryptor() {

    override fun generateKey(key: String): SecretKey {
        val kg = KeyGenerator.getInstance(Constants.DESede_METHOD)
        kg.init(Constants.DESede_KEY_SIZE)
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