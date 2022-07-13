package com.tokopedia.topchat

import com.tokopedia.common.network.util.CommonUtil
import java.io.File
import java.lang.reflect.Type

object FileUtil {
    fun readFileContent(fileName: String): String {
        val fileContent = StringBuilder()
        val filePath = javaClass.getResource(fileName)?.path
        File(filePath).forEachLine {
            fileContent.append(it)
        }
        return fileContent.toString()
    }

    fun <T> parse(fileName: String, typeOfT: Type): T {
        val stringFile = readFileContent(fileName)
        return CommonUtil.fromJson(stringFile, typeOfT)
    }

    fun <T> parseContent(content: String, typeOfT: Type): T {
        return CommonUtil.fromJson(content, typeOfT)
    }
}