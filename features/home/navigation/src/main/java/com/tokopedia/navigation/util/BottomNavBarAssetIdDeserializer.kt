package com.tokopedia.navigation.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.tokopedia.navigation_common.ui.BottomNavBarAsset
import java.lang.reflect.Type

object BottomNavBarAssetIdDeserializer : JsonDeserializer<BottomNavBarAsset.Id> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): BottomNavBarAsset.Id {
        if (json == null || !json.isJsonPrimitive) return BottomNavBarAsset.Id("")
        return BottomNavBarAsset.Id(json.asString)
    }
}
