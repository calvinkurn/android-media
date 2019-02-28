package com.tokopedia.kotlin.util

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken

class NullToDefaultValueAdapterFactory : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val rawType = type.getRawType() as Class<T>
        return if (rawType == String::class.java) {
            StringAdapter() as TypeAdapter<T>
        } else if (rawType == List::class.java || rawType == ArrayList::class.java) {
            ListAdapter() as TypeAdapter<T>
        } else null
    }
}