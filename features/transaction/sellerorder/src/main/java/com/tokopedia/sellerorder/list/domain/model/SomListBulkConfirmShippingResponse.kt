package com.tokopedia.sellerorder.list.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomListBulkConfirmShippingResponse(
        @Expose
        @SerializedName("data")
        val `data`: Data = Data()
) {
    data class Data(
            @Expose
            @SerializedName("mpLogisticBulkConfirmShipping")
            val mpLogisticBulkConfirmShipping: MpLogisticBulkConfirmShipping = MpLogisticBulkConfirmShipping()
    ) {
        data class MpLogisticBulkConfirmShipping(
                @Expose
                @SerializedName("errors")
                val errors: List<ErrorBulkConfirmShipping> = listOf(),
                @Expose
                @SerializedName("job_id")
                val jobId: String = "",
                @Expose
                @SerializedName("message")
                val message: String = "",
                @Expose
                @SerializedName("status")
                val status: Long = 0,
                @Expose
                @SerializedName("total_on_process")
                val totalOnProcess: Long = 0
        )

        data class ErrorBulkConfirmShipping(
                @Expose
                @SerializedName("message")
                val message: String = "",
                @Expose
                @SerializedName("order_id")
                val orderId: String = ""
        )
    }
}