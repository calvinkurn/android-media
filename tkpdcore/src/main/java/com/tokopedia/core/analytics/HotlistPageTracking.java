package com.tokopedia.core.analytics;

import android.text.TextUtils;

import com.tokopedia.core.analytics.nishikino.model.EventTracking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nakama on 3/23/18.
 */

public class HotlistPageTracking extends TrackingUtils {

    public static void eventEnhance(Map<String, Object> dataLayer) {
        getGTMEngine().clearEnhanceEcommerce();
        getGTMEngine().eventTrackingEnhancedEcommerce(dataLayer);
    }

    public static void eventHotlistSort(String sortValue) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.HOTLIST,
                "hotlist page",
                "click sort",
                sortValue
        ).setUserId().getEvent());
    }

    public static void eventHotlistFilter(Map<String, String> selectedFilter) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.HOTLIST,
                "hotlist page",
                "click filter",
                generateFilterEventLabel(selectedFilter)
        ).setUserId().getEvent());
    }

    private static String generateFilterEventLabel(Map<String, String> selectedFilter) {
        List<String> filterList = new ArrayList<>();
        for (Map.Entry<String, String> entry : selectedFilter.entrySet()) {
            filterList.add(entry.getKey() + "=" + entry.getValue());
        }
        return TextUtils.join("&", filterList);
    }

    public static void eventAddWishlist(int position, String productName, String productID) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.HOTLIST,
                "hotlist page",
                "product list add to wishlist",
                String.format("%d - %s %s", position, productName, productID)
        ).getEvent());
    }

    public static void eventClickHastag(String url) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.HOTLIST,
                "hotlist page",
                "click category tagging",
                url
        ).getEvent());
    }

    public static void eventHotlistPromoImpression(String hotlistTitle, String title, String voucherCode) {
        sendGTMEvent(new EventTracking(
                AppEventTracking.Event.HOTLIST,
                "hotlist page",
                "click category tagging",
                String.format("%s - %s - %s", hotlistTitle, title, voucherCode)
        ).getEvent());
    }
}
