package com.tokopedia.onboarding.util

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.File
import java.io.StringReader
import java.lang.reflect.Type

object FileUtil {
    @Throws(JsonSyntaxException::class)
    fun <T> fromJson(json: String?, typeOfT: Type?): T?{
        if (json == null) {
            return null
        }
        val reader = StringReader(json)
        return Gson().fromJson<Any>(reader, typeOfT) as T
    }

    fun readFileContent(fileName: String): String {
        val fileContent = StringBuilder()
        javaClass.getResource(fileName)?.path?.let { filePath ->
            File(filePath).forEachLine {
                fileContent.append(it)
            }
        }
        return fileContent.toString()
    }

    fun <T> parse(fileName: String, typeOfT: Type): T {
        val stringFile = readFileContent(fileName)
        return fromJson(stringFile, typeOfT)!!
    }
}