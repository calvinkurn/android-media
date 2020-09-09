package com.tokopedia.devicefingerprint.utils

import com.google.gson.Gson
import com.tokopedia.devicefingerprint.payload.DeviceInfoPayload
import com.tokopedia.encryption.security.AESEncryptorCBC
import com.tokopedia.encryption.security.RSA
import com.tokopedia.encryption.utils.Utils
import javax.inject.Inject

class ContentCreator @Inject constructor(
        private val gson: Gson,
        private val rsa: RSA
){

    // TODO Should be moved somewhere else. This key is for staging only.
    private val publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCaDPpCjDLd15gwys1oYW+90ALTYKShRvz3ZzOlYLxfh6uBrWqb05UD+nilZ84z3UmHWZ+LqkE+GhifwI965iTUg4Oz67K52SJ19BXs8SpyLxRmovi539CiRCu6ZMQSAGl9GC/Zv1/tV8UFx2XuprRkwbUJU0oA0xXcP2sqCkN/EwIDAQAB"

    fun createContent(deviceInfoPayload: DeviceInfoPayload): String {
        // Create key from 32-byte random byte array, encrypted with RSA using publicKey
        val secretKeyInByteArray = RandomHelper.randomByteArray(32)
        val key = rsa.encrypt(secretKeyInByteArray, publicKey, Utils::byteToHex)

        // Create iv
        val ivInString = RandomHelper.randomNumber(16)

        // Encrypt deviceInfoPayload using AES-256-CBC, with iv and secret key generated earlier
        val aesEncryptorCBC = AESEncryptorCBC(ivInString)
        val deviceInfoPayloadInJson = gson.toJson(deviceInfoPayload)
        val secretKey = aesEncryptorCBC.generateKey(secretKeyInByteArray)
        val encryptedPayload = aesEncryptorCBC.encrypt(deviceInfoPayloadInJson, secretKey, Utils::byteToHex)

        return "${key}${ivInString}${encryptedPayload}"
    }

}
