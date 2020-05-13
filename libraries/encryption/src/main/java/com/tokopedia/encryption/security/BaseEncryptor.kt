package com.tokopedia.encryption.security

import com.tokopedia.encryption.utils.Utils
import javax.crypto.SecretKey

abstract class BaseEncryptor {

    abstract fun generateKey(key: String): SecretKey

    abstract fun encrypt(message: String, secretKey: SecretKey,
                encoder: ((ByteArray) -> (String)) = Utils::byteToHex): String

    abstract fun decrypt(message: String, secretKey: SecretKey,
                decoder: ((String) -> (ByteArray)) = Utils::decodeHex): String

    open fun encrypt(message: String, secretKey: SecretKey): String {
        return encrypt(message, secretKey, Utils::byteToHex)
    }

    open fun decrypt(message: String, secretKey: SecretKey): String {
        return decrypt(message, secretKey, Utils::decodeHex)
    }
}