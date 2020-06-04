package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendEditProductClick

object ProductEditMainTracking {
    const val SCREEN = "/addproductpage - main"

    fun trackScreen() {
        ProductAddEditTracking.getTracker().sendScreenAuthenticated(SCREEN)
    }

    fun trackBack(shopId: String) {
        sendEditProductClick(shopId, "click back on main page")
    }

    fun trackAddPhoto(shopId: String) {
        sendEditProductClick(shopId, "click add product photo")
    }

    fun trackRemovePhoto(shopId: String) {
        sendEditProductClick(shopId, "click remove product image")
    }

    fun trackDragPhoto(shopId: String) {
        sendEditProductClick(shopId, "click drag product image")
    }

    fun clickOtherCategory(shopId: String) {
        sendEditProductClick(shopId, "click choose other categories")
    }

    fun clickBackOtherCategory(shopId: String) {
        sendEditProductClick(shopId, "click back choose other categories")
    }

    fun clickSaveOtherCategory(shopId: String) {
        sendEditProductClick(shopId, "click save choose other categories")
    }

    fun clickWholesale(shopId: String) {
        sendEditProductClick(shopId, "click wholesale button")
    }

    fun clickRemoveWholesale(shopId: String) {
        sendEditProductClick(shopId, "click remove wholesale price")
    }

    fun clickAddWholesale(shopId: String) {
        sendEditProductClick(shopId, "click add wholesale price")
    }

    fun clickBackOnVariantPage(shopId: String) {
        sendEditProductClick(shopId, "click back on variant page")
    }

    fun clickWholesaleOnVariantPage(shopId: String) {
        sendEditProductClick(shopId, "click add wholesale price on variant page")
    }

    fun clickSaveVariantPage(shopId: String) {
        sendEditProductClick(shopId, "click save on variant page")
    }

    fun clickPreorderButton(shopId: String) {
        sendEditProductClick(shopId, "click preorder button")
    }

    fun clickPreorderDropDownMenu(shopId: String) {
        sendEditProductClick(shopId, "click preorder dropdown menu")
    }

    fun clickCancelPreOrderDuration(shopId: String) {
        sendEditProductClick(shopId, "click cancel preorder duration")
    }

    fun clickPreOrderDuration(shopId: String, isDay: Boolean) {
        sendEditProductClick(shopId, "click preorder duration", if (isDay) {
            "day"
        } else {
            "week"
        })
    }

    fun clickContinue(shopId: String) {
        sendEditProductClick(shopId, "click continue on main page")
    }

}