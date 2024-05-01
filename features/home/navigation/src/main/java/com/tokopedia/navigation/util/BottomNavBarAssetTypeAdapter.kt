package com.tokopedia.navigation.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.tokopedia.navigation_common.ui.BottomNavBarAsset

object BottomNavBarAssetTypeAdapter : TypeAdapter<BottomNavBarAsset>() {

    private const val KEY_TYPE = "type"
    private const val KEY_URL = "url"

    private const val VALUE_IMAGE = "image"
    private const val VALUE_LOTTIE = "lottie"

    override fun write(writer: JsonWriter, asset: BottomNavBarAsset?) {
        if (asset == null) return
        writer.beginObject()
        with(writer) {
            name(KEY_TYPE)
            value(asset.jsonStringValue)

            name(KEY_URL)
            value(asset.url)
        }
        writer.endObject()
    }

    override fun read(reader: JsonReader): BottomNavBarAsset? {
        reader.beginObject()

        var typeString: String? = null
        var url = ""
        while (reader.hasNext()) {
            val token = reader.peek()
            if (token.equals(JsonToken.NAME)) {
                val name = reader.nextName()

                if (name == KEY_TYPE) typeString = reader.nextString()
                if (name == KEY_URL) url = reader.nextString()
            }
        }
        reader.endObject()

        return when (typeString) {
            VALUE_IMAGE -> BottomNavBarAsset.Image(url)
            VALUE_LOTTIE -> BottomNavBarAsset.Lottie(url)
            else -> null
        }
    }

    private val BottomNavBarAsset.jsonStringValue
        get() = when (this) {
            is BottomNavBarAsset.Image -> VALUE_IMAGE
            is BottomNavBarAsset.Lottie -> VALUE_LOTTIE
        }
}
