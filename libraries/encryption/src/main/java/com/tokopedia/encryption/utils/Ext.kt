package com.tokopedia.encryption.utils

import android.util.Base64
import com.google.crypto.tink.Aead
import java.util.*

object EncryptionExt {

    @JvmStatic
    fun simplyEncrypt(aead: Aead, text: String): String = aead.simplyEncrypt(text)

    @JvmStatic
    fun simplyDecrypt(aead: Aead, text: String): String = aead.simplyDecrypt(text)

}

fun Aead.simplyEncrypt(text: String): String {
    return encrypt(text.toByteArray(), null).let {
        Base64.encodeToString(it, Base64.DEFAULT)
    }
}

fun Aead.simplyDecrypt(text: String): String {
    return String(decrypt(Base64.decode(text, Base64.DEFAULT), null))
}