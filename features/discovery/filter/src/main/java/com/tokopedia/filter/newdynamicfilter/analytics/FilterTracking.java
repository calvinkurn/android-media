package com.tokopedia.filter.newdynamicfilter.analytics;

import android.text.TextUtils;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Created by henrypriyono on 1/5/18.
 */

public class FilterTracking {

    public static void eventSearchResultFilterJourney(String trackingPrefix,
                                                      String filterName,
                                                      String filterValue,
                                                      boolean isInsideDetail, boolean isActive) {

        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                FilterEventTracking.Event.SEARCH_RESULT,
                trackingPrefix + " - " + FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.CLICK.toLowerCase() + " - "
                        + filterName + ": " + filterValue + " - "
                        + (isInsideDetail ? "inside lihat semua" : "outside lihat semua"),
                Boolean.toString(isActive)
        ));
    }

    public static void eventSearchResultApplyFilterDetail(String trackingPrefix, String filterName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                FilterEventTracking.Event.SEARCH_RESULT,
                trackingPrefix + " - " + FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.SIMPAN_ON_LIHAT_SEMUA + filterName,
                ""
        ));
    }

    public static void eventSearchResultBackFromFilterDetail(String trackingPrefix, String filterName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                FilterEventTracking.Event.SEARCH_RESULT,
                trackingPrefix + " - " + FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.BACK_ON_LIHAT_SEMUA + filterName,
                ""
        ));
    }

    public static void eventSearchResultNavigateToFilterDetail(String trackingPrefix, String filterName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                FilterEventTracking.Event.SEARCH_RESULT,
                trackingPrefix + " - " + FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.CLICK_LIHAT_SEMUA + filterName,
                ""
        ));
    }

    public static void eventSearchResultOpenFilterPage(String trackingPrefix, String tabName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                FilterEventTracking.Event.SEARCH_RESULT,
                trackingPrefix + " - " + FilterEventTracking.Category.FILTER.toLowerCase() + " " + tabName,
                FilterEventTracking.Action.CLICK_FILTER,
                ""
        ));
    }

    public static void eventSearchResultCloseBottomSheetFilter(String trackingPrefix,
                                                               String screenName,
                                                               Map<String, String> selectedFilter) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                FilterEventTracking.Event.SEARCH_RESULT,
                trackingPrefix + " - " + FilterEventTracking.Category.FILTER_PRODUCT,
                FilterEventTracking.Action.APPLY_FILTER.toLowerCase() + " - " + screenName,
                generateFilterEventLabel(selectedFilter)
        ));
    }

    public static void eventSearchResultFilter(String trackingPrefix, String screenName, Map<String, String> selectedFilter) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                FilterEventTracking.Event.SEARCH_RESULT,
                trackingPrefix + " - " + FilterEventTracking.Category.FILTER_PRODUCT,
                FilterEventTracking.Action.FILTER.toLowerCase() + " - " + screenName,
                generateFilterEventLabel(selectedFilter)
        ));
    }

    private static String generateFilterEventLabel(Map<String, String> selectedFilter) {
        if (selectedFilter == null) {
            return "";
        }
        List<String> filterList = new ArrayList<>();
        for (Map.Entry<String, String> entry : selectedFilter.entrySet()) {
            filterList.add(entry.getKey() + "=" + entry.getValue());
        }
        return TextUtils.join("&", filterList);
    }
}
