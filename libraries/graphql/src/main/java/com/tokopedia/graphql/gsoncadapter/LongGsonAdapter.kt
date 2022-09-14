package com.tokopedia.graphql.gsoncadapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.lang.NumberFormatException

class LongSafeTypeAdapter: TypeAdapter<Long?>() {
    override fun write(reader: JsonWriter?, longValue: Long?) {
        reader?.value(longValue ?: return)
    }

    override fun read(reader: JsonReader?): Long? {
        val numberStr = reader?.nextString() ?: return null
        return try {
            numberStr.toLong()
        } catch (e: NumberFormatException) {
            0 // in case of invalid int, it will return zero
        }
    }
}