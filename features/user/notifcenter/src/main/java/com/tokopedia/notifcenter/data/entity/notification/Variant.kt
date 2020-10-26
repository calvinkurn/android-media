package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class Variant(
    @SerializedName("hex")
    val hex: String = "",
    @SerializedName("identifier")
    val identifier: String = "",
    @SerializedName("value")
    val value: String = ""
)