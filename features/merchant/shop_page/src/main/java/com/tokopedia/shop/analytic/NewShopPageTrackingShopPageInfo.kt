package com.tokopedia.shop.analytic

import com.tokopedia.shop.analytic.NewShopPageTrackingConstant.*
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.trackingoptimizer.TrackingQueue


class NewShopPageTrackingShopPageInfo(
        trackingQueue: TrackingQueue
) : NewShopPageTracking(trackingQueue) {

    override fun clickBackArrow(isMyShop: Boolean, customDimensionShopPage: CustomDimensionShopPage?) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SHOP_PROFILE_PAGE_BUYER,
                CLICK_BACK,
                "",
                customDimensionShopPage)
    }

    fun clickShareButton(customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(CLICK_SHOP_PAGE,
                SHOP_PROFILE_PAGE_BUYER,
                CLICK_SHARE,
                "",
                customDimensionShopPage)
    }

}
