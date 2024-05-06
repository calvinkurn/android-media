package com.tokopedia.navigation.util

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.tokopedia.navigation_common.ui.BottomNavBarAsset

object BottomNavBarAssetTypeTypeAdapter : TypeAdapter<BottomNavBarAsset.Type>() {

    private const val KEY_TYPE = "type"
    private const val KEY_URL = "url"
    private const val KEY_RES = "res"

    private const val VALUE_IMAGE = "image"
    private const val VALUE_IMAGE_RES = "image_res"
    private const val VALUE_LOTTIE = "lottie"
    private const val VALUE_LOTTIE_RES = "lottie_res"

    override fun write(writer: JsonWriter, asset: BottomNavBarAsset.Type?) {
        if (asset == null) return
        writer.beginObject()
        with(writer) {
            name(KEY_TYPE)
            value(asset.jsonStringValue)

            when (asset) {
                is BottomNavBarAsset.Type.ImageUrl -> {
                    name(KEY_URL)
                    value(asset.url)
                }
                is BottomNavBarAsset.Type.ImageRes -> {
                    name(KEY_RES)
                    value(asset.res)
                }
                is BottomNavBarAsset.Type.LottieUrl -> {
                    name(KEY_URL)
                    value(asset.url)
                }
                is BottomNavBarAsset.Type.LottieRes -> {
                    name(KEY_RES)
                    value(asset.res)
                }
            }
        }
        writer.endObject()
    }

    override fun read(reader: JsonReader): BottomNavBarAsset.Type? {
        reader.beginObject()

        var typeString: String? = null
        var url = ""
        var res = -1
        while (reader.hasNext()) {
            val token = reader.peek()
            if (token.equals(JsonToken.NAME)) {
                val name = reader.nextName()

                if (name == KEY_TYPE) typeString = reader.nextString()
                if (name == KEY_URL) url = reader.nextString()
                if (name == KEY_RES) res = reader.nextInt()
            }
        }
        reader.endObject()

        return when (typeString) {
            VALUE_IMAGE -> BottomNavBarAsset.Type.ImageUrl(url)
            VALUE_IMAGE_RES -> BottomNavBarAsset.Type.ImageRes(res)
            VALUE_LOTTIE -> BottomNavBarAsset.Type.LottieUrl(url)
            VALUE_LOTTIE_RES -> BottomNavBarAsset.Type.LottieRes(res)
            else -> null
        }
    }

    private val BottomNavBarAsset.Type.jsonStringValue
        get() = when (this) {
            is BottomNavBarAsset.Type.ImageUrl -> VALUE_IMAGE
            is BottomNavBarAsset.Type.ImageRes -> VALUE_IMAGE_RES
            is BottomNavBarAsset.Type.LottieUrl -> VALUE_LOTTIE
            is BottomNavBarAsset.Type.LottieRes -> VALUE_LOTTIE_RES
        }
}
