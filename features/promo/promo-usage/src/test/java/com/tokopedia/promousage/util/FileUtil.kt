package com.tokopedia.promousage.util

import java.io.IOException

class FileUtil {

    fun getJsonFromAsset(path: String): String {
        var json = ""
        try {
            val inputStream = this.javaClass.classLoader!!.getResourceAsStream(path)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return json
    }
}
