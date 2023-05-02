package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductClick

object ProductAddChooseImageTracking {
    const val SCREEN = "/addproductpage - choose photo"

    fun trackScreen() {
        ProductAddEditTracking.getTracker().sendScreenAuthenticated(SCREEN)
    }

    fun trackBack(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click back on gallery page")
    }
}
