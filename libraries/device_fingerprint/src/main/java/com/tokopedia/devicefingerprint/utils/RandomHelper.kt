package com.tokopedia.devicefingerprint.utils

import kotlin.random.Random

object RandomHelper {

    fun randomNumber(length: Int): String {
        val source = ('0'..'9')
        return (1..length).map { source.random() }.joinToString("")
    }

    fun randomString(numOfBytes: Int): String {
        var byteArray = ByteArray(numOfBytes)
        byteArray = Random.nextBytes(byteArray)
        return String(byteArray)
    }

    fun randomByteArray(length: Int): ByteArray {
        var byteArray = ByteArray(length)
        byteArray = Random.nextBytes(byteArray)
        return  byteArray
    }

}
