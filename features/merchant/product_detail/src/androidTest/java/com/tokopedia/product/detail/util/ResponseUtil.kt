package com.tokopedia.product.detail.util

import java.io.IOException

/**
 * Created by Yehezkiel on 29/07/21
 */
object ResponseUtil {

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