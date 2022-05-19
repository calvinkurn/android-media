package com.tokopedia.encryption.security

import android.util.Base64
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

/**
 * Created by Yoris Prayogo on 10/02/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

object RsaUtils {

    private const val TRANSFORMATION = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING"
    private const val RSA_ALGORITHM = "RSA"

    fun encrypt(message: String, publicKey: String, isNeedHash: Boolean = false): String {
        try {
            if (message.isNotEmpty()) {
                var finalMsg = message
                if(isNeedHash){
                    finalMsg = message.sha256()
                }
                val convertedPublicKey = getPublicKey(cleanKey(publicKey))
                convertedPublicKey?.run {
                    return encryptWithRSA(finalMsg, convertedPublicKey)
                }
            }
        } catch (e: Exception){
            return ""
        }
        return ""
    }

    fun encryptWithSalt(message: String, publicKey: String, salt: String): String {
        try {
            if (message.isNotEmpty()) {
                val finalMsg = (message + salt).sha256()
                val decodedPublicKey: ByteArray = Base64.decode(
                    publicKey.toByteArray(),
                    Base64.DEFAULT
                )
                val cleanedPublicKey = cleanKey(String(decodedPublicKey))
                val decodedCleanKey = Base64.decode(cleanedPublicKey, Base64.DEFAULT)
                val keySpec = X509EncodedKeySpec(decodedCleanKey)
                val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
                val finalPublicKey = keyFactory.generatePublic(keySpec) as RSAPublicKey
                return encryptWithRSA(finalMsg, finalPublicKey)
            }
        } catch (e: Exception){
            return ""
        }
        return ""
    }

    private fun getPublicKey(pubKey: String): RSAPublicKey? {
        return try {
            val publicBytes: ByteArray = Base64.decode(
                    pubKey.toByteArray(Charsets.UTF_8),
                    Base64.DEFAULT
            )
            val keySpec = X509EncodedKeySpec(publicBytes)
            val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
            keyFactory.generatePublic(keySpec) as RSAPublicKey
        }catch (e: Exception){
            null
        }
    }

    private fun encryptWithRSA(message: String, publicKey: RSAPublicKey): String {
        return try {
            val cipher = Cipher.getInstance(TRANSFORMATION)

            cipher.init(Cipher.ENCRYPT_MODE, publicKey)

            val encryptedBytes = cipher.doFinal(message.toByteArray(Charsets.UTF_8))
            return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
        }catch (e: Exception){
            ""
        }
    }

    private fun cleanKey(key: String): String{
        return try {
            var newKey = key
            newKey = newKey.replace("\\n".toRegex(), "")
            newKey = newKey.replace("-----BEGIN PUBLIC KEY-----", "")
            newKey = newKey.replace("-----END PUBLIC KEY-----", "")
            newKey = newKey.replace("-----BEGIN RSA PUBLIC KEY-----", "")
            newKey = newKey.replace("-----END RSA PUBLIC KEY-----", "")
            newKey
        }catch (e: Exception){
            ""
        }
    }
}