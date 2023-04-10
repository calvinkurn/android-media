package com.tokopedia.logisticseller.data.response

import com.google.gson.annotations.SerializedName

data class NewDriverAvailabilityResponse(
    @SerializedName("mpLogisticNewDriverAvailability")
    val data: NewDriverAvailabilityData? = null
) {
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
        @SerializedName("availability_retry")
        val availabilityRetry: Boolean = false
    )
}
