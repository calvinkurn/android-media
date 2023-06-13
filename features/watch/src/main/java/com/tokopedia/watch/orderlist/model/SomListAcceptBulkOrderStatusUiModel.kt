package com.tokopedia.watch.orderlist.model

data class SomListAcceptBulkOrderStatusUiModel(
        val `data`: Data = Data(),
        val errors: List<Error> = listOf()
) {
    data class Data(
            val multiOriginInvalidOrder: List<MultiOriginInvalidOrder> = listOf(),
            val success: Int = 0,
            val fail: Int = 0,
            val totalOrder: Int = 0,
            var shouldRecheck: Boolean = false
    ) {
        data class MultiOriginInvalidOrder(
                val orderId: Long = 0,
                val invoiceRefNum: String = ""
        )
    }
    data class Error(
        val code: String = "",
        val status: String = "",
        val title: String = "",
        val detail: String = ""
    )
}