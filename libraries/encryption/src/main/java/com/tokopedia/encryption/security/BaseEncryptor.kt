package com.tokopedia.encryption.security

import android.util.Base64
import javax.crypto.SecretKey

abstract class BaseEncryptor {

    abstract fun generateKey(key: String): SecretKey

    abstract fun encrypt(message: String, secretKey: SecretKey,
                encoder: ((ByteArray) -> (String)) = { bytes ->
                    Base64.encodeToString(bytes, Base64.DEFAULT)
                }): String

    abstract fun decrypt(message: String, secretKey: SecretKey,
                decoder: ((String) -> (ByteArray)) = { bytes ->
                    Base64.decode(bytes, Base64.DEFAULT)
                }): String

    open fun encrypt(message: String, secretKey: SecretKey): String {
        return encrypt(message, secretKey) { bytes ->
            Base64.encodeToString(bytes, Base64.DEFAULT)
        }
    }

    open fun decrypt(message: String, secretKey: SecretKey): String {
        return decrypt(message, secretKey) { bytes ->
            Base64.decode(bytes, Base64.DEFAULT)
        }
    }
}