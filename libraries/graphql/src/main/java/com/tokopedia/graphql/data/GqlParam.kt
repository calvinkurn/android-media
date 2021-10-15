package com.tokopedia.graphql.data

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.tokopedia.graphql.util.GsonConverter

interface GqlParam {

    fun toMapParam(): Map<String, Any> {
        val builder = GsonBuilder()
        builder.registerTypeAdapter(
            object : TypeToken<Map<String, Any>>() {}.type,
            GsonConverter()
        )

        val gson = builder.create()
        val json = gson.toJson(this)

        return gson.fromJson(
            json,
            object : TypeToken<Map<String, Any>>() {}.type
        )
    }

}