package com.tokopedia.developer_options.applink.utils

import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.GraphqlHelper.streamToString
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class DeepLinkFileUtils @Inject constructor() {

    inline fun <reified T> getJsonArrayResources(json: String): List<T> {
        val gson = Gson()
        val type = object : TypeToken<List<T>>() {}.type
        return gson.fromJson(json, type)
    }

    fun getJsonFromRaw(resources: Resources, resourcesId: Int): String {
        val rawResource: InputStream = resources.openRawResource(resourcesId)
        val content: String = streamToString(rawResource)
        try {
            rawResource.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return content
    }
}