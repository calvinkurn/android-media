package com.tokopedia.graphql.gsoncadapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.lang.NumberFormatException

class FloatSafeTypeAdapter: TypeAdapter<Float?>() {
    override fun write(reader: JsonWriter?, floatValue: Float?) {
        reader?.value(floatValue ?: return)
    }

    override fun read(reader: JsonReader?): Float? {
        val numberStr = reader?.nextString() ?: return null
        return try {
            numberStr.toFloat()
        } catch (e: NumberFormatException) {
            0f // in case of invalid int, it will return zero
        }
    }
}