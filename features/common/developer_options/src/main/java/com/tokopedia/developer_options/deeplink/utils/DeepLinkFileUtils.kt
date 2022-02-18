package com.tokopedia.developer_options.deeplink.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import javax.inject.Inject

class DeepLinkFileUtils @Inject constructor() {

    fun <T> getJsonArrayResources(path: String): List<T> {
        val j: String = getJsonFromResource(path)
        val gson = Gson()
        val t = object : TypeToken<ArrayList<T>>() {}.type
        return gson.fromJson(j, t)
    }

    private fun getJsonFromResource(path: String): String {
        var json = ""
        try {
            val inputStream = this.javaClass.classLoader.getResourceAsStream(path)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
        return json
    }
}