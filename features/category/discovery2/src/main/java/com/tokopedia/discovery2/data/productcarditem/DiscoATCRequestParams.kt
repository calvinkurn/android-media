package com.tokopedia.discovery2.data.productcarditem

import com.tokopedia.discovery2.data.ComponentsItem

data class DiscoATCRequestParams(
    val parentPosition: Int,
    val position: Int,
    val productId: String,
    val quantity: Int,
    var shopId: String? = null,
    val isGeneralCartATC: Boolean,
    val requestingComponent: ComponentsItem,
    val appLogParam: String
)
