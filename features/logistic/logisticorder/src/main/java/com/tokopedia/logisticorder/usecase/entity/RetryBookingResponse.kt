package com.tokopedia.logisticorder.usecase.entity

import com.google.gson.annotations.SerializedName


data class RetryBookingResponse(
        @SerializedName("retryBooking")
        var retryBooking: RetryBooking = RetryBooking()
)

data class RetryBooking(
        @SerializedName("awbnum")
        var awbnum: String = "",
        @SerializedName("order_id")
        var orderId: String = "",
        @SerializedName("order_tx_id")
        var orderTxId: String = "",
        @SerializedName("shipper_id")
        var shipperId: String = "",
        @SerializedName("shipper_product_id")
        var shipperProductId: String = ""
)