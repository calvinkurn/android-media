package com.tokopedia.sellerorder.list.presentation.models

data class MultiShippingStatusUiModel(
        val total_order: Int = 0,
        val processed: Int = 0,
        val success: Int = 0,
        val fail: Int = 0,
        val listFail: String = "",
        val listError: List<ErrorMultiShippingStatusUiModel> = emptyList()
) {
    data class ErrorMultiShippingStatusUiModel(
            val message: String = "",
            val orderId: String = ""
    )
}