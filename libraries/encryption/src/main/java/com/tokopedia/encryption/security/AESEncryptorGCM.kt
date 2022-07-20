package com.tokopedia.encryption.security

import com.tokopedia.encryption.utils.Constants
import com.tokopedia.encryption.utils.Constants.GCM_256_ALGORITHM
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESEncryptorGCM(
    private val random12ByteNonce: String,
    private val appendNonce: Boolean = true
) : BaseEncryptor() {

    override fun generateKey(key: String): SecretKey {
        return SecretKeySpec(key.toByteArray(Charsets.UTF_8), Constants.AES_METHOD)
    }

    override fun encrypt(
        message: String, secretKey: SecretKey,
        encoder: ((ByteArray) -> (String))
    ): String {
        val cipher = Cipher.getInstance(GCM_256_ALGORITHM)
        val nonce = random12ByteNonce.toByteArray()
        cipher.init(
            Cipher.ENCRYPT_MODE, secretKey,
            GCMParameterSpec(128, nonce)
        )
        val byteArray = cipher.doFinal(message.toByteArray(Charsets.UTF_8))

        return if (appendNonce) {
            encoder(byteArray + nonce)
        } else {
            encoder(byteArray)
        }
    }

    override fun decrypt(
        message: String, secretKey: SecretKey,
        decoder: ((String) -> (ByteArray))
    ): String {
        val cipher = Cipher.getInstance(GCM_256_ALGORITHM)
        cipher.init(
            Cipher.DECRYPT_MODE, secretKey,
            GCMParameterSpec(128, random12ByteNonce.toByteArray())
        )
        val decodedMessage: ByteArray = decoder(message)
        return if (appendNonce) {
            String(
                cipher.doFinal(
                    decodedMessage.copyOfRange
                        (0, decodedMessage.size - random12ByteNonce.length)
                ),
                Charsets.UTF_8
            )
        } else {
            String(cipher.doFinal(decodedMessage), Charsets.UTF_8)
        }
    }

}