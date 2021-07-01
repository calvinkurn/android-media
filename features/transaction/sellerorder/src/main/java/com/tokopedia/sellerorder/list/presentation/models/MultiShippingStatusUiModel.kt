package com.tokopedia.sellerorder.list.presentation.models

import com.google.gson.annotations.SerializedName

data class MultiShippingStatusUiModel(
        val total_order: Long = 0,
        val processed: Long = 0,
        val success: Long = 0,
        val fail: Long = 0,
        val listFail: String = "",
        val listError: List<ErrorMultiShippingStatusUiModel> = emptyList()
) {
    data class ErrorMultiShippingStatusUiModel(
            @SerializedName("message")
            val message: String = "",
            @SerializedName("order_id")
            val orderId: Long = 0
    )
}