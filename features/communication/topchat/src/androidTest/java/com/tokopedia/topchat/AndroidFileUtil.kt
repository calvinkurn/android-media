package com.tokopedia.topchat

import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.common.network.util.CommonUtil
import java.io.IOException
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

    fun getJsonFromResource(path: String): String {
        var json = ""
        try {
            val inputStream = this.javaClass.classLoader!!.getResourceAsStream(path)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return json
    }
}