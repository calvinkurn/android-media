package com.tokopedia.topchat.chatroom.domain.pojo.headerctamsg


import com.google.gson.annotations.SerializedName

data class Extras(
    @SerializedName("extras_product")
    var extrasProduct: List<ExtrasProduct> = listOf(),
    @SerializedName("intent")
    var intent: String = "",
    @SerializedName("location_stock")
    var locationStock: LocationStock = LocationStock()
)