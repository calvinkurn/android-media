package com.tokopedia.notifcenter.stub.common

import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.common.network.util.CommonUtil
import java.lang.reflect.Type

object AndroidFileUtil {

    fun <T> parse(fileName: String, typeOfT: Type): T {
        val stringFile = readFileContent(fileName)
        val result = CommonUtil.fromJson<T>(stringFile, typeOfT)
        return result
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
