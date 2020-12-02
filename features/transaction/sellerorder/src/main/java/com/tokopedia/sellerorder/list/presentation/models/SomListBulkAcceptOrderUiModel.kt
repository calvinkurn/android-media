package com.tokopedia.sellerorder.list.presentation.models

data class SomListBulkAcceptOrderUiModel(
        val `data`: Data = Data(),
        val errors: List<Error> = listOf()
) {
    data class Data(
            val batchId: String = "",
            val message: String = "",
            val totalOrder: Int = 0
    )
}