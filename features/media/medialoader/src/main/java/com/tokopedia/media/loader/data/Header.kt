package com.tokopedia.media.loader.data

import com.google.gson.Gson

data class Header(
    val key: String,
    val values: List<String>
) {
    companion object {
        fun Map<String, List<String>>.toModel(): List<Header> {
            return map {
                val (key, value) = it

                Header(
                    key = key,
                    values = value
                )
            }
        }

        fun List<Header>.toJson(): String {
            return Gson().toJson(this)
        }
    }
}
