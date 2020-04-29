package com.tokopedia.shop.analytic;

import com.tokopedia.shop.analytic.model.CustomDimensionShopPage;
import com.tokopedia.trackingoptimizer.TrackingQueue;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.CLICK;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.CLICK_FOLLOW_FROM_ZERO_FOLLOWER;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.CLICK_SHOP_PAGE;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.CLICK_TOP_NAV;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.IMPRESSION;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.IMPRESSION_FOLLOW_FROM_ZERO_FOLLOWER;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.SHOP_PAGE_BUYER;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.SHOP_SEARCH_PRODUCT_CLICK_SEARCH_BOX;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.TOP_NAV;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.TOP_SECTION;
import static com.tokopedia.shop.analytic.OldShopPageTrackingConstant.VIEW_SHOP_PAGE;

public class OldShopPageTrackingBuyer extends OldShopPageTrackingUser {

    public OldShopPageTrackingBuyer(
                                 TrackingQueue trackingQueue) {
        super(trackingQueue);
    }

    public void followFromZeroFollower(CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(CLICK_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                joinDash(TOP_SECTION, CLICK),
                CLICK_FOLLOW_FROM_ZERO_FOLLOWER,
                customDimensionShopPage);
    }

    public void impressionFollowFromZeroFollower(CustomDimensionShopPage customDimensionShopPage) {
        sendEvent(VIEW_SHOP_PAGE,
                SHOP_PAGE_BUYER,
                joinDash(TOP_SECTION, IMPRESSION),
                IMPRESSION_FOLLOW_FROM_ZERO_FOLLOWER,
                customDimensionShopPage);
    }

    public void clickSearchBox(String pageName) {
        sendEvent(
                CLICK_TOP_NAV,
                String.format(TOP_NAV, pageName),
                SHOP_SEARCH_PRODUCT_CLICK_SEARCH_BOX,
                "",
                null
        );
    }
}
