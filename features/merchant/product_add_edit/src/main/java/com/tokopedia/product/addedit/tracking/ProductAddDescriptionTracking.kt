package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductClick

object ProductAddDescriptionTracking {
    const val SCREEN = "/addproductpage - description"

    fun trackScreen() {
        ProductAddEditTracking.getTracker().sendScreenAuthenticated(SCREEN)
    }

    fun clickRemoveVideoLink(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click remove video link")
    }

    fun clickAddVideoLink(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click add video link")
    }

    fun clickAddProductVariant(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click add product variant")
    }

    fun clickContinue(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click continue on product description page")
    }

}
