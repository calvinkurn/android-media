package com.tokopedia.orderhistory

import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.common.network.util.CommonUtil
import java.lang.reflect.Type


object AndroidFileUtil {
    fun readFileContent(fileName: String): String {
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

    fun <T> parse(fileName: String, typeOfT: Type): T {
        val stringFile = readFileContent(fileName)
        return CommonUtil.fromJson(stringFile, typeOfT)
    }
}