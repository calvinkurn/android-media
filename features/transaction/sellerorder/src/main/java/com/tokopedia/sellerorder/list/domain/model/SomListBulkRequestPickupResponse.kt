package com.tokopedia.sellerorder.list.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomListBulkRequestPickupResponse(
        @Expose
        @SerializedName("data")
        val `data`: Data = Data()
) {
    data class Data(
            @Expose
            @SerializedName("mpLogisticBulkRequestPickup")
            val mpLogisticBulkRequestPickup: MpLogisticBulkRequestPickup = MpLogisticBulkRequestPickup()
    ) {
        data class MpLogisticBulkRequestPickup(
                @Expose
                @SerializedName("errors")
                val errors: List<Error> = listOf(),
                @Expose
                @SerializedName("job_id")
                val jobId: String = "",
                @Expose
                @SerializedName("message")
                val message: String = "",
                @Expose
                @SerializedName("status")
                val status: Int = 0,
                @Expose
                @SerializedName("total_on_process")
                val totalOnProcess: Long = 0
        ) {
            data class Error(
                    @Expose
                    @SerializedName("message")
                    val message: String = "",
                    @Expose
                    @SerializedName("order_id")
                    val orderId: String = ""
            )
        }
    }
}