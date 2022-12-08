package com.tokopedia.logisticseller.data.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.model.GraphqlError

data class NewDriverBookingResponse(
    @SerializedName("MpLogisticNewDriverBooking")
    val mpLogisticNewDriverBooking: MpLogisticNewDriverBooking? = null,
) {
    data class MpLogisticNewDriverBooking(
        @SerializedName("data")
        val data: NewDriverBookingData? = null,
        @SerializedName("errors")
        val errors: List<GraphqlError>? = null,
    )

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

    val data: NewDriverBookingData?
        get() = mpLogisticNewDriverBooking?.data

    val errorMessage: String
        get() = mpLogisticNewDriverBooking?.errors?.firstOrNull()?.message.orEmpty()
}
