package com.tokopedia.oneclickcheckout.common.view.model.preference

data class PaymentModel(
        var image: String = "",
        var description: String = "",
        var gatewayCode: String = "",
        var url: String = "",
        var gatewayName: String = "",
        var metadata: String = "",
        var tickerMessage: String = ""
)
