package com.tokopedia.pms.paymentlist.domain.data

import com.tokopedia.pms.payment.data.model.CancelDetail

data class CancelDetailWrapper(
    val transactionId: String,
    val merchantCode: String,
    val productName: String?,
    val cancelDetailData: CancelDetail
)