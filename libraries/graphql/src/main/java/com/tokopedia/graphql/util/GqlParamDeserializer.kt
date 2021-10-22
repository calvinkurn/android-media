package com.tokopedia.graphql.util

import com.google.gson.*
import com.google.gson.internal.LinkedTreeMap
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.lang.reflect.Type
import kotlin.math.ceil


class GqlParamDeserializer : JsonDeserializer<Map<String, Any>> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Map<String, Any> {
        json?.let {
            return read(it) as Map<String, Any>
        } ?: throw NullPointerException()
    }

    private fun read(json: JsonElement): Any {
        if (json.isJsonArray) {
            val list: MutableList<Any> = ArrayList()
            val arr: JsonArray = json.asJsonArray
            for (anArr in arr) {
                list.add(read(anArr))
            }
            return list
        } else if (json.isJsonObject) {
            val map: MutableMap<String, Any> = mutableMapOf()
            val obj: JsonObject = json.asJsonObject
            val entitySet = obj.entrySet()
            for ((key, value) in entitySet) {
                map[key] = read(value)
            }
            return map
        } else if (json.isJsonPrimitive) {
            val prim: JsonPrimitive = json.asJsonPrimitive
            when {
                prim.isBoolean -> {
                    return prim.asBoolean
                }
                prim.isString -> {
                    return prim.asString
                }
                prim.isNumber -> {
                    val num = prim.asNumber
                    return if (ceil(num.toDouble()) == num.toLong().toDouble()) {
                        num.toLong()
                    } else {
                        num.toDouble()
                    }
                }
            }
        }

        return json
    }
}