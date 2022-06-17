package com.tokopedia.logisticseller.reschedulepickup.data.param

import com.google.gson.annotations.SerializedName

data class GetReschedulePickupParam(
    @SerializedName("input")
    val input: MpLogisticGetReschedulePickupInputs = MpLogisticGetReschedulePickupInputs()
) {
    data class MpLogisticGetReschedulePickupInputs(
        @SerializedName("order_ids")
        val orderIds: String = ""
    )
}