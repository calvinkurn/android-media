package com.tokopedia.troubleshooter.notification.util

import com.google.gson.Gson
import java.io.File
import java.io.StringReader
import java.lang.reflect.Type

object FileUtils {
    fun <T> parse(fileName: String, typeOfT: Type): T {
        val json = readFileContent(fileName)
        return fromJson(json, typeOfT)
    }

    fun <T> parseContent(content: String, typeOfT: Type): T? {
        return fromJson(content, typeOfT)
    }

    private fun readFileContent(fileName: String): String? {
        val content = StringBuilder()
        val filePath = javaClass.getResource(fileName)?.path

        if (filePath.isNullOrEmpty()) return null

        File(filePath).forEachLine {
            content.append(it)
        }
        return content.toString()
    }

    private fun <T> fromJson(content: String?, typeOfT: Type): T {
        val reader = StringReader(content)
        return Gson().fromJson(reader, typeOfT)
    }
}