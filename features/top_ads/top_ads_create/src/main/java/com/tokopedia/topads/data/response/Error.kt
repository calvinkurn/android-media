package com.tokopedia.topads.data.response

import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("detail")
    val detail: String = "",
    @SerializedName("object")
    val objectX: Object = Object(),
    @SerializedName("title")
    val title: String = ""
) {
    data class Object(
        @SerializedName("text")
        val text: Any? = Any(),
        @SerializedName("type")
        val type: Int = 0
    )
}