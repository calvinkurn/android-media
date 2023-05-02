package com.tokopedia.editshipping.util

import com.google.gson.Gson
import com.tokopedia.logisticCommon.data.response.customproductlogistic.OngkirGetCPLQGLResponse
import java.io.IOException
import java.nio.charset.Charset

object CPLDataProvider {
    private val gson = Gson()

    fun provideCPLResponse(): OngkirGetCPLQGLResponse {
        return gson.fromJson(getJsonFromAsset("assets/ongkir_get_cpl_editor.json"), OngkirGetCPLQGLResponse::class.java)
    }

    private fun getJsonFromAsset(path: String?): String {
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
