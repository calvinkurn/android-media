package com.tokopedia.encryption.security

import com.google.crypto.tink.Aead

interface AeadEncryptor {
    fun getAead(): Aead
    fun encrypt(message: String, associatedData: ByteArray? = null): String
    fun decrypt(base64EncryptedString: String, associatedData: ByteArray? = null): String
    fun delete()
}
