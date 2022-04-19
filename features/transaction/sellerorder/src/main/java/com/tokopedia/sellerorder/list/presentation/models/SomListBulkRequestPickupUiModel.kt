package com.tokopedia.sellerorder.list.presentation.models

data class SomListBulkRequestPickupUiModel (
        val `data`: Data = Data(),
        val errors: List<ErrorBulkRequestPickup> = listOf(),
        val status: Int? = null
) {
    data class Data(
            val jobId: String = "",
            val message: String = "",
            val totalOnProcess: Int = 0
    )

    data class ErrorBulkRequestPickup(
            val orderId: String = "",
            val message: String = ""
    )
}