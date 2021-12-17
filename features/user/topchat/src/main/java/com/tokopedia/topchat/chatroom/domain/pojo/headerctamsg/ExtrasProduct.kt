package com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg


import com.google.gson.annotations.SerializedName

data class ExtrasProduct(
    @SerializedName("product_id")
    var productId: String = "",
    @SerializedName("url")
    var url: String = "",
    @SerializedName("product_name")
    var product_name: String = ""
)