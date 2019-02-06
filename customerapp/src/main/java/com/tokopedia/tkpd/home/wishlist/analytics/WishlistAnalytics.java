package com.tokopedia.tkpd.home.wishlist.analytics;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.app.MainApplication;

import java.util.List;

/**
 * Created by meta on 18/09/18.
 */
public class WishlistAnalytics {

    private static final String WISHLIST_PAGE = "wishlist page";
    private static final String CLICK_CART_WISHLIST = "click - cek keranjang on wishlist";
    public static final String DEFAULT_VALUE_NONE_OTHER = "none / other";

    private static final String CLICK_WISHLIST = "Click Wishlist";
    private static final String LONG_PRESS_SHORTCUT_WISHLIST = "Share";

    private AnalyticTracker analyticTracker;

    public void trackEventAddToCardProductWishlist(Object dataItem) {
        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", "addToCart",
                        "eventCategory", "wishlist page",
                        "eventAction", "click - beli on wishlist",
                        "eventLabel", DEFAULT_VALUE_NONE_OTHER,
                        "ecommerce", DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "add",
                                DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/wishlist"),
                                        "products", DataLayer.listOf(dataItem)
                                )
                        )
                )
        );
    }
    public void trackEventClickOnProductWishlist(String position,
                                                        Object dataItem) {
        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", "productClick",
                        "eventCategory", "wishlist page",
                        "eventAction", "click product",
                        "eventLabel", position,
                        "ecommerce",
                        DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "click",
                                DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/wishlist"),
                                        "products", DataLayer.listOf(dataItem)
                                )
                        )
                )
        );
    }

    public void trackEventImpressionOnProductWishlist(List<Object> dataItemList) {
        analyticTracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", "productView",
                        "eventCategory", "wishlist page",
                        "eventAction", "product impressions",
                        "eventLabel", "",
                        "ecommerce",
                        DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                        dataItemList.toArray(new Object[dataItemList.size()])
                                ))
                )
        );
    }

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

    public void eventWishlistShortcut() {
        analyticTracker.sendEventTracking(new EventTracking(
                AppEventTracking.Event.LONG_CLICK,
                AppEventTracking.Category.LONG_PRESS,
                CLICK_WISHLIST,
                LONG_PRESS_SHORTCUT_WISHLIST
        ).setUserId(MainApplication.getTkpdCoreRouter().legacySessionHandler().getUserId()).getEvent());
    }
}
