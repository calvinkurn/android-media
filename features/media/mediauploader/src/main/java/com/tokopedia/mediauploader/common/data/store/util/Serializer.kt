package com.tokopedia.mediauploader.common.data.store.util

import com.google.gson.Gson

object Serializer {

    inline fun <reified T> write(data: T): String {
        return Gson().toJson(data)
    }

    inline fun <reified T> read(data: String): T {
        return Gson().fromJson(data, T::class.java)
    }
}
