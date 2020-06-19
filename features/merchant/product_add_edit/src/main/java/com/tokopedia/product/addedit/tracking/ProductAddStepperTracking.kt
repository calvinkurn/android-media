package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.CAT_ADD_PRODUCT_PAGE
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.EVENT_CLICK_ADD_PRODUCT
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.KEY_SCREEN_NAME
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.KEY_SHOP_ID
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductClick


object ProductAddStepperTracking {
    const val SCREEN = "/addproductpage - stepper"

    fun trackScreen() {
        ProductAddEditTracking.getTracker().sendScreenAuthenticated(SCREEN)
    }

    fun trackBack(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click back on stepper page")
    }

    fun trackDraftYes(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click save as draft")
    }

    fun trackDraftCancel(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click cancel save as draft")
    }

    fun trackStart(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click start")
    }

    fun trackHelpProductQuality(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click toaster", "product photo quality")
    }

}