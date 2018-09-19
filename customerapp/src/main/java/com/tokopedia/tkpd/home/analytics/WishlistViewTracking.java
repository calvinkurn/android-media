package com.tokopedia.tkpd.home.analytics;

import android.content.Context;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import java.util.List;

public class WishlistViewTracking {

    public static final String DEFAULT_VALUE_NONE_OTHER = "none / other";


    public static void trackEventAddToCardProductWishlist(Context context,
                                                          Object dataItem) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", "addToCart",
                        "eventCategory", "wishlist page",
                        "eventAction", "click - beli on wishlist",
                        "eventLabel", DEFAULT_VALUE_NONE_OTHER,
                        "ecommerce", DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "click",
                                DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/recent"),
                                        "products", DataLayer.listOf(dataItem)
                                )
                        )
                )
        );
    }
}
