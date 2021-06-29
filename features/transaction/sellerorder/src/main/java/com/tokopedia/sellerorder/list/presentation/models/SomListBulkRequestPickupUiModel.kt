package com.tokopedia.sellerorder.list.presentation.models

data class SomListBulkRequestPickupUiModel (
        val `data`: Data = Data(),
        val errors: List<ErrorBulkRequestPickup> = listOf()
) {
    data class Data(
            val jobId: String = "",
            val message: String = "",
            val totalOnProcess: Long = 0
    )

    data class ErrorBulkRequestPickup(
            val orderId: String = "",
            val message: String = ""
    )
}