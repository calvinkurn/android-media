package com.tokopedia.buyerorder.unifiedhistory.list.data.model


import com.google.gson.annotations.SerializedName

data class UohFinishOrderParam(
    @SerializedName("order_id")
    var orderId: String = "",

    @SerializedName("user_id")
    var userId: String = "",

    @SerializedName("reason")
    var reason: String = "",

    @SerializedName("action_by")
    var actionBy: String = "buyer",

    @SerializedName("action")
    var action: String = "",

    @SerializedName("admin")
    var admin: String = "",

    @SerializedName("lang")
    var lang: String = "id",

    @SerializedName("os_type")
    var osType: String = ""
)