package com.tokopedia.devicefingerprint

import com.google.gson.Gson
import com.tokopedia.devicefingerprint.model.DeviceInfoPayload
import com.tokopedia.encryption.security.AESEncryptorCBC
import com.tokopedia.encryption.security.RSA
import com.tokopedia.encryption.utils.Utils
import javax.inject.Inject
import kotlin.random.Random

class ContentCreator @Inject constructor(
        private val gson: Gson,
        private val rsa: RSA
){

    // TODO Should be moved somewhere else. This key is for staging only.
    private val publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCaDPpCjDLd15gwys1oYW+90ALTYKShRvz3ZzOlYLxfh6uBrWqb05UD+nilZ84z3UmHWZ+LqkE+GhifwI965iTUg4Oz67K52SJ19BXs8SpyLxRmovi539CiRCu6ZMQSAGl9GC/Zv1/tV8UFx2XuprRkwbUJU0oA0xXcP2sqCkN/EwIDAQAB"

    fun createContent(deviceInfoPayload: DeviceInfoPayload): String {
        // Create key from 32-byte random string, encrypted with RSA using publicKey
        val secretKeyInString = randomString(32)
        val key = rsa.encrypt(secretKeyInString, publicKey, Utils::byteToHex)

        // Create iv
        val ivInString = randomNumber(16)

        // Encrypt deviceInfoPayload using AES-256-CBC, with iv and secret key generated earlier
        val aesEncryptorCBC = AESEncryptorCBC(ivInString)
        val deviceInfoPayloadInJson = gson.toJson(deviceInfoPayload)
        val secretKey = aesEncryptorCBC.generateKey(secretKeyInString)
        val encryptedPayload = aesEncryptorCBC.encrypt(deviceInfoPayloadInJson, secretKey, Utils::byteToHex)

        return "${key}${ivInString}${encryptedPayload}"
    }

    private fun randomNumber(length: Int): String {
        val source = ('0'..'9')
        return (1..length).map { source.random() }.joinToString("")
    }

    private fun randomString(lengthInBytes: Int): String {
        var byteArray = ByteArray(lengthInBytes)
        byteArray = Random.nextBytes(byteArray)
        return String(byteArray)
    }
}
