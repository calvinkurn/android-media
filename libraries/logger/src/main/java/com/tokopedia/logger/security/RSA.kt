package com.tokopedia.logger.security

import com.tokopedia.logger.utils.Constants
import com.tokopedia.logger.utils.byteToHex
import com.tokopedia.logger.utils.decodeHex
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
        val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(SecurityConstants.RSA_METHOD)
        kpg.initialize(SecurityConstants.RSA_LENGTH)
        kp = kpg.genKeyPair()
        this.privateKey = kp.private
        this.publicKey = kp.public
    }

    fun encrypt(message: String, key: PublicKey): String {
        val cipher: Cipher = Cipher.getInstance(SecurityConstants.RSA_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedBytes = cipher.doFinal(message.toByteArray(Charsets.UTF_8))
        return byteToHex(encryptedBytes)
    }

    fun decrypt(message: String, key: PrivateKey): String {
        val cipher: Cipher = Cipher.getInstance(SecurityConstants.RSA_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, key)
        return String(cipher.doFinal(decodeHex(message)), Charsets.UTF_8)
    }
}