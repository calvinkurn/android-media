package com.tokopedia.sellerorder.reschedule_pickup.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetReschedulePickupParam(
    @SerializedName("order_ids")
    @Expose
    val orderIds: String = ""
)