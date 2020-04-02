package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.CAT_ADD_PRODUCT_PAGE
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.EVENT_CLICK_ADD_PRODUCT
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.KEY_SCREEN_NAME
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.KEY_SHOP_ID
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductClick

object ProductAddDescriptionTracking {
    const val SCREEN = "/addproductpage - description"

    fun trackScreen() {
        ProductAddEditTracking.getTracker().sendScreenAuthenticated(SCREEN)
    }

    fun clickBack(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click back on product description page")
    }

    fun clickHelpWriteDescription(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click toaster", "clear product description")
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

    fun clickHelpVariant(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click toaster", "determine product variant")
    }

    fun clickContinue(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click continue on product description page")
    }

}