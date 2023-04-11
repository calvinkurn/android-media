package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendEditProductClick

object ProductEditShippingTracking {

    fun clickBack(shopId: String) {
        sendEditProductClick(shopId, "click back on shipping detail page")
    }

    fun clickInsurance(shopId: String) {
        sendEditProductClick(shopId, "click shipping insurance button")
    }
}
