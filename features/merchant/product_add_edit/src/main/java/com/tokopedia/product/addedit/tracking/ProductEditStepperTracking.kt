package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.CAT_EDIT_PRODUCT_PAGE
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.EVENT_CLICK_EDIT_PRODUCT
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.KEY_SHOP_ID
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendEditProductImpression


object ProductEditStepperTracking {
    const val SCREEN = "/editproductpage - "

    fun trackScreen(shopId: String, isRegular: Boolean, isPowerMerchant: Boolean) {
        ProductAddEditTracking.getTracker().sendScreenAuthenticated(SCREEN + shopId,
            mapOf("shopType" to if (isRegular) {
                "regular"
            } else if (isPowerMerchant) {
                "power_merchant"
            } else {
                "official_store"
            },
                "pageType" to "/shoppage"))
    }

    fun trackBack(shopId: String) {
        sendEditProductClick(shopId, "click back on stepper page")
    }

    fun trackClickChangeProductPic(shopId: String) {
        sendEditProductClick(shopId, "click change product picture")
    }

    fun trackRemoveProductImage(shopId: String) {
        sendEditProductClick(shopId, "click remove product image")
    }

    fun trackChangeProductDetail(shopId: String) {
        sendEditProductClick(shopId, "click change product detail")
    }

    fun trackChangeProductDesc(shopId: String) {
        sendEditProductClick(shopId, "click change product description")
    }

    fun trackAddProductVariant(shopId: String) {
        sendEditProductClick(shopId, "click add product variant")
    }

    fun trackClickHelpPriceVariant(shopId: String) {
        sendEditProductClick(shopId, "click toaster", "determine stock and price")
    }

    fun trackChangeShipping(shopId: String) {
        sendEditProductClick(shopId, "click change shipping detail")
    }

    fun trackChangePromotion(shopId: String) {
        sendEditProductClick(shopId, "click change promotion")
    }

    fun trackChangeProductStatus(shopId: String) {
        sendEditProductClick(shopId, "click product status button")
    }

    fun trackFinishButton(shopId: String) {
        sendEditProductClick(shopId, "click finish on stepper page")
    }

    fun trackDragPhoto(shopId: String) {
        sendEditProductClick(shopId, "click drag product image")
    }

    fun oopsConnectionPageScreen(userId: String, serverStatus: String, errorName: String) {
        sendEditProductImpression(userId, "impression edit product error", "server error - $serverStatus - $errorName")
    }

    fun trackFinishService(shopId: String, isSuccess: Boolean) {
        sendEditProductClick(shopId, "click finish", if (isSuccess) {
            "success"
        } else {
            "error"
        })
    }

    fun trackFinishService(shopId: String) {
        sendEditProductClick(shopId, "click failed edit product notification", "")
    }

    private fun sendEditProductClick(shopId: String, action: String, label: String = "") {
        ProductAddEditTracking.getTracker().sendGeneralEventCustom(
            EVENT_CLICK_EDIT_PRODUCT,
            CAT_EDIT_PRODUCT_PAGE,
            action,
            label,
            mapOf(KEY_SHOP_ID to shopId))
    }

}