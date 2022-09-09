package com.tokopedia.logisticseller.data.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class SaveReschedulePickupParam(
    @SerializedName("input")
    val input: MpLogisticInsertReschedulePickupInputs = MpLogisticInsertReschedulePickupInputs(),
) : GqlParam {
    data class MpLogisticInsertReschedulePickupInputs(
        @SerializedName("order_ids")
        val orderIds: List<String> = listOf(),
        @SerializedName("date")
        val date: String = "",
        @SerializedName("time")
        val time: String = "",
        @SerializedName("reason")
        val reason: String = "",
    ) : GqlParam
}