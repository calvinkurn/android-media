package com.tokopedia.devicefingerprint.submitdevice.utils

object RandomHelper {

    fun randomNumber(length: Int): String {
        val source = ('0'..'9')
        return (1..length).map { source.random() }.joinToString("")
    }

    fun randomString(length: Int): String {
        val source = ('0'..'9') + ('a'..'z') + ('A'..'Z') + "!@#$%^&*()-_=+".toList()
        return (1..length).map { source.random() }.joinToString("")
    }

}
