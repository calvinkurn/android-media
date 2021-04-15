package com.tokopedia.shop.analytic

import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.shop.analytic.OldShopPageTrackingUser
import com.tokopedia.shop.analytic.model.CustomDimensionShopPage
import com.tokopedia.shop.analytic.OldShopPageTrackingConstant

class OldShopPageTrackingBuyer(
        trackingQueue: TrackingQueue) : OldShopPageTrackingUser(trackingQueue) {
    fun followFromZeroFollower(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.CLICK_SHOP_PAGE,
                OldShopPageTrackingConstant.SHOP_PAGE_BUYER,
                joinDash(OldShopPageTrackingConstant.TOP_SECTION, OldShopPageTrackingConstant.CLICK),
                OldShopPageTrackingConstant.CLICK_FOLLOW_FROM_ZERO_FOLLOWER,
                customDimensionShopPage)
    }

    fun impressionFollowFromZeroFollower(customDimensionShopPage: CustomDimensionShopPage?) {
        sendEvent(OldShopPageTrackingConstant.VIEW_SHOP_PAGE,
                OldShopPageTrackingConstant.SHOP_PAGE_BUYER,
                joinDash(OldShopPageTrackingConstant.TOP_SECTION, OldShopPageTrackingConstant.IMPRESSION),
                OldShopPageTrackingConstant.IMPRESSION_FOLLOW_FROM_ZERO_FOLLOWER,
                customDimensionShopPage)
    }

    fun clickSearchBox(pageName: String?) {
        sendEvent(
                OldShopPageTrackingConstant.CLICK_TOP_NAV, String.format(OldShopPageTrackingConstant.TOP_NAV, pageName),
                OldShopPageTrackingConstant.SHOP_SEARCH_PRODUCT_CLICK_SEARCH_BOX,
                "",
                null
        )
    }
}