package com.tokopedia.sellerorder.waitingpaymentorder.presentation.model

data class Paging(
        val currentPage: Int = 0,
        val batchPage: Int = 0,
        val nextPaymentDeadline: String = ""
)