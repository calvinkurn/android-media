package com.tokopedia.encryption.security

import com.tokopedia.encryption.utils.Constants
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher

class RSA {

    lateinit var privateKey: PrivateKey
    lateinit var publicKey: PublicKey

    fun generateKeyPair() {
        val kp: KeyPair
        val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(Constants.RSA_METHOD)
        kpg.initialize(Constants.RSA_LENGTH)
        kp = kpg.genKeyPair()
        this.privateKey = kp.private
        this.publicKey = kp.public
    }

    fun encrypt(message: String, key: PublicKey,
                encoder: ((ByteArray) -> (String))): String {
        val cipher: Cipher = Cipher.getInstance(Constants.RSA_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedBytes = cipher.doFinal(message.toByteArray(Charsets.UTF_8))
        return encoder(encryptedBytes)
    }

    fun decrypt(message: String, key: PrivateKey,
                decoder: ((String) -> (ByteArray))): String {
        val cipher: Cipher = Cipher.getInstance(Constants.RSA_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, key)
        return String(cipher.doFinal(decoder(message)), Charsets.UTF_8)
    }
}