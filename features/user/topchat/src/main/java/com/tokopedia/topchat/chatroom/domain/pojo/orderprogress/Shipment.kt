package com.tokopedia.topchat.chatroom.domain.pojo.orderprogress


import com.google.gson.annotations.SerializedName

data class Shipment(
    @SerializedName("awb")
    val awb: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("productId")
    val productId: Int = 0,
    @SerializedName("productName")
    val productName: String = ""
)