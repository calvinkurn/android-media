package com.tokopedia.encryption.security

import javax.crypto.SecretKey

interface BaseEncryptor {

    fun generateKey(key: String): SecretKey

    fun encrypt(message: String, secretKey: SecretKey): String

    fun decrypt(message: String, secretKey: SecretKey): String
}