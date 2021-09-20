package com.tokopedia.pms.paymentlist.domain.data

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("notifications")
    val notificationData: NotificationCountData
)

data class NotificationCountData(
    @SerializedName("buyerOrderStatus")
    val statusData: BuyerOrderStatusData
)

data class BuyerOrderStatusData(
    @SerializedName("paymentStatus")
    val paymentStatusCount: Int
)
