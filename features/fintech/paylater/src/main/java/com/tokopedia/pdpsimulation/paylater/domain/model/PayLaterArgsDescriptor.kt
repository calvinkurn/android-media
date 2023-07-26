package com.tokopedia.pdpsimulation.paylater.domain.model

data class PayLaterArgsDescriptor(
    val productId: String,
    val defaultTenure: Int,
    val parentId: String,
    val categoryId: String,
)
