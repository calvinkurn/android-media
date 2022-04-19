package com.tokopedia.loginregister

import com.tokopedia.common.network.util.CommonUtil
import java.io.File
import java.lang.reflect.Type

object FileUtil {
    private fun readFileContent(fileName: String): String {
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
}