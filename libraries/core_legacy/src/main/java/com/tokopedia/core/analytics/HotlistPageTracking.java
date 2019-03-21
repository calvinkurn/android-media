package com.tokopedia.core.analytics;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.tagmanager.DataLayer;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.track.TrackApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nakama on 3/23/18.
 */

public class HotlistPageTracking extends TrackingUtils {

    public static void eventEnhance(Context context, Map<String, Object> dataLayer) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(dataLayer);
    }

    public static void eventHotlistSort(Context context, String sortValue) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.HOTLIST,
                AppEventTracking.Event.HOTLIST_PAGE,
                "click sort",
                sortValue
        ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent());
    }

    public static void eventHotlistFilter(Context context, Map<String, String> selectedFilter) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.HOTLIST,
                AppEventTracking.Event.HOTLIST_PAGE,
                "click filter",
                generateFilterEventLabel(selectedFilter)
        ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent());
    }

    private static String generateFilterEventLabel(Map<String, String> selectedFilter) {
        List<String> filterList = new ArrayList<>();
        for (Map.Entry<String, String> entry : selectedFilter.entrySet()) {
            filterList.add(entry.getKey() + "=" + entry.getValue());
        }
        return TextUtils.join("&", filterList);
    }

    public static void eventAddWishlist(Context context, int position, String productName, String productID) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.HOTLIST,
                AppEventTracking.Event.HOTLIST_PAGE,
                "product list add to wishlist",
                String.format("%d - %s %s", position, productName, productID)
        ).getEvent());
    }

    public static void eventClickHastag(Context context, String url) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.HOTLIST,
                AppEventTracking.Event.HOTLIST_PAGE,
                "click category tagging",
                url
        ).getEvent());
    }

    public static void clickTnCButtonHotlistPromo(String hotlistName, String promoName, String promoCode) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                DataLayer.mapOf(
                        "event", "clickHotlist",
                        "eventCategory", "hotlist page",
                        "eventAction", "hotlist promo click syarat ketentuan",
                        "eventLabel", String.format("%s - %s - %s", hotlistName, promoName, promoCode)
                )
        );
    }

    public static void clickCopyButtonHotlistPromo(String hotlistName, String promoName, String promoCode) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                DataLayer.mapOf(
                        "event", "clickHotlist",
                        "eventCategory", "hotlist page",
                        "eventAction", "hotlist promo click salin kode",
                        "eventLabel", String.format("%s - %s - %s", hotlistName, promoName, promoCode)
                )
        );
    }

    public static void eventHotlistPromoImpression(Context context, String hotlistTitle, String title, String voucherCode) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.HOTLIST,
                AppEventTracking.Event.HOTLIST_PAGE,
                "click category tagging",
                String.format("%s - %s - %s", hotlistTitle, title, voucherCode)
        ).getEvent());
    }

    public static void eventShareHotlist(Context context, String channel) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.HOTLIST,
                AppEventTracking.Event.HOTLIST_PAGE,
                "click social share",
                channel
        ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent());
    }

    public static void eventHotlistDisplay(Context context, String display) {
        sendGTMEvent(context, new EventTracking(
                AppEventTracking.Event.HOTLIST,
                AppEventTracking.Event.HOTLIST_PAGE,
                "click display",
                display
        ).setUserId(RouterUtils.getRouterFromContext(context).legacySessionHandler().getUserId()).getEvent());
    }
}
