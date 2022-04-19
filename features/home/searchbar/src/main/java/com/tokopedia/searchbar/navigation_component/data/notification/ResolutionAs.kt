package com.tokopedia.searchbar.navigation_component.data.notification


import com.google.gson.annotations.SerializedName

data class ResolutionAs(
    @SerializedName("buyer")
    val buyer: Int = 0,
    @SerializedName("seller")
    val seller: Int = 0
)