package com.tokopedia.encryption.security

import android.util.Base64
import com.tokopedia.encryption.utils.Constants
import com.tokopedia.encryption.utils.Constants.CBC_ALGORITHM
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESEncryptorCBC(private val spec16ByteString: String) : BaseEncryptor() {
    var ivParameterSpec: IvParameterSpec? = null

    override fun generateKey(key: String): SecretKey {
        return SecretKeySpec(key.toByteArray(Charsets.UTF_8), Constants.AES_METHOD)
    }

    override fun encrypt(message: String, secretKey: SecretKey,
                         encoder: ((ByteArray) -> (String))): String {
        val cipher = Cipher.getInstance(CBC_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, getIv())
        val byteArray = cipher.doFinal(message.toByteArray(Charsets.UTF_8))
        return encoder(byteArray)
    }

    override fun decrypt(message: String, secretKey: SecretKey,
                         decoder: ((String) -> (ByteArray))): String {
        val cipher = Cipher.getInstance(CBC_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, getIv())
        return String(cipher.doFinal(decoder(message)), Charsets.UTF_8)
    }

    override fun encrypt(message: String, secretKey: SecretKey): String {
        return encrypt(message, secretKey, ::encrypt)
    }

    override fun decrypt(message: String, secretKey: SecretKey): String {
        return decrypt(message, secretKey, ::decrypt)
    }

    fun encrypt(input: ByteArray):String {
        return Base64.encodeToString(input, 0).replace("\n", "")
    }

    fun decrypt(string: String):ByteArray {
        return Base64.decode(string, Base64.DEFAULT)
    }

    fun getIv(): IvParameterSpec {
        if (ivParameterSpec == null) {
            ivParameterSpec = IvParameterSpec(spec16ByteString.toByteArray())
        }
        return ivParameterSpec!!
    }
}
