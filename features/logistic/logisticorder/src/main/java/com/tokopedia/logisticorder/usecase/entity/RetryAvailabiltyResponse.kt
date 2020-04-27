package com.tokopedia.logisticorder.usecase.entity

import com.google.gson.annotations.SerializedName

data class RetryAvailabilityResponse(
        @SerializedName("retryAvailability")
        var retryAvailability: RetryAvailability = RetryAvailability()
)

data class RetryAvailability(
        @SerializedName("availability_retry")
        var availabilityRetry: Boolean = false,
        @SerializedName("awbnum")
        var awbnum: String = "",
        @SerializedName("deadline_retry")
        var deadlineRetry: String = "",
        @SerializedName("deadline_retry_unixtime")
        var deadlineRetryUnixtime: String = "",
        @SerializedName("order_id")
        var orderId: String = "",
        @SerializedName("order_tx_id")
        var orderTxId: String = "",
        @SerializedName("show_retry_button")
        var showRetryButton: Boolean = false
)