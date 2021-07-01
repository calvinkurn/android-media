package com.tokopedia.sellerorder.list.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomListGetMultiShippingResponse(
        @Expose
        @SerializedName("data")
        val `data`: Data = Data()
) {
    data class Data(
            @Expose
            @SerializedName("mpLogisticMultiShippingStatus")
            val mpLogisticMultiShippingStatus: MpLogisticMultiShippingStatus = MpLogisticMultiShippingStatus()
    ) {
        data class MpLogisticMultiShippingStatus(
                @Expose
                @SerializedName("fail")
                val fail: String = "",
                @Expose
                @SerializedName("list_error")
                val listError: List<ErrorMultiShippingStatus> = listOf(),
                @Expose
                @SerializedName("list_fail")
                val listFail: String = "",
                @Expose
                @SerializedName("processed")
                val processed: String = "",
                @Expose
                @SerializedName("success")
                val success: String = "",
                @Expose
                @SerializedName("total_order")
                val totalOrder: String = ""
        )

        data class ErrorMultiShippingStatus(
                @SerializedName("message")
                val message: String = "",
                @SerializedName("order_id")
                val orderId: Long = 0
        )
    }
}

