package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.CAT_ADD_PRODUCT_PAGE
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.CAT_EDIT_PRODUCT_PAGE
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.EVENT_CLICK_ADD_PRODUCT
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.EVENT_CLICK_EDIT_PRODUCT
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.KEY_SCREEN_NAME
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.KEY_SHOP_ID

object ProductEditChooseImageTracking {

    fun trackBack(shopId: String) {
        sendEditProductClick(shopId, "click back on gallery page")
    }

    fun trackContinue(shopId: String) {
        sendEditProductClick(shopId, "click continue on gallery page")
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