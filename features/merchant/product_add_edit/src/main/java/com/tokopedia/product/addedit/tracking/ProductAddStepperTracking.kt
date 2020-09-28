package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductClick
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductImpression


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

    fun trackRemoveProductImage(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click remove product image")
    }

    fun trackDragPhoto(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click drag product image")
    }

    fun oopsConnectionPageScreen(userId: String, serverStatus: String, errorName: String) {
        sendAddProductImpression(userId, "impression add product error", "server error - $serverStatus - $errorName")
    }

}