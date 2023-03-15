package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductClick

object ProductAddShippingTracking {
    const val SCREEN = "/addproductpage - shipping"
    const val EVENT_ADD_PRODUCT_IRIS = "viewAddProductIris"
    const val CAT_SHOP_PAGE_SELLER = "shop page - seller"

    fun trackScreen() {
        ProductAddEditTracking.getTracker().sendScreenAuthenticated(SCREEN)
    }

    fun clickBack(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click back on shipping detail page")
    }
}
