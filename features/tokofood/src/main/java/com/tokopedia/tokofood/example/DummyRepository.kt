package com.tokopedia.tokofood.example

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.util.*
import java.util.regex.Pattern
import kotlin.math.max

class DummyRepository {
    companion object {
        val pattern = Pattern.compile("[^a-zA-Z0-9]")
        suspend fun processText(text: String): String {
            return withContext(Dispatchers.IO) {
                delay(2000)
                if (pattern.matcher(text).find()) {
                    throw RuntimeException("Ada symbol nih. ga bisa.")
                }
                val output = text.uppercase(Locale.US)
                Log.w("HENDRY", "Finish process $text")
                output
            }
        }
        suspend fun processDoubleInput(text1: String, text2: String): String {
            return withContext(Dispatchers.IO) {
                delay(1000)
                val strOutputBuilder = StringBuilder()
                val lenTxt1 = text1.length
                val lenTxt2 = text2.length
                val maxLen = max(lenTxt1, lenTxt2)
                for (i in 0 until maxLen) {
                    text1.getOrNull(i)?.let {
                        strOutputBuilder.append(it.uppercase())
                    }
                    text2.getOrNull(i)?.let {
                        strOutputBuilder.append(it.lowercase())
                    }
                }
                strOutputBuilder.toString()
            }
        }
    }
}