package com.tokopedia.logisticseller.data.response

import com.google.gson.annotations.SerializedName

data class NewDriverBookingResponse(
    @SerializedName("MpLogisticNewDriverBooking")
    val data: NewDriverBookingData? = null,
) {
    data class NewDriverBookingData(
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
        @SerializedName("shipper_id")
        val shipperId: String = "",
        @SerializedName("shipper_product_id")
        val shipperProductId: String = "",
    )
}
