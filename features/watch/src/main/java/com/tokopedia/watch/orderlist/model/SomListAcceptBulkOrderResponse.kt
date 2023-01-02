package com.tokopedia.watch.orderlist.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomListAcceptBulkOrderResponse(
        @SerializedName("data")
        @Expose
        val `data`: Data = Data()
) {
    data class Data(
            @SerializedName("multi_accept_order")
            @Expose
            val multiAcceptOrder: MultiAcceptOrder = MultiAcceptOrder()
    ) {
        data class MultiAcceptOrder(
                @SerializedName("data")
                @Expose
                val `data`: Data = Data(),
                @SerializedName("errors")
                @Expose
                val errors: List<Error> = listOf()
        ) {
            data class Data(
                    @SerializedName("batch_id")
                    @Expose
                    val batchId: String = "",
                    @SerializedName("message")
                    @Expose
                    val message: String = "",
                    @SerializedName("total_order")
                    @Expose
                    val totalOrder: Int = 0
            )
            data class Error(
                @SerializedName("code")
                @Expose
                val code: String = "",
                @SerializedName("status")
                @Expose
                val status: String = "",
                @SerializedName("title")
                @Expose
                val title: String = "",
                @SerializedName("detail")
                @Expose
                val detail: String = ""
            )
        }
    }
}