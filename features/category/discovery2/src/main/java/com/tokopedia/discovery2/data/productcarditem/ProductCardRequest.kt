package com.tokopedia.discovery2.data.productcarditem

import com.tokopedia.analytics.byteio.RefreshType

data class ProductCardRequest(
    val componentId: String,
    val pageEndpoint: String,
    val sessionId: String,
    val componentName: String?,
    val refreshType: RefreshType
)
