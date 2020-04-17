package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.CAT_EDIT_PRODUCT_PAGE
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.EVENT_CLICK_EDIT_PRODUCT
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.KEY_SHOP_ID

object ProductEditNotifTracking {
    fun clickFailed(shopId: String) {
        ProductAddEditTracking.getTracker().sendGeneralEventCustom(
                EVENT_CLICK_EDIT_PRODUCT,
                CAT_EDIT_PRODUCT_PAGE,
                "click failed edit product notification",
                "",
                mapOf(KEY_SHOP_ID to shopId))
    }
}