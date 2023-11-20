package com.tokopedia.universal_sharing.stub.common.util

import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.StringReader
import java.lang.reflect.Type

object AndroidFileUtil {

    fun <T> parse(fileName: String, typeOfT: Type): T {
        val stringFile = readFileContent(fileName)
        val result = fromJson<T>(stringFile, typeOfT)
        return result
    }

    @Throws(JsonSyntaxException::class)
    private fun <T> fromJson(json: String?, typeOfT: Type?): T {
        val reader = StringReader(json)
        return Gson().fromJson<Any>(reader, typeOfT) as T
    }

    private fun readFileContent(fileName: String): String {
        val fileIn = InstrumentationRegistry
            .getInstrumentation()
            .context
            .assets
            .open(fileName)
        val fileContent = fileIn
            .readBytes()
            .toString(Charsets.UTF_8)
        fileIn.close()
        return fileContent
    }
}
