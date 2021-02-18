package com.tokopedia.sessioncommon.extensions

import android.util.Base64
import java.security.MessageDigest

/**
 * Created by Yoris Prayogo on 16/02/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */

fun ByteArray.toHexString(): String {
    if(isNotEmpty()) {
        var hexString = ""
        for (byte in this) {
            hexString += String.format("%02X", byte)
        }
        return hexString
    }
    return ""
}

fun String.getSHA256Hash(): ByteArray {
    return try {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(toByteArray(Charsets.UTF_8))
        hash
    } catch (e: java.lang.Exception){
        e.printStackTrace()
        byteArrayOf()
    }
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