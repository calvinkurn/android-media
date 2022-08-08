package com.tokopedia.graphql.gsoncadapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.lang.NumberFormatException

class IntSafeTypeAdapter: TypeAdapter<Int?>() {
    override fun write(reader: JsonWriter?, intValue: Int?) {
        reader?.value(intValue ?: return)
    }

    override fun read(reader: JsonReader?): Int? {
        val numberStr = reader?.nextString() ?: return null
        return try {
            numberStr.toInt()
        } catch (e: NumberFormatException) {
            0
        }
    }
}