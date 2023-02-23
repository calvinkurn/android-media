package com.tokopedia.unifyorderhistory.data.model

import com.google.gson.annotations.SerializedName

data class PmsNotification(
    @SerializedName("notifications")
    val notifications: Notifications = Notifications()
)

data class Notifications(
    @SerializedName("buyerOrderStatus")
    val buyerOrderStatus: BuyerOrderStatus = BuyerOrderStatus()
)

data class BuyerOrderStatus(
    @SerializedName("paymentStatus")
    val paymentStatus: Int = 0
)
