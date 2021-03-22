package com.tokopedia.devicefingerprint.submitdevice.utils

import android.util.Base64
import com.google.gson.Gson
import com.tokopedia.devicefingerprint.submitdevice.payload.DeviceInfoPayload
import com.tokopedia.encryption.security.AESEncryptorCBC
import com.tokopedia.encryption.security.RSA
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import javax.inject.Inject


class ContentCreator @Inject constructor(
        private val rsa: RSA
){

    private val publicKeyString: String = if (TokopediaUrl.getInstance().TYPE == Env.LIVE) {
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVm5b8oVlwM4LaKKNqt6WHL/fmuM+m+wRev+5ibuS6idviplK24OElYIencibA1T3bX1NBNBX++I6iiVr9D2VLU/RZ809u3TyCCD3jetvDiqQwfzJBiADVY/Q/Nk1zDrKA+2ZhPRTWwH0H0y5WLgju2nq0yKkoLHdCwrKxCQ9pdQIDAQAB"
    } else {
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCaDPpCjDLd15gwys1oYW+90ALTYKShRvz3ZzOlYLxfh6uBrWqb05UD+nilZ84z3UmHWZ+LqkE+GhifwI965iTUg4Oz67K52SJ19BXs8SpyLxRmovi539CiRCu6ZMQSAGl9GC/Zv1/tV8UFx2XuprRkwbUJU0oA0xXcP2sqCkN/EwIDAQAB"
    }

    fun createContent(payLoad: String): String {
        // Create key from 32-byte random byte array, encrypted with RSA using publicKey
        val secretKeyInString = RandomHelper.randomString(32)
        val publicKey = rsa.stringToPublicKey(publicKeyString)
        val key = rsa.encrypt(secretKeyInString, publicKey, "RSA/ECB/PKCS1Padding") { bytes ->
            Base64.encodeToString(bytes, Base64.DEFAULT)
        }

        // Create iv
        val ivInString = RandomHelper.randomNumber(16)

        // Encrypt deviceInfoPayload using AES-256-CBC, with iv and secret key generated earlier
        val aesEncryptorCBC = AESEncryptorCBC(ivInString)
        val secretKey = aesEncryptorCBC.generateKey(secretKeyInString)
        val encryptedPayload = aesEncryptorCBC.encrypt(payLoad, secretKey) { bytes ->
            Base64.encodeToString(bytes, Base64.DEFAULT)
        }

        return "${key}${ivInString}${encryptedPayload}".replace("\n", "")
    }

}
