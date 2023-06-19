package com.tokopedia.common_electronic_money.util

import com.tokopedia.encryption.security.AESEncryptorGCM
import com.tokopedia.encryption.security.RSA
import com.tokopedia.encryption.utils.Constants
import javax.inject.Inject

class ElectronicMoneyEncryption @Inject constructor(
    private val rsa: RSA,
    private val aesEncryptorGCM: AESEncryptorGCM
) {

    /**
     * @param publicKeyString is key to encrypt AES key
     * @param payload is string that need to be encrypted payload with AES GCM Algorithm
     * @return Pair of encrypted AES Key and Encrypted Payload
     */
    fun createEncryptedPayload(publicKeyString: String, payload: String): Pair<String, String> {
        // 32 byte random array, will encrypted using RSA public key
        val aesRandomKey = randomString(AES_RANDOM_LENGTH)
        // Convert publicKeyString to RSAPublicKey
        val publicKey = rsa.stringToPublicKey(publicKeyString)
        // Encrypt AES Key with RSA Algorithm
        val encryptedAESKey = rsa.encrypt(aesRandomKey, publicKey, Constants.RSA_OAEP_ALGORITHM)

        // Generate Secret Key
        val secretKey = aesEncryptorGCM.generateKey(aesRandomKey)
        // Encrypt Payload with AES GCM
        val encryptedPayload = aesEncryptorGCM.encrypt(payload, secretKey)

        return Pair(encryptedAESKey, encryptedPayload)
    }


    /**
     * @param privateKeyString is key to decrypt AES key
     * @param encryptedAESKeyString is string that need to be decrypt payload with AES GCM Algorithm
     * @param encryptedPayload is string payload that need to be decrypted
     * @return Decrypted Payload
     */
    fun createDecryptedPayload(privateKeyString: String, encryptedAESKeyString: String, encryptedPayload: String): String {
        // Convert privateKeyString to RSAPrivateKey
        val privateKey = rsa.stringToPrivateKey(privateKeyString)
        // Decrypt AES Key with RSA Algorithm
        val decryptedKey = rsa.decrypt(encryptedAESKeyString, privateKey, Constants.RSA_OAEP_ALGORITHM)

        // Generate Secret Key
        val secretKey = aesEncryptorGCM.generateKey(decryptedKey)
        // Decrypt Payload
        val decryptPayload = aesEncryptorGCM.decrypt(encryptedPayload, secretKey)

        return decryptPayload
    }

    private fun randomString(length: Int): String {
        val source = ('0'..'9') + ('a'..'z') + ('A'..'Z') + "!@#$%^&*()-_=+".toList()
        return (1..length).map { source.random() }.joinToString("")
    }

    companion object {
        private const val AES_RANDOM_LENGTH = 32
    }
}
