package com.tokopedia.product.addedit.tracking

import android.content.Context
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.KEY_SHOP_ID
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductClick
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductClickWithoutScreen
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductImpression

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

    fun clickInsurance(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click shipping insurance button")
    }

    fun clickCancelChangeWeight(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click cancel change weight")
    }

    fun clickWeightDropDown(shopId: String) {
        sendAddProductClick(SCREEN, shopId, "click weight dropdown menu")
    }

    fun clickChooseWeight(shopId: String, isGram: Boolean) {
        sendAddProductClick(SCREEN, shopId, "click choose weight", if (isGram) {
            "gram"
        } else {
            "kilogram"
        })
    }

    fun clickFinish(shopId: String, isSuccess: Boolean, errorName: String = "", errorMessage: String = "") {
        if (isSuccess) {
            sendAddProductClick(SCREEN, shopId, "click finish success", "")
        } else {
            sendAddProductClickWithoutScreen(shopId, "click finish error", "$errorMessage - $errorName")
        }
    }

    fun uploadImageFailed(userId: String, errorName: String) {
        sendAddProductImpression(userId, "impression add product error", "validation error - (Upload Image) $errorName")
    }

    fun redirectToShopPage(context: Context, shopId: String) {
        sendAddProductIris(context, shopId, "impression add product - shop page")
    }

    fun redirectToDraftPage(context: Context, shopId: String) {
        sendAddProductIris(context, shopId, "impression add product - product draft page")
    }

    private fun sendAddProductIris(context: Context, shopId: String, action: String, label: String = "") {
        IrisAnalytics.getInstance(context).saveEvent(
            createEventMap(EVENT_ADD_PRODUCT_IRIS,
                CAT_SHOP_PAGE_SELLER,
                action,
                "", mapOf(KEY_SHOP_ID to shopId)))
    }

}