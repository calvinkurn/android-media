package com.tokopedia.tokofood.stub.common.util

import com.tokopedia.tokochat_common.util.CommonUtil
import java.io.IOException
import java.lang.reflect.Type

object AndroidTestUtil {

    fun <T> parse(fileName: String, typeOfT: Type): T? {
        val stringFile = getJsonFromResource(fileName)
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
