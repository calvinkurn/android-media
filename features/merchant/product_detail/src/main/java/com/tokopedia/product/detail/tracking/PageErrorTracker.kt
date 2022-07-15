package com.tokopedia.product.detail.tracking

import com.tokopedia.kotlin.extensions.view.toZeroStringIfNull

class PageErrorTracker(
    productId: String?,
    isFromDeeplink: Boolean,
    deeplinkUrl: String,
    shopDomain: String,
    productKey: String
) {

    val deeplink = if (isFromDeeplink)
        "$shopDomain/$productKey"
    else deeplinkUrl

    val finalProductId = productId.toZeroStringIfNull()
}