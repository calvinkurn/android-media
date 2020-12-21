package com.tokopedia.encryption.security

import android.util.Base64
import com.tokopedia.encryption.utils.Constants
import java.security.*
import java.security.spec.X509EncodedKeySpec
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

    fun encrypt(message: String,
                key: PublicKey,
                algorithm: String = Constants.RSA_ALGORITHM,
                encoder: ((ByteArray) -> (String))): String {
        val cipher: Cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val encryptedBytes = cipher.doFinal(message.toByteArray(Charsets.UTF_8))
        return encoder(encryptedBytes)
    }

    fun stringToPublicKey(keyInString: String): PublicKey {
        val publicBytes: ByteArray = Base64.decode(keyInString, Base64.DEFAULT)
        val keySpec = X509EncodedKeySpec(publicBytes)
        val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec)
    }

    fun decrypt(message: String, key: PrivateKey,
                decoder: ((String) -> (ByteArray))): String {
        val cipher: Cipher = Cipher.getInstance(Constants.RSA_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, key)
        return String(cipher.doFinal(decoder(message)), Charsets.UTF_8)
    }
}
