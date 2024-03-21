package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class Timer(
    @SerializedName("expired_time")
    val expiredTime: String = "",
    @SerializedName("server_time")
    val serverTime: String = ""
)
