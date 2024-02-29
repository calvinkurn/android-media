package com.tokopedia.discovery2.data.productcarditem

data class ProductCardRequest(
    val componentId: String,
    val pageEndpoint: String,
    val sessionId: String,
    val componentName: String?
)
