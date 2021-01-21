package com.tokopedia.seamless_login_common.utils

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and


/**
 * Created by Yoris Prayogo on 2019-11-11.
 * Copyright (c) 2019 PT. Tokopedia All rights reserved.
 */

class AESUtils {

    companion object {

        const val DEFAULT_ENCRYPTION_MODE = "AES"
        const val DEFAULT_ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding"

        const val SEAMLESS_KEY = "c4b5d4e9bd474f95a7dfd8e2ae9c7ae0"
        private val iv = ByteArray(12)
        private val cipher = Cipher.getInstance(DEFAULT_ENCRYPTION_ALGORITHM)

        var secureRandom = SecureRandom()

        private fun generateKey(key: String): SecretKey {
            return SecretKeySpec(key.toByteArray(), DEFAULT_ENCRYPTION_MODE)
        }

        private fun generateIV(): IvParameterSpec {
            secureRandom.nextBytes(iv)
            return IvParameterSpec(iv)
        }

        fun hashString(key: String): ByteArray {
            val md = MessageDigest.getInstance("SHA-1")
            val textBytes: ByteArray = key.toByteArray(charset("iso-8859-1"))
            md.update(textBytes, 0, textBytes.size)
            return md.digest()
        }

        fun byteArrayToHexString(b: ByteArray): String {
            var result = ""
            for (i in b.indices) {
                result += Integer.toString((b[i] and 0xff.toByte()) + 0x100, 16).substring(1)
            }
            return result
        }

        fun generateSecretKey(password: String, iv: ByteArray): SecretKey {
            val spec = PBEKeySpec(password.toCharArray(), iv, 65536, 128); // AES-128
            val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            val key = secretKeyFactory.generateSecret(spec).getEncoded();
            return SecretKeySpec(key, "AES");
        }

        fun encryptSeamless(data: ByteArray): String {
            return try {
                // Generate random IV / Nonce everytime
                secureRandom.nextBytes(iv)
                /*
                1. Hash Seamless Key with SHA-1 and append 12 bytes length array
                2. Create Secret key from hashed key
                3. Init cipher with secret key and IV
                */
                val hashKey = hashString(SEAMLESS_KEY) + ByteArray(iv.size)
                val secretKey = SecretKeySpec(hashKey, DEFAULT_ENCRYPTION_MODE)
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))

                //Encrypt the data
                val encryptedData = cipher.doFinal(data)
                //Prepend IV to encrypted data and encode with base64 with no wrap, no padding and url safe flags
                Base64.encodeToString(cipher.iv + encryptedData, Base64.NO_WRAP or Base64.URL_SAFE or Base64.NO_PADDING)
            }catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }

        fun encrypt(stringToEncrypt: String, key: String): String {
            return try {
                cipher.init(Cipher.ENCRYPT_MODE, generateKey(key), generateIV())
                val cipherIV = cipher.iv
                val byteArray = cipher.doFinal(stringToEncrypt.toByteArray(Charsets.UTF_8))
                return Base64.encodeToString(cipherIV, Base64.NO_WRAP) + Base64.encodeToString(byteArray, Base64.NO_WRAP)
            } catch (e: Exception) {
                e.printStackTrace()
                stringToEncrypt
            }
        }

        fun decrypt(stringToEncrypt: String, key: String, cipherIV: ByteArray): String {
            return try {
                cipher.init(Cipher.DECRYPT_MODE, generateKey(key), IvParameterSpec(cipherIV))
                String(cipher.doFinal(HexUtils.decodeHex(stringToEncrypt)), Charsets.UTF_8)
            } catch (e: Exception) {
                e.printStackTrace()
                stringToEncrypt
            }
        }
    }
}