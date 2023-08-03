package com.tokopedia.kotlin.util

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

import java.io.IOException
import java.util.ArrayList

/**
 * Author errysuprayogi on 27,February,2019
 */
class ListAdapter : TypeAdapter<List<*>>() {
    @Throws(IOException::class)
    override fun write(writer: JsonWriter, value: List<*>?) {
        if (value == null) {
            writer.beginArray()
            writer.endArray()
            return
        }
        writer.jsonValue(Gson().toJson(value))
    }

    @Throws(IOException::class)
    override fun read(reader: JsonReader): List<*>? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return emptyList<List<*>>()
        }
        return Gson().fromJson<List<*>>(reader.nextString(), object : TypeToken<ArrayList<*>>() {}.type)
    }
}
