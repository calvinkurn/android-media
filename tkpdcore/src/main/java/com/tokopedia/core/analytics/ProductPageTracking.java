package com.tokopedia.core.analytics;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import java.util.Map;

/**
 * Created by nakama on 4/2/18.
 */

public class ProductPageTracking {

    public static final String CLICK_PDP = "clickPDP";
    public static final String PRODUCT_DETAIL_PAGE = "product detail page";

    public static void eventEnhanceProductDetail(Context context, Map<String, Object> maps) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEnhancedEcommerce(maps);
    }

    public static void eventClickAtcNotLogin(Context context, String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - tambah ke keranjang - before login",
                productId
        );
    }

    public static void eventClickBuyNotLogin(Context context, String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - beli - before login",
                productId
        );
    }

    public static void eventClickBuyInVariantNotLogin(Context context, String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - beli on variants page - before login",
                productId
        );
    }

    public static void eventClickAtcInVariantNotLogin(Context context, String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - tambah ke keranjang on variants page - before login",
                productId
        );
    }

    public static void eventClickBuyTriggerVariant(Context context, String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - beli - redirect to variants page",
                productId
        );
    }

    public static void eventClickAtcTriggerVariant(Context context, String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - tambah ke keranjang - redirect to variants page",
                productId
        );
    }
}
