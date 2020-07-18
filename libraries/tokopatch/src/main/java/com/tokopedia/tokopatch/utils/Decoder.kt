package com.tokopedia.tokopatch.utils

import android.util.Base64
import java.math.BigInteger
import java.security.Key
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Author errysuprayogi on 02,July,2020
 */
class Decoder {
    companion object {
        var cipher: Cipher = Cipher.getInstance(
                "AES/GCM/NoPadding"
        )

        @Throws(Exception::class)
        fun decrypt(signature: String, encryptedText: String): ByteArray {
            var fingerprint  = md5(signature.toByteArray())
            val encryptedTextByte = Base64.decode(encryptedText, Base64.DEFAULT)
            val secretKey: Key = SecretKeySpec(fingerprint.toByteArray(), "AES")
            val iv = ByteArray(12)
            val ivParameterSpec = IvParameterSpec(iv)
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    secretKey,
                    ivParameterSpec
            )
            return cipher.doFinal(encryptedTextByte)
        }

        fun md5(byteArray: ByteArray): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(byteArray)).toString(16).padStart(32, '0')
        }
    }

}