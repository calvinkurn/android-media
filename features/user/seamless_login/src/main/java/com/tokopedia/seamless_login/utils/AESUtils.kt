package com.tokopedia.seamless_login.utils

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
        const val DEFAULT_ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding"
        const val SECRET_IV = "P0bH8$2Veq)1gmcA"

        private val iv = generateIV()
        private val cipher = Cipher.getInstance(DEFAULT_ENCRYPTION_ALGORITHM)

        private fun generateKey(key: String): SecretKey {
            return SecretKeySpec(key.toByteArray(Charsets.UTF_8), DEFAULT_ENCRYPTION_MODE)
        }

        private fun generateIV(): IvParameterSpec {
            return IvParameterSpec(SECRET_IV.toByteArray())
        }

        fun encrypt(stringToEncrypt: String, key: String): String {
            return try {
                cipher.init(Cipher.ENCRYPT_MODE, generateKey(key), iv)
                val byteArray = cipher.doFinal(stringToEncrypt.toByteArray(Charsets.UTF_8))
                HexUtils.byteToHex(byteArray)
            }catch (e: Exception){
                e.printStackTrace()
                key
            }
        }

        fun decrypt(stringToEncrypt: String, key: String): String {
            cipher.init(Cipher.DECRYPT_MODE, generateKey(key), iv)
            return String(cipher.doFinal(HexUtils.decodeHex(stringToEncrypt)), Charsets.UTF_8)
        }
    }
}