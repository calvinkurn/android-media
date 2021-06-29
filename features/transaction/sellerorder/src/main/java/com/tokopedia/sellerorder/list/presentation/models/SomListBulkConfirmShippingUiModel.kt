package com.tokopedia.sellerorder.list.presentation.models

data class SomListBulkConfirmShippingUiModel (
        val `data`: Data = Data(),
        val errors: List<ErrorBulkConfirmShipping> = listOf()
) {
    data class Data(
            val jobId: String = "",
            val message: String = "",
            val totalOnProcess: Long = 0
    )

    data class ErrorBulkConfirmShipping(
            val orderId: String = "",
            val message: String = ""
    )
}