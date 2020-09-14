package com.tokopedia.topchat.chatsetting.data


import com.google.gson.annotations.SerializedName

data class Buyer(
    @SerializedName("alias")
    val alias: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("label")
    val label: String = "",
    @SerializedName("link")
    val link: String = "",
    @SerializedName("typeLabel")
    val typeLabel: Int = 0
)