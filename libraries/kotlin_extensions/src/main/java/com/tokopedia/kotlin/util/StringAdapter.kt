package com.tokopedia.kotlin.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class StringAdapter : TypeAdapter<String>() {

    override fun read(reader: JsonReader): String {
        if (reader.peek() === JsonToken.NULL) {
            reader.nextNull()
            return ""
        }
        return reader.nextString()
    }

    override fun write(writer: JsonWriter, value: String?) {
        if (value == null) {
            writer.value("")
            return
        }
        writer.value(value)
    }
}