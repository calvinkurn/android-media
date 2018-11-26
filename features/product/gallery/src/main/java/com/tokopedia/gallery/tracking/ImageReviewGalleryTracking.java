package com.tokopedia.gallery.tracking;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

public class ImageReviewGalleryTracking {

    private static final String CLICK_PDP = "clickPDP";
    private static final String PRODUCT_DETAIL_PAGE = "product detail page";

    public static void eventClickReviewGalleryItem(Context context, String productId) {
        if (!(context.getApplicationContext() instanceof AbstractionRouter)) {
            return;
        }
        AnalyticTracker tracker = ((AbstractionRouter) context.getApplicationContext()).getAnalyticTracker();
        tracker.sendEventTracking(
                CLICK_PDP,
                PRODUCT_DETAIL_PAGE,
                "click - review gallery on review gallery list page",
                productId
        );
    }
}
