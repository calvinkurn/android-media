package com.tokopedia.logisticseller.data.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetReschedulePickupParam(
    @SerializedName("input")
    val input: MpLogisticGetReschedulePickupInputs = MpLogisticGetReschedulePickupInputs()
) : GqlParam {
    data class MpLogisticGetReschedulePickupInputs(
        @SerializedName("order_ids")
        val orderIds: String = ""
    ) : GqlParam
}