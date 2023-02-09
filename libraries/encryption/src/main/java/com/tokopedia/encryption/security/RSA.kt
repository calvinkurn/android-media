package com.tokopedia.encryption.security

import android.util.Base64
import com.tokopedia.encryption.utils.Constants
import java.security.*
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.MGF1ParameterSpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource

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
                algorithm: String = Constants.RSA_PKCS1_ALGORITHM,
                encoder: ((ByteArray) -> (String)) = { bytes ->
                    Base64.encodeToString(bytes, Base64.DEFAULT)
                }): String {
        val cipher: Cipher = Cipher.getInstance(algorithm)
        if (Constants.RSA_OAEP_ALGORITHM == algorithm) {
            val oaepParameterSpec = OAEPParameterSpec(
                "SHA-256", "MGF1",
                MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT
            )
            cipher.init(Cipher.ENCRYPT_MODE, key, oaepParameterSpec)
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, key)
        }
        val encryptedBytes = cipher.doFinal(message.toByteArray(Charsets.UTF_8))
        return encoder(encryptedBytes)
    }

    fun stringToPublicKey(keyInString: String): RSAPublicKey {
        val publicBytes: ByteArray = Base64.decode(keyInString, Base64.DEFAULT)
        val keySpec = X509EncodedKeySpec(publicBytes)
        val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
        return (keyFactory.generatePublic(keySpec) as RSAPublicKey)
    }

    fun stringToPrivateKey(keyInString: String): RSAPrivateKey {
        val privateBytes: ByteArray = Base64.decode(keyInString, Base64.DEFAULT)
        val keySpec = PKCS8EncodedKeySpec(privateBytes)
        val keyFactory: KeyFactory = KeyFactory.getInstance("RSA")
        return (keyFactory.generatePrivate(keySpec) as RSAPrivateKey)
    }

    fun decrypt(message: String, key: PrivateKey,
                algorithm: String = Constants.RSA_PKCS1_ALGORITHM): String {
        val cipher: Cipher = Cipher.getInstance(algorithm)
        val decoder: ((String) -> (ByteArray)) = { bytes ->
            Base64.decode(bytes, Base64.DEFAULT)
        }
        if (Constants.RSA_OAEP_ALGORITHM == algorithm) {
            val oaepParameterSpec = OAEPParameterSpec(
                "SHA-256", "MGF1",
                MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT
            )
            cipher.init(Cipher.DECRYPT_MODE, key, oaepParameterSpec)
        } else {
            cipher.init(Cipher.DECRYPT_MODE, key)
        }
        return String(cipher.doFinal(decoder(message)), Charsets.UTF_8)
    }
}
