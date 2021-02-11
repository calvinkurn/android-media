package com.tokopedia.sellerorder.list.domain.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SomListGetBulkAcceptOrderStatusResponse(
        @SerializedName("data")
        @Expose
        val `data`: Data = Data()
) {
    data class Data(
            @SerializedName("get_multi_accept_order_status")
            @Expose
            val getMultiAcceptOrderStatus: GetMultiAcceptOrderStatus = GetMultiAcceptOrderStatus()
    ) {
        data class GetMultiAcceptOrderStatus(
                @SerializedName("data")
                @Expose
                val `data`: Data = Data(),
                @SerializedName("errors")
                @Expose
                val errors: List<Error> = listOf()
        ) {
            data class Data(
                    @SerializedName("multi_origin_invalid_order")
                    @Expose
                    val multiOriginInvalidOrder: List<MultiOriginInvalidOrder> = listOf(),
                    @SerializedName("fail")
                    @Expose
                    val fail: String = "",
                    @SerializedName("success")
                    @Expose
                    val success: String = "",
                    @SerializedName("total_order")
                    @Expose
                    val totalOrder: String = ""
            ) {
                data class MultiOriginInvalidOrder(
                        @SerializedName("order_id")
                        @Expose
                        val orderId: String = "",
                        @SerializedName("invoice_ref_num")
                        @Expose
                        val invoiceRefNum: String = ""
                )
            }
        }
    }
}