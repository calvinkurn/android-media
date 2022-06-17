package com.tokopedia.logisticseller.reschedulepickup.data.param

import com.google.gson.annotations.SerializedName

data class GetReschedulePickupParam(
    @SerializedName("order_ids")
    val orderIds: String = ""
)