package com.tokopedia.common_electronic_money.util

import android.util.Base64
import com.tokopedia.encryption.security.AESEncryptorGCM
import com.tokopedia.encryption.security.RSA
import com.tokopedia.encryption.utils.Constants
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject


class ElectronicMoneyEncryption @Inject constructor(
    private val rsa: RSA,
    private val aesEncryptorGCM: AESEncryptorGCM
) {

    /**
     * @param rawPublicKeyString is key to encrypt AES key
     * @param payload is string that need to be encrypted payload with AES GCM Algorithm
     * @return Pair of encrypted AES Key and Encrypted Payload
     */
    fun createEncryptedPayload(rawPublicKeyString: String, payload: String): Pair<String, String> {
        // 32 byte random array, will encrypted using RSA public key
        val aesRandomKey = randomString(AES_RANDOM_LENGTH)
        // Convert publicKeyString to RSAPublicKey
        val publicKey = rsa.stringToPublicKey(rawPublicKeyString)
        // Encrypt AES Key with RSA Algorithm
        val encryptedAESKey = rsa.encrypt(aesRandomKey, publicKey, Constants.RSA_OAEP_ALGORITHM).replace("\n", "")

        // Generate Secret Key
        val secretKey = aesEncryptorGCM.generateKey(aesRandomKey)
        // Encrypt Payload with AES GCM
        val encryptedPayload = aesEncryptorGCM.encrypt(payload, secretKey).replace("\n", "")

        return Pair(encryptedAESKey, encryptedPayload)
    }


    /**
     * @param rawPrivateKeyString is key to decrypt AES key
     * @param encryptedAESKeyString is string that need to be decrypt payload with AES GCM Algorithm
     * @param encryptedPayload is string payload that need to be decrypted
     * @return Decrypted Payload
     */
    fun createDecryptedPayload(rawPrivateKeyString: String, encryptedAESKeyString: String, encryptedPayload: String): String {
        // Convert privateKeyString to RSAPrivateKey
        val privateKey = rsa.stringToPrivateKey(rawPrivateKeyString)
        // Decrypt AES Key with RSA Algorithm
        val decryptedKey = rsa.decrypt(encryptedAESKeyString, privateKey, Constants.RSA_OAEP_ALGORITHM)

        // Collect nonce and decode, because we cannot split the string and decode it, we need to decode it that split it
        val payloadByte: ByteArray = Base64.decode(encryptedPayload, Base64.DEFAULT)
        val nonce = payloadByte.copyOfRange(payloadByte.size - NONCE_SIZE, payloadByte.size)
        val decode = payloadByte.copyOfRange(Int.ZERO, payloadByte.size - NONCE_SIZE)

        // Create SecretKey and GCM Param
        val secretKey: SecretKey = SecretKeySpec(decryptedKey.toByteArray(Charsets.UTF_8), Constants.AES_METHOD)
        val params = GCMParameterSpec(GCM_TLEN, nonce)

        // Create Cipher
        val cipher = Cipher.getInstance(Constants.GCM_256_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, params)

        // Decrypt encrypted message
        val plaintext = cipher.doFinal(decode)
        return String(plaintext,  Charsets.UTF_8)
    }

    private fun randomString(length: Int): String {
        val source = ('0'..'9') + ('a'..'z') + ('A'..'Z') + "!@#$%^&*()-_=+".toList()
        return (Int.ONE..length).map { source.random() }.joinToString("")
    }

    companion object {
        private const val AES_RANDOM_LENGTH = 32
        private const val NONCE_SIZE = 12
        private const val GCM_TLEN = 128
    }
}
