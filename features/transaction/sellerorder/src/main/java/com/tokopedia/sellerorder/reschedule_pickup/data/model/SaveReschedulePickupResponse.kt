package com.tokopedia.sellerorder.reschedule_pickup.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SaveReschedulePickupResponse(
    @Expose
    @SerializedName("data")
    val data: Data
) {
    data class Data(
        @Expose
        @SerializedName("mpLogisticInsertReschedulePickup")
        val mpLogisticInsertReschedulePickup: MpLogisticInsertReschedulePickup
    ) {
        data class MpLogisticInsertReschedulePickup(
            @Expose
            @SerializedName("message")
            val message: String,
            @Expose
            @SerializedName("status")
            val status: String
        )
    }
}
