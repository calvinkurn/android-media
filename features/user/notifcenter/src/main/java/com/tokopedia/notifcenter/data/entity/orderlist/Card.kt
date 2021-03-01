package com.tokopedia.notifcenter.data.entity.orderlist


import com.google.gson.annotations.SerializedName

data class Card(
    @SerializedName("counter")
    val counter: Int = 0,
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("link")
    val link: Link = Link(),
    @SerializedName("text")
    val text: String = ""
)