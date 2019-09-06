package com.tokopedia.filter.newdynamicfilter.analytics;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.trackingoptimizer.TrackingQueue;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by henrypriyono on 1/5/18.
 */

public class FilterTracking {

    public static void eventSearchResultFilterJourney(Context context,
                                                      String filterName,
                                                      String filterValue,
                                                      boolean isInsideDetail, boolean isActive) {

        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                FilterEventTracking.Event.SEARCH_RESULT,
                FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.CLICK.toLowerCase() + " - "
                        + filterName + ": " + filterValue + " - "
                        + (isInsideDetail ? "inside lihat semua" : "outside lihat semua"),
                Boolean.toString(isActive)
        ));
    }

    public static void eventSearchResultApplyFilterDetail(Context context, String filterName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                FilterEventTracking.Event.SEARCH_RESULT,
                FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.SIMPAN_ON_LIHAT_SEMUA + filterName,
                ""
        ));
    }

    public static void eventSearchResultBackFromFilterDetail(Context context, String filterName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                FilterEventTracking.Event.SEARCH_RESULT,
                FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.BACK_ON_LIHAT_SEMUA + filterName,
                ""
        ));
    }

    public static void eventSearchResultNavigateToFilterDetail(Context context, String filterName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                FilterEventTracking.Event.SEARCH_RESULT,
                FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.CLICK_LIHAT_SEMUA + filterName,
                ""
        ));
    }

    public static void eventSearchResultOpenFilterPageProduct(Context context) {
        eventSearchResultOpenFilterPage(context, "product");
    }

    public static void eventSearchResultOpenFilterPageCatalog(Context context) {
        eventSearchResultOpenFilterPage(context,"catalog");
    }

    public static void eventSearchResultOpenFilterPageShop(Context context) {
        eventSearchResultOpenFilterPage(context,"shop");
    }

    private static void eventSearchResultOpenFilterPage(Context context, String tabName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                FilterEventTracking.Event.SEARCH_RESULT,
                FilterEventTracking.Category.FILTER.toLowerCase() + " " + tabName,
                FilterEventTracking.Action.CLICK_FILTER,
                ""
        ));
    }
}
