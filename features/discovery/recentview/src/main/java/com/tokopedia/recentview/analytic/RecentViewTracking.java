package com.tokopedia.recentview.analytic;

import android.content.Context;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.core.analytics.UnifyTracking;

import java.util.List;
import java.util.Map;

public class RecentViewTracking {

    public static void trackEventClickOnProductRecentView(Context context,
                                                          Object dataItem,
                                                          String userId) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", "productClick",
                        "eventCategory", "recent view",
                        "eventAction", "click on product",
                        "eventLabel", "",
                        "ecommerce", DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "click",
                                DataLayer.mapOf("actionField", DataLayer.mapOf("list", "/recent"),
                                        "products", DataLayer.listOf(dataItem)
                                )
                        ),
                        "userId", userId
                )
        );
    }

    public static void trackEventImpressionOnProductRecentView(Context context,
                                                         List<Object> dataItemList,
                                                         String userId) {
        if (context == null || !(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();

        tracker.sendEnhancedEcommerce(
                DataLayer.mapOf("event", "productView",
                        "eventCategory", "recent view",
                        "eventAction", "impression on product",
                        "eventLabel", "",
                        "ecommerce",
                        DataLayer.mapOf(
                                "currencyCode", "IDR",
                                "impressions", DataLayer.listOf(
                                        dataItemList.toArray(new Object[dataItemList.size()])
                                )),
                        "userId", userId
                )
        );
    }
}
