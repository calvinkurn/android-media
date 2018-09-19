package com.tokopedia.tkpd.home.wishlist.analytics;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.core.analytics.AppEventTracking;

/**
 * Created by meta on 18/09/18.
 */
public class WishlistAnalytics {

    private static final String WISHLIST_PAGE = "wishlist page";
    private static final String CLICK_CART_WISHLIST = "click - cek keranjang on wishlist";

    private AnalyticTracker analyticTracker;

    public WishlistAnalytics(AnalyticTracker analyticTracker) {
        this.analyticTracker = analyticTracker;
    }

    public void eventClickCariWishlist(String query) {
        if (analyticTracker != null) {
            analyticTracker.sendEventTracking(
                    AppEventTracking.Event.WISHLIST,
                    AppEventTracking.Category.HOMEPAGE.toLowerCase(),
                    AppEventTracking.Action.CLICK_SEARCH_ITEM_WISHLIST,
                    query
            );
        }
    }

    public void eventSearchWishlist(String label) {
        if (analyticTracker != null) {
            analyticTracker.sendEventTracking(
                AppEventTracking.Event.HOME_WISHLIST,
                AppEventTracking.Category.HOMEPAGE,
                AppEventTracking.Action.WISHLIST_SEARCH_ITEM,
                label
            );
        }
    }

    public void eventRemoveWishlist() {
        if (analyticTracker != null) {
            analyticTracker.sendEventTracking(
                    AppEventTracking.Event.WISHLIST,
                    AppEventTracking.Category.HOMEPAGE.toLowerCase(),
                    AppEventTracking.Action.CLICK_REMOVE_WISHLIST,
                    ""
            );
        }
    }

    public void eventClickCariEmptyWishlist() {
        if (analyticTracker != null) {
            analyticTracker.sendEventTracking(
                    AppEventTracking.Event.WISHLIST,
                    AppEventTracking.Category.HOMEPAGE.toLowerCase(),
                    AppEventTracking.Action.CLICK_EMPTY_SEARCH_WISHLIST,
                    ""
            );
        }
    }

    public void eventClickCartWishlist() {
        if (analyticTracker != null) {
            analyticTracker.sendEventTracking(
                    AppEventTracking.Event.WISHLIST,
                    WISHLIST_PAGE,
                    CLICK_CART_WISHLIST,
                    ""
            );
        }
    }
}
