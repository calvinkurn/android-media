package com.tokopedia.logisticseller.data.response

import com.google.gson.annotations.SerializedName

data class SaveReschedulePickupResponse(
    @SerializedName("data")
    val data: Data = Data()
) {
    data class Data(
        @SerializedName("mpLogisticInsertReschedulePickup")
        val mpLogisticInsertReschedulePickup: MpLogisticInsertReschedulePickup = MpLogisticInsertReschedulePickup()
    ) {
        data class MpLogisticInsertReschedulePickup(
            @SerializedName("message")
            val message: String = "",
            @SerializedName("status")
            val status: String = "",
            @SerializedName("errors")
            val errors: List<String> = emptyList()
        )
    }
}
