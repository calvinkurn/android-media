package com.tokopedia.notifications.common

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException

import java.lang.reflect.Type

class StringEncryptTypeAdapter : JsonDeserializer<String> {

    @Throws(JsonParseException::class)
    override fun deserialize(jsonElement: JsonElement, type: Type,
                             jsonDeserializationContext: JsonDeserializationContext): String {
        return EncoderDecoder.Encrypt(jsonElement.asString, "tokopedia1234567")
    }

    companion object {

        private val INSTANCE = StringEncryptTypeAdapter()

        fun instance(): StringEncryptTypeAdapter {
            return INSTANCE
        }
    }
}