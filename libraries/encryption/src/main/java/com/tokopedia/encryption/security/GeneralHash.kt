package com.tokopedia.encryption.security

import android.util.Base64
import java.security.MessageDigest

fun String.md5(): String {
    return hashString(this, "MD5")
}

fun String.sha256(): String {
    return hashString(this, "SHA-256")
}

fun String.toBase64():String {
    return toBase64(this, Base64.NO_WRAP)
}

fun String.decodeBase64(): String {
    return try {
        val data: ByteArray = Base64.decode(this, Base64.DEFAULT)
        String(data)
    }catch (e: Exception){
        e.printStackTrace()
        this
    }
}

private fun hashString(input: String, algorithm: String): String {
    return MessageDigest
            .getInstance(algorithm)
            .digest(input.toByteArray())
            .fold("", { str, it -> str + "%02x".format(it) })
}

fun toBase64(text: String, mode: Int): String {
    try {
        val data = text.toByteArray(charset("UTF-8"))
        return Base64.encodeToString(data, mode)
    } catch (e:Exception) {
        return ""
    }
}