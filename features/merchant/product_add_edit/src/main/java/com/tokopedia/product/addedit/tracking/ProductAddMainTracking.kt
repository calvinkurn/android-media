package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductClick

object ProductAddMainTracking {
    const val SCREEN = "/addproductpage - main"

    fun trackScreen() {
        ProductAddEditTracking.getTracker().sendScreenAuthenticated(SCREEN)
    }

    fun trackDragPhoto(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click drag product image")
    }

    fun clickProductNameRecom(shopId: String, productName: String) {
        sendAddProductClick(SCREEN, shopId, "click product name recommendation", productName)
    }

    fun clickProductCategoryRecom(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click product category recommendation")
    }

    fun clickOtherCategory(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click choose other categories")
    }

    fun clickRemoveWholesale(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click remove wholesale price")
    }

    fun clickAddWholesale(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click add wholesale price")
    }

    fun clickPreorderDropDownMenu(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click preorder dropdown menu")
    }

    fun clickCancelPreOrderDuration(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click cancel preorder duration")
    }

    fun clickPreOrderDuration(shopId: String, isDay: Boolean) {
        sendAddProductClick(SCREEN, shopId, "click preorder duration", if (isDay) {
            "day"
        } else {
            "week"
        })
    }

    fun clickContinue(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click continue on main page")
    }
}
