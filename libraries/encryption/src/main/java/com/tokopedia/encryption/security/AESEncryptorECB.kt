package com.tokopedia.encryption.security

import com.tokopedia.encryption.utils.Constants
import com.tokopedia.encryption.utils.Constants.ECB_ALGORITHM
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class AESEncryptorECB() : BaseEncryptor() {

    override fun generateKey(key: String): SecretKey {
        return SecretKeySpec(key.toByteArray(Charsets.UTF_8), Constants.AES_METHOD)
    }

    override fun encrypt(message: String, secretKey: SecretKey,
                         encoder: ((ByteArray) -> (String))): String {
        val cipher = Cipher.getInstance(ECB_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val byteArray = cipher.doFinal(message.toByteArray(Charsets.UTF_8))
        return encoder(byteArray)
    }

    override fun decrypt(message: String, secretKey: SecretKey,
                         decoder: ((String) -> (ByteArray))): String {
        val cipher = Cipher.getInstance(ECB_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        return String(cipher.doFinal(decoder(message)), Charsets.UTF_8)
    }

}