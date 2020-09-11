package com.tokopedia.devicefingerprint.utils

import android.util.Base64
import com.google.gson.Gson
import com.tokopedia.devicefingerprint.payload.DeviceInfoPayload
import com.tokopedia.encryption.security.AESEncryptorCBC
import com.tokopedia.encryption.security.RSA
import javax.inject.Inject


class ContentCreator @Inject constructor(
        private val gson: Gson,
        private val rsa: RSA
){

    // TODO Should be moved somewhere else. This key is for staging only.
    private val publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCaDPpCjDLd15gwys1oYW+90ALTYKShRvz3ZzOlYLxfh6uBrWqb05UD+nilZ84z3UmHWZ+LqkE+GhifwI965iTUg4Oz67K52SJ19BXs8SpyLxRmovi539CiRCu6ZMQSAGl9GC/Zv1/tV8UFx2XuprRkwbUJU0oA0xXcP2sqCkN/EwIDAQAB"

    fun createContent(deviceInfoPayload: DeviceInfoPayload): String {
        // Create key from 32-byte random byte array, encrypted with RSA using publicKey
        val secretKeyInString = RandomHelper.randomString(32)
        val publicKey = rsa.stringToPublicKey(publicKeyString)
        val key = rsa.encrypt(secretKeyInString, publicKey, "RSA/ECB/PKCS1Padding") { bytes ->
            Base64.encodeToString(bytes, Base64.DEFAULT)
        }

        // Create iv
        val ivInString = RandomHelper.randomNumber(16)

        // Encrypt deviceInfoPayload using AES-256-CBC, with iv and secret key generated earlier
        val deviceInfoPayloadInJson = gson.toJson(deviceInfoPayload)

        // Encrypt deviceInfoPayload using AES-256-CBC, with iv and secret key generated earlier
        val aesEncryptorCBC = AESEncryptorCBC(ivInString)
        val secretKey = aesEncryptorCBC.generateKey(secretKeyInString)
        val encryptedPayload = aesEncryptorCBC.encrypt(deviceInfoPayloadInJson, secretKey) { bytes ->
            Base64.encodeToString(bytes, Base64.DEFAULT)
        }

        return "${key}${ivInString}${encryptedPayload}".replace("\n", "")
    }

}
