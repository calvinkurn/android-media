package com.tokopedia.sellerorder.reschedulepickup.data.model

import com.google.gson.annotations.SerializedName

data class GetReschedulePickupParam(
    @SerializedName("order_ids")
    val orderIds: String = ""
)