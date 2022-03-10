package com.tokopedia.sellerorder.reschedule_pickup.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SaveReschedulePickupParam(
    @SerializedName("order_ids")
    @Expose
    val orderIds: List<String> = listOf(),
    @SerializedName("date")
    @Expose
    val date: String = "",
    @SerializedName("time")
    @Expose
    val time: String = "",
    @SerializedName("reason")
    @Expose
    val reason: String = "",
)