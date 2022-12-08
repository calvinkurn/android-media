package com.tokopedia.logisticseller.data.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.model.GraphqlError

data class NewDriverAvailabilityResponse(
    @SerializedName("mpLogisticNewDriverAvailability")
    val mLogisticNewDriverAvailability: LogisticNewDriverAvailability? = null
) {
    data class LogisticNewDriverAvailability(
        @SerializedName("data")
        val data: NewDriverAvailabilityData? = null,
        @SerializedName("errors")
        val errors: List<GraphqlError>? = null,
    )

    data class NewDriverAvailabilityData(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("order_id")
        val orderId: String = "",
        @SerializedName("order_tx_id")
        val orderTxId: String = "",
        @SerializedName("invoice")
        val invoice: String = "",
        @SerializedName("awbnum")
        val awbnum: String = "",
        @SerializedName("available_time")
        val availableTime: String = "",
    )

    val data: NewDriverAvailabilityData?
        get() = mLogisticNewDriverAvailability?.data

    val errorMessage: String
        get() = mLogisticNewDriverAvailability?.errors?.firstOrNull()?.message.orEmpty()
}
