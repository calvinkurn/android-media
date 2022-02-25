package com.tokopedia.pdpsimulation.paylater.domain.model

data class PayLaterArgsDescriptor(
    val productId: String,
    val defaultTenure: Int,
    val productUrl:String
)