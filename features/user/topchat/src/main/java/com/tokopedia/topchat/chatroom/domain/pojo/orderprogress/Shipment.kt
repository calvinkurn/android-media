package com.tokopedia.topchat.chatroom.domain.pojo.orderprogress


import com.google.gson.annotations.SerializedName

data class Shipment(
    @SerializedName("awb")
    val awb: String = "",
    @SerializedName("id")
    val id: Long = 0L,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("productId")
    val productId: Long = 0L,
    @SerializedName("productName")
    val productName: String = ""
)