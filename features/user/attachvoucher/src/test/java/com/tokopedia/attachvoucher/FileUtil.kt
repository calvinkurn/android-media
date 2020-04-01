package com.tokopedia.attachvoucher

import java.io.File


object FileUtil {
    fun <T> readFileContent(input: Class<T>, fileName: String): String {
        val fileContent = StringBuilder()
        val filePath = javaClass.getResource(fileName)?.path
        File(filePath).forEachLine {
            fileContent.append(it)
        }
        return fileContent.toString()
    }
}