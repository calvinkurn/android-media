package com.tokopedia.shop.analytic

import com.tokopedia.trackingoptimizer.TrackingQueue

class OldShopPageTrackingBuyer(trackingQueue: TrackingQueue) : OldShopPageTrackingUser(trackingQueue) {
    fun clickSearchBox(pageName: String?) {
        sendEvent(
            OldShopPageTrackingConstant.CLICK_TOP_NAV,
            String.format(OldShopPageTrackingConstant.TOP_NAV, pageName),
            OldShopPageTrackingConstant.SHOP_SEARCH_PRODUCT_CLICK_SEARCH_BOX,
            "",
            null
        )
    }
}
