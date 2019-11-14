package com.tokopedia.seamless_login.utils

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by Yoris Prayogo on 2019-11-11.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

class AESUtils {

    companion object {

        const val DEFAULT_ENCRYPTION_MODE = "AES"
        const val DEFAULT_ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding"

        private val iv = ByteArray(12)
        private val cipher = Cipher.getInstance(DEFAULT_ENCRYPTION_ALGORITHM)

        var secureRandom = SecureRandom()

        private fun generateKey(key: String): SecretKey {
            return SecretKeySpec(key.toByteArray(Charsets.UTF_8), DEFAULT_ENCRYPTION_MODE)
        }

        private fun generateIV(): IvParameterSpec {
            secureRandom.nextBytes(iv)
            return IvParameterSpec(iv)
        }

        fun encrypt(stringToEncrypt: String, key: String): String {
            return try {
                cipher.init(Cipher.ENCRYPT_MODE, generateKey(key), generateIV())
                val cipherIV = cipher.iv
                val byteArray = cipher.doFinal(stringToEncrypt.toByteArray(Charsets.UTF_8))
                return Base64.encodeToString(cipherIV, Base64.NO_WRAP) + Base64.encodeToString(byteArray, Base64.NO_WRAP)
            }catch (e: Exception){
                e.printStackTrace()
                stringToEncrypt
            }
        }

        fun decrypt(stringToEncrypt: String, key: String, cipherIV: ByteArray): String {
            return try {
                cipher.init(Cipher.DECRYPT_MODE, generateKey(key), IvParameterSpec(cipherIV))
                String(cipher.doFinal(HexUtils.decodeHex(stringToEncrypt)), Charsets.UTF_8)
            }catch (e: Exception){
                e.printStackTrace()
                stringToEncrypt
            }
        }
    }
}