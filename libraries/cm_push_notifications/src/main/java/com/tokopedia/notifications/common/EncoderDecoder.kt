package com.tokopedia.notifications.common

import android.util.Base64

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncoderDecoder {

    fun Encrypt(text: String, initialVector: String): String {
        val raw = byteArrayOf('g'.toByte(), 'g'.toByte(), 'g'.toByte(), 'g'.toByte(), 't'.toByte(), 't'.toByte(), 't'.toByte(), 't'.toByte(), 't'.toByte(), 'u'.toByte(), 'j'.toByte(), 'k'.toByte(), 'r'.toByte(), 'r'.toByte(), 'r'.toByte(), 'r'.toByte())
        val sKeySpec = SecretKeySpec(raw, "AES")
        //String initialVector = "abcdefgh";
        var encodeResult = ""
        val ivs = IvParameterSpec(initialVector.toByteArray())
        try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, ivs)
            val encryptedData = cipher.doFinal(text.toByteArray())
            encodeResult = Base64.encodeToString(encryptedData, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return encodeResult
    }

    fun Encrypt(text: String, initialVector: String, raw: ByteArray): String? {
        val sKeySpec = SecretKeySpec(raw, "AES")
        //String initialVector = "abcdefgh";
        var encodeResult: String? = null
        val ivs = IvParameterSpec(initialVector.toByteArray())
        try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, ivs)
            val encryptedData = cipher.doFinal(text.toByteArray())
            encodeResult = Base64.encodeToString(encryptedData, 0)
            encodeResult!!.replace("\n", "")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return encodeResult
    }

    fun Decrypt(text: String, initialVector: String, raw: ByteArray): String {
        val sKeySpec = SecretKeySpec(raw, "AES")
        //String initialVector = "abcdefgh";
        var decodeResult = ""
        val ivs = IvParameterSpec(initialVector.toByteArray())
        try {
            val data = Base64.decode(text, Base64.DEFAULT)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, ivs)
            val decryptedData = cipher.doFinal(data)
            decodeResult = String(decryptedData)
            //decode_result = Base64.encodeToString(encryptedData, 0);
            decodeResult = decodeResult.replace("\n", "")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return decodeResult
    }

    fun Decrypt(text: String, initialVector: String): String {
        val raw = byteArrayOf('g'.toByte(), 'g'.toByte(), 'g'.toByte(), 'g'.toByte(), 't'.toByte(), 't'.toByte(), 't'.toByte(), 't'.toByte(), 't'.toByte(), 'u'.toByte(), 'j'.toByte(), 'k'.toByte(), 'r'.toByte(), 'r'.toByte(), 'r'.toByte(), 'r'.toByte())
        val sKeySpec = SecretKeySpec(raw, "AES")
        //String initialVector = "abcdefgh";
        var decodeResult = ""
        val ivs = IvParameterSpec(initialVector.toByteArray())
        try {
            val data = Base64.decode(text, Base64.DEFAULT)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, ivs)
            val decryptedData = cipher.doFinal(data)
            decodeResult = String(decryptedData)
            //decode_result = Base64.encodeToString(encryptedData, 0);
            decodeResult = decodeResult.replace("\n", "")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return decodeResult
    }

}
