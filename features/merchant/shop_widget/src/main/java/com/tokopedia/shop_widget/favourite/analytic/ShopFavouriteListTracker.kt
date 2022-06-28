package com.tokopedia.shop_widget.favourite.analytic

import com.tokopedia.shop_widget.common.analytic.ShopWidgetTracker
import com.tokopedia.shop_widget.common.analytic.ShopWidgetTrackingConstant.CLICK
import com.tokopedia.shop_widget.common.analytic.ShopWidgetTrackingConstant.CLICK_FOLLOW_FROM_ZERO_FOLLOWER
import com.tokopedia.shop_widget.common.analytic.ShopWidgetTrackingConstant.CLICK_SHOP_PAGE
import com.tokopedia.shop_widget.common.analytic.ShopWidgetTrackingConstant.IMPRESSION
import com.tokopedia.shop_widget.common.analytic.ShopWidgetTrackingConstant.IMPRESSION_FOLLOW_FROM_ZERO_FOLLOWER
import com.tokopedia.shop_widget.common.analytic.ShopWidgetTrackingConstant.SHOP_PAGE_BUYER
import com.tokopedia.shop_widget.common.analytic.ShopWidgetTrackingConstant.TOP_SECTION
import com.tokopedia.shop_widget.common.analytic.ShopWidgetTrackingConstant.VIEW_SHOP_PAGE
import com.tokopedia.shop_widget.common.analytic.model.CustomDimensionShopWidget
import com.tokopedia.trackingoptimizer.TrackingQueue

class ShopFavouriteListTracker(trackingQueue: TrackingQueue): ShopWidgetTracker(trackingQueue) {

    fun followFromZeroFollower(customDimensionShopWidget: CustomDimensionShopWidget?) {
        sendEvent(
            CLICK_SHOP_PAGE,
            SHOP_PAGE_BUYER,
            joinDash(TOP_SECTION, CLICK),
            CLICK_FOLLOW_FROM_ZERO_FOLLOWER,
            customDimensionShopWidget
        )
    }

    fun impressionFollowFromZeroFollower(customDimensionShopWidget: CustomDimensionShopWidget?) {
        sendEvent(
            VIEW_SHOP_PAGE,
            SHOP_PAGE_BUYER,
            joinDash(TOP_SECTION, IMPRESSION),
            IMPRESSION_FOLLOW_FROM_ZERO_FOLLOWER,
            customDimensionShopWidget
        )
    }

}