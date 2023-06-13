package com.tokopedia.usecase

import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.util.Base64_
import com.tokopedia.user.session.util.EncoderDecoder
import org.junit.Test
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncoderDecoderTest {

    //old encryption function
    private fun oldEncryption(text: String, initialVector: String): String? {
        val raw = byteArrayOf(
            'g'.code.toByte(),
            'g'.code.toByte(),
            'g'.code.toByte(),
            'g'.code.toByte(),
            't'.code.toByte(),
            't'.code.toByte(),
            't'.code.toByte(),
            't'.code.toByte(),
            't'.code.toByte(),
            'u'.code.toByte(),
            'j'.code.toByte(),
            'k'.code.toByte(),
            'r'.code.toByte(),
            'r'.code.toByte(),
            'r'.code.toByte(),
            'r'.code.toByte()
        )
        val skeySpec = SecretKeySpec(raw, "AES")
        var encode_result: String? = null
        val ivs = IvParameterSpec(initialVector.toByteArray())
        try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivs)
            val encryptedData = cipher.doFinal(text.toByteArray())
            encode_result = Base64_.encodeToString(encryptedData, 0)
            encode_result.replace("\n", "")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return encode_result
    }

    @Test
    fun `decrypt old encryption with new decryption`() {
        val rawKey = "abclalayeye"
        var oldEncryptKey = oldEncryption(rawKey, UserSession.KEY_IV)
        var decryptedKeyNewFunc = EncoderDecoder.Decrypt(oldEncryptKey, UserSession.KEY_IV)
        assert(rawKey == decryptedKeyNewFunc)
    }

    @Test
    fun `encrypt key with new encryption and decrypt with new decryption`() {
        val rawKey = "abclalayeye"
        var oldEncryptKey = EncoderDecoder.Encrypt(rawKey, UserSession.KEY_IV)
        var decryptedKeyNewFunc = EncoderDecoder.Decrypt(oldEncryptKey, UserSession.KEY_IV)
        assert(rawKey == decryptedKeyNewFunc)
    }
}
