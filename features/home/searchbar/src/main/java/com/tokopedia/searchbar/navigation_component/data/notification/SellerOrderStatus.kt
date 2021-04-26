package com.tokopedia.searchbar.navigation_component.data.notification


import com.google.gson.annotations.SerializedName

data class SellerOrderStatus(
    @SerializedName("arriveAtDestination")
    val arriveAtDestination: Int = 0,
    @SerializedName("newOrder")
    val newOrder: Int = 0,
    @SerializedName("readyToShip")
    val readyToShip: Int = 0,
    @SerializedName("shipped")
    val shipped: Int = 0
)