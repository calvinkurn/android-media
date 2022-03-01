package com.tokopedia.pdpsimulation.activateCheckout.domain.model

import com.tokopedia.pdpsimulation.paylater.domain.model.InstallmentDetails

data class TenureSelectedModel(
        var priceText: String? = null,
        var tenure: String? = null,
        var installmentDetails: InstallmentDetails?
)
