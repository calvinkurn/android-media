package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.CAT_ADD_PRODUCT_PAGE
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.EVENT_CLICK_ADD_PRODUCT
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.KEY_SCREEN_NAME
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.KEY_SHOP_ID

object ProductEditNotifTracking {
    fun clickFailed(shopId: String) {
        ProductAddEditTracking.getTracker().sendGeneralEventCustom(
            EVENT_CLICK_ADD_PRODUCT,
            CAT_ADD_PRODUCT_PAGE,
            "click failed edit product notification",
            "",
            mapOf(KEY_SHOP_ID to shopId))
    }
}