package com.tokopedia.content.common.model

import com.google.gson.annotations.SerializedName

data class UserProfile(
    @SerializedName("isShown")
    val isShown: Boolean = false,
    @SerializedName("image")
    val image: String = "",
    @SerializedName("weblink")
    val weblink: String = "",
    @SerializedName("applink")
    val applink: String = "",
    @SerializedName("__typename")
    val typeName: String = ""
)
