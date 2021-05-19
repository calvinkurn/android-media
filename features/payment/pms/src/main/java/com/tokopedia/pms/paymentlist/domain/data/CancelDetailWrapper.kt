package com.tokopedia.pms.paymentlist.domain.data

data class CancelDetailWrapper(
    val transactionId: String,
    val merchantCode: String,
    val productName: String?,
    val cancelDetailData: CancelDetail
)