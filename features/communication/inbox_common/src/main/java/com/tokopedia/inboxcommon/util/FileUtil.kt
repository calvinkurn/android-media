package com.tokopedia.inboxcommon.util

import com.tokopedia.common.network.util.CommonUtil
import java.io.File
import java.lang.reflect.Type

object FileUtil {
    private fun readFileContent(fileName: String): String {
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
        return CommonUtil.fromJson(stringFile, typeOfT)
    }
}