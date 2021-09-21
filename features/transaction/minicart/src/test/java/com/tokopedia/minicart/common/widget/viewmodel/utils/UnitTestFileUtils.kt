package com.tokopedia.minicart.common.widget.viewmodel.utils

import java.io.IOException
import java.nio.charset.Charset

object UnitTestFileUtils {
    fun getJsonFromAsset(path: String?): String {
        var json = ""
        javaClass.classLoader?.getResourceAsStream(path)?.let {
            try {
                val size = it.available()
                val buffer = ByteArray(size)
                it.read(buffer)
                it.close()
                json = String(buffer, Charset.forName("UTF-8"))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return json
    }
}