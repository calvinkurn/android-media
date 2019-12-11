package com.tokopedia.home.account.analytics.data.model


import com.google.gson.annotations.SerializedName

data class UserShopInfo(
    @SerializedName("info")
    val info: Info = Info(),
    @SerializedName("stats")
    val stats: Stats = Stats()
)