package com.tokopedia.product.addedit.tracking

import android.content.Context
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.KEY_SHOP_ID
import com.tokopedia.product.addedit.tracking.ProductAddEditTracking.sendAddProductClick

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

    private fun sendAddProductIris(context: Context, shopId: String, action: String, label: String = "") {
        IrisAnalytics.getInstance(context).saveEvent(
            createEventMap(
                EVENT_ADD_PRODUCT_IRIS,
                CAT_SHOP_PAGE_SELLER,
                action,
                "",
                mapOf(KEY_SHOP_ID to shopId)
            )
        )
    }
}
