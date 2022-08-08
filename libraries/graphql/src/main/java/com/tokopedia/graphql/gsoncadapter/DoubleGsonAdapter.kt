package com.tokopedia.graphql.gsoncadapter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.lang.NumberFormatException

class DoubleSafeTypeAdapter: TypeAdapter<Double?>() {
    override fun write(reader: JsonWriter?, doubleValue: Double?) {
        reader?.value(doubleValue ?: return)
    }

    override fun read(reader: JsonReader?): Double? {
        val numberStr = reader?.nextString() ?: return null
        return try {
            numberStr.toDouble()
        } catch (e: NumberFormatException) {
            0.0 // in case of invalid int, it will return zero
        }
    }
}