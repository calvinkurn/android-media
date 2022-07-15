package com.tokopedia.topchat.chatsearch.data


import com.google.gson.annotations.SerializedName

data class Attributes(
    @SerializedName("domain")
    val domain: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("tag")
    val tag: String = "",
    @SerializedName("thumbnail")
    val thumbnail: String = ""
)