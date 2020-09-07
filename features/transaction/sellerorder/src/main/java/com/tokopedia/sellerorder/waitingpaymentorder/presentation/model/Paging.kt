package com.tokopedia.sellerorder.waitingpaymentorder.presentation.model

/**
 * Created by yusuf.hendrawan on 2020-09-07.
 */

data class Paging(
        val currentPage: Int = 0,
        val batchPage: Int = 0,
        val nextPaymentDeadline: String = ""
)