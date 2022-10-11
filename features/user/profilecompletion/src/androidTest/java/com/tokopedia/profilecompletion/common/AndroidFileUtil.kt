package com.tokopedia.home_account.common

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.tokopedia.test.application.util.InstrumentationMockHelper
import java.io.IOException
import java.io.InputStream
import java.io.StringReader
import java.lang.reflect.Type

object AndroidFileUtil {

    private fun readFileContent(resId: Int): String {
        val context = InstrumentationRegistry
            .getInstrumentation()
            .context
        return InstrumentationMockHelper.getRawString(
            context, resId
        )
    }

    fun <T> parse(resId: Int, typeOfT: Type): T {
        val stringFile = readFileContent(resId)
        return fromJson(stringFile, typeOfT)
    }

    @Throws(JsonSyntaxException::class)
    private fun <T> fromJson(json: String, typeOfT: Type): T {
        val reader = StringReader(json)
        return Gson().fromJson<Any>(reader, typeOfT) as T
    }
}