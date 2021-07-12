package com.tokopedia.searchbar.navigation_component.data.notification


import com.google.gson.annotations.SerializedName

data class BuyerOrderStatus(
    @SerializedName("arriveAtDestination")
    val arriveAtDestination: Int = 0,
    @SerializedName("confirmed")
    val confirmed: Int = 0,
    @SerializedName("paymentStatus")
    val paymentStatus: Int = 0,
    @SerializedName("processed")
    val processed: Int = 0,
    @SerializedName("shipped")
    val shipped: Int = 0
)