package com.tokopedia.product.addedit.tracking

import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.CAT_DRAFT_PRODUCT_PAGE
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.EVENT_CLICK_ADD_PRODUCT

object ProductAddEditDraftListPageTracking {

    const val CLICK_ADD_PRODUCT = "click add product"
    const val CLICK_ADD_PRODUCT_WITHOUT_DRAFT = "click add product without draft"

    fun eventAddEditDraftClicked(shopId: String, action: String) {
        ProductAddEditTracking.getTracker().sendGeneralEventCustom(
                EVENT_CLICK_ADD_PRODUCT,
                CAT_DRAFT_PRODUCT_PAGE,
                action,
                "",
                mapOf("shopId" to shopId))
    }
}