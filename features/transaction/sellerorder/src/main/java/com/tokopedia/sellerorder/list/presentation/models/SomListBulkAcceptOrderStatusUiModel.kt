package com.tokopedia.sellerorder.list.presentation.models

data class SomListBulkAcceptOrderStatusUiModel(
        val `data`: Data = Data(),
        val errors: List<Error> = listOf()
) {
    data class Data(
            val multiOriginInvalidOrder: List<MultiOriginInvalidOrder> = listOf(),
            val success: Int = 0,
            val totalOrder: Int = 0
    ) {
        data class MultiOriginInvalidOrder(
                val orderId: Int = 0,
                val invoiceRefNum: String = ""
        )
    }
}