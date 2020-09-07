package com.tokopedia.devicefingerprint.utils

import kotlin.random.Random

object RandomHelper {

    fun randomNumber(length: Int): String {
        val source = ('0'..'9')
        return (1..length).map { source.random() }.joinToString("")
    }

    fun randomString(lengthInBytes: Int): String {
        var byteArray = ByteArray(lengthInBytes)
        byteArray = Random.nextBytes(byteArray)
        return String(byteArray)
    }

}
