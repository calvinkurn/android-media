package com.tokopedia.tokochat_common.util

import com.google.gson.Gson
import com.google.gson.JsonNull
import com.google.gson.JsonSyntaxException
import java.io.StringReader
import java.lang.reflect.Type

object CommonUtil {

    @Throws(JsonSyntaxException::class)
    fun <T> fromJson(json: String?, typeOfT: Type?): T? {
        if (json == null) {
            return null
        }
        val reader = StringReader(json)
        return Gson().fromJson<Any>(reader, typeOfT) as T
    }

    fun toJson(src: Any?): String {
        val gson = Gson()
        return if (src == null) {
            gson.toJson(JsonNull.INSTANCE)
        } else gson.toJson(src, src.javaClass)
    }
}
