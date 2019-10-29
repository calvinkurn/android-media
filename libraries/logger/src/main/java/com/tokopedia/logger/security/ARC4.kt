package com.tokopedia.logger.security

import com.tokopedia.logger.utils.byteToHex
import com.tokopedia.logger.utils.decodeHex
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

class ARC4 {

    fun generateKey(key: String): SecretKey {
        return SecretKeySpec(key.toByteArray(Charsets.UTF_8), SecurityConstants.ARC4_METHOD);
    }

    fun encrypt(message: String, key: SecretKey): String {
        val cipher = Cipher.getInstance(SecurityConstants.ARC4_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val byteArray = cipher.doFinal(message.toByteArray(Charsets.UTF_8))
        return byteToHex(byteArray)
    }

    fun decrypt(message: String, key: SecretKey): String {
        val cipher = Cipher.getInstance(SecurityConstants.ARC4_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, key)
        return String(cipher.doFinal(decodeHex(message)), Charsets.UTF_8)
    }
}