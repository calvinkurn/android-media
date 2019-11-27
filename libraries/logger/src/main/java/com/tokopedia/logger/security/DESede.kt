package com.tokopedia.logger.security

import com.tokopedia.logger.utils.byteToHex
import com.tokopedia.logger.utils.decodeHex
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class DESede {
    fun generateKey(): SecretKey {
        val kg = KeyGenerator.getInstance(SecurityConstants.DESede_METHOD)
        kg.init(SecurityConstants.DESede_KEY_SIZE)
        return kg.generateKey()
    }

    fun encrypt(message: String, key: SecretKey): String {
        val cipher = Cipher.getInstance(SecurityConstants.DESede_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val byteArray = cipher.doFinal(message.toByteArray(Charsets.UTF_8))
        return byteToHex(byteArray)
    }

    fun decrypt(message: String, key: SecretKey): String {
        val cipher = Cipher.getInstance(SecurityConstants.DESede_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, key)
        return String(cipher.doFinal(decodeHex(message)), Charsets.UTF_8)
    }
}