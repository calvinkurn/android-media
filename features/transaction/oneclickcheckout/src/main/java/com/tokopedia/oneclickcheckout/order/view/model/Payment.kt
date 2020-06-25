package com.tokopedia.oneclickcheckout.order.view.model

data class Payment(
        val image: String? = null,
        val description: String? = null,
        val gatewayCode: String? = null,
        val url: String? = null,
        val gatewayName: String? = null,
        val errorMessage: String? = null
)