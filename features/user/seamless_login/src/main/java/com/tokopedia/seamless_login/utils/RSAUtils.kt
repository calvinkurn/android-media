package com.tokopedia.seamless_login.utils

import android.util.Base64
import com.tokopedia.seamless_login.internal.SeamlessLoginConstant.RSA_ALGORITHM
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

/**
 * Created by Yoris Prayogo on 2019-11-07.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

class RSAUtils {

    companion object {

        private fun generatePubKey(pubKey: String): PublicKey? {
            try {
                val keyBytes = Base64.decode(pubKey, Base64.DEFAULT)
                val spec = X509EncodedKeySpec(keyBytes)
                val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
                return keyFactory.generatePublic(spec)
            }catch (e: Exception){
                e.printStackTrace()
            }

            return null
        }

        fun encrypt(message: String, pubKey: String): String {
            try {
                val cipher: Cipher = Cipher.getInstance(RSA_ALGORITHM)
                cipher.init(Cipher.ENCRYPT_MODE, generatePubKey(pubKey))
                val encryptedBytes = cipher.doFinal(message.toByteArray(Charsets.UTF_8))
                return byteToHex(Base64.encode(encryptedBytes, Base64.DEFAULT))
            }catch (e: Exception){
                e.printStackTrace()
            }
            return ""
        }

        private fun byteToHex(byteArray: ByteArray): String {
            var hexString = ""
            for (byte in byteArray) {
                hexString += String.format("%02X", byte)
            }
            return hexString
        }
    }
}