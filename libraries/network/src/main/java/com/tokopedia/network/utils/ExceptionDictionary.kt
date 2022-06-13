package com.tokopedia.network.utils

import com.tokopedia.network.interceptor.akamai.AkamaiErrorException
import java.io.FileNotFoundException
import java.io.IOException
import javax.net.ssl.SSLHandshakeException

class ExceptionDictionary {
    companion object {

        fun getRandomString(length:Int): String{
            val allowedChars = ('A'..'Z') + ('1'..'9')
            return (1..length)
                    .map { allowedChars.random() }
                    .joinToString("")
        }

        fun getErrorCodeSimple(exception: Throwable): String {
            return when (exception) {
                is AkamaiErrorException -> { "AK" }
                is ArithmeticException -> { "AE" }
                is ArrayIndexOutOfBoundsException -> { "AI" }
                is ClassNotFoundException -> { "CN" }
                is FileNotFoundException -> { "FN" }
                is IOException -> { "IO" }
                is IllegalArgumentException -> { "IA" }
                is IllegalStateException -> { "IS" }
                is InstantiationException -> { "IT" }
                is InterruptedException -> { "IR" }
                is NoSuchFieldException -> { "NF" }
                is NoSuchMethodException -> { "NM" }
                is NullPointerException -> { "NP" }
                is NumberFormatException -> { "NU" }
                is RuntimeException -> { "RU" }
                is SecurityException -> { "SE" }
                is SSLHandshakeException -> { "SL" }
                is StringIndexOutOfBoundsException -> { "SI" }
                is UnsupportedOperationException -> { "UO" }
                else -> "OO"
            }
        }
    }
}