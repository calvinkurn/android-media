package com.tokopedia.tokofood.feature.ordertracking.domain.utils

import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class FileUtilsTemp @Inject constructor() {

    inline fun <reified T> getJsonResources(json: String): T {
        val gson = Gson()
        val type = object : TypeToken<T>() {}.type
        return gson.fromJson(json, T::class.java)
    }

    fun getJsonFromRaw(resources: Resources, resourcesId: Int): String {
        val rawResource: InputStream = resources.openRawResource(resourcesId)
        val content: String = GraphqlHelper.streamToString(rawResource)
        try {
            rawResource.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return content
    }
}