package com.tokopedia.oneclickcheckout.order.data.gocicil

class GoCicilInstallmentRequest(
        val gatewayCode: String,
        val merchantCode: String,
        val profileCode: String,
        val userId: String,
        val paymentAmount: Double,
        val signature: String,
        val merchantType: String,
)