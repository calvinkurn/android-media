package com.tokopedia.watch.orderlist.model

data class AcceptBulkOrderModel(
        val `data`: Data = Data(),
        val errors: List<Error> = listOf()
) {
    data class Data(
            val batchId: String = "",
            val message: String = "",
            val totalOrder: Int = 0
    )
    data class Error(
        val code: String = "",
        val status: String = "",
        val title: String = "",
        val detail: String = ""
    )
}