package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductClick

object ProductAddEditImageTracking {
    const val SCREEN = "/addproductpage - edit photo"

    fun trackScreen() {
        ProductAddEditTracking.getTracker().sendScreenAuthenticated(SCREEN)
    }

    fun trackBack(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click back on gallery page")
    }

    fun trackContinue(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click continue on gallery page")
    }

    fun trackEditBack(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click back on edit photo page")
    }

}
