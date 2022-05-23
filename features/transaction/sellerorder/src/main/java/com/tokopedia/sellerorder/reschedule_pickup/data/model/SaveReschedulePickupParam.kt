package com.tokopedia.sellerorder.reschedule_pickup.data.model

import com.google.gson.annotations.SerializedName

data class SaveReschedulePickupParam(
    @SerializedName("order_ids")
    val orderIds: List<String> = listOf(),
    @SerializedName("date")
    val date: String = "",
    @SerializedName("time")
    val time: String = "",
    @SerializedName("reason")
    val reason: String = "",
)