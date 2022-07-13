package com.tokopedia.product.detail.tracking

class PageErrorTracker(
    productId: String?,
    isFromDeeplink: Boolean,
    deeplinkUrl: String,
    shopDomain: String,
    productKey: String
) {

    val deeplink = if(isFromDeeplink)
        "$shopDomain/$productKey"
    else deeplinkUrl

    val finalProductId = productId ?: "0"
}