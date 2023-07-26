package com.tokopedia.promousage.domain.entity

data class BoAdditionalData(
    val code: String = "",
    val uniqueId: String = "",
    val cartStringGroup: String = "",
    val shippingId: Long = 0,
    val spId: Long = 0,
)
