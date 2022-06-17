package com.tokopedia.logisticseller.reschedulepickup.data.param

import com.google.gson.annotations.SerializedName

data class SaveReschedulePickupParam(
    @SerializedName("input")
    val input: MpLogisticInsertReschedulePickupInputs = MpLogisticInsertReschedulePickupInputs(),
) {
    data class MpLogisticInsertReschedulePickupInputs(
        @SerializedName("order_ids")
        val orderIds: List<String> = listOf(),
        @SerializedName("date")
        val date: String = "",
        @SerializedName("time")
        val time: String = "",
        @SerializedName("reason")
        val reason: String = "",
    )
}