package com.tokopedia.inbox.viewmodel.util

import java.io.File
import java.lang.reflect.Type

object FileUtil {

    private fun readFileContent(fileName: String): String {
        val fileContent = StringBuilder()
        val filePath = javaClass.getResource(fileName)?.path
        filePath?.let {
            File(it).forEachLine { content ->
                fileContent.append(content)
            }
        }
        return fileContent.toString()
    }

    fun <T> parse(fileName: String, typeOfT: Type): T? {
        val stringFile = readFileContent(fileName)
        return CommonUtil.fromJson(stringFile, typeOfT)
    }

}