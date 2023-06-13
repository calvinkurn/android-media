package com.tokopedia.shop.analytic

import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_TOP_NAV
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_SEARCH_PRODUCT_CLICK_SEARCH_BOX
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TOP_NAV
import com.tokopedia.trackingoptimizer.TrackingQueue

class OldShopPageTrackingBuyer(trackingQueue: TrackingQueue) : OldShopPageTrackingUser(trackingQueue) {
    fun clickSearchBox(pageName: String?) {
        sendEvent(
            CLICK_TOP_NAV,
            String.format(TOP_NAV, pageName),
            SHOP_SEARCH_PRODUCT_CLICK_SEARCH_BOX,
            "",
            null
        )
    }
}
