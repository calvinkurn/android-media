package com.tokopedia.network.utils

import java.io.FileNotFoundException
import java.io.IOException

class ExceptionDictionary {
    companion object {

        fun getRandomString(length:Int): String{
            val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return (1..length)
                    .map { allowedChars.random() }
                    .joinToString("")
        }

        fun getErrorCodeSimple(exception: Throwable): String {
            return when (exception) {
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
                is StringIndexOutOfBoundsException -> { "SI" }
                is UnsupportedOperationException -> { "UO" }
                else -> "OO"
            }
        }

        fun getErrorCodeNumeric(exception: Throwable): String {
            return when (exception) {
                is ArithmeticException -> { "01" }
                is ArrayIndexOutOfBoundsException -> { "02" }
                is ClassNotFoundException -> { "03" }
                is FileNotFoundException -> { "04" }
                is IOException -> { "05" }
                is IllegalArgumentException -> { "06" }
                is IllegalStateException -> { "07" }
                is InstantiationException -> { "08" }
                is InterruptedException -> { "09" }
                is NoSuchFieldException -> { "10" }
                is NoSuchMethodException -> { "11" }
                is NullPointerException -> { "12" }
                is NumberFormatException -> { "13" }
                is RuntimeException -> { "14" }
                is SecurityException -> { "15" }
                is StringIndexOutOfBoundsException -> { "16" }
                is UnsupportedOperationException -> { "17" }
                else -> "00"
            }
        }
    }
}