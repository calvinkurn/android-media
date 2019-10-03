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

    public static void eventFilterJourney(FilterTrackingData trackingData,
                                          String filterName,
                                          String filterValue,
                                          boolean isInsideDetail,
                                          boolean isActive,
                                          boolean isAnnotation) {

        Map<String, Object> trackingMap = TrackAppUtils.gtmData(
                trackingData.getEvent(),
                trackingData.getPrefix() + " - " + FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.CLICK.toLowerCase() + " - "
                        + filterName + ": " + filterValue + " - "
                        + (isInsideDetail ? "inside lihat semua" : "outside lihat semua") + " - "
                        + (isAnnotation ? "annotation" : "standard"),
                Boolean.toString(isActive)
        );

        if (!TextUtils.isEmpty(trackingData.getCategoryId())) {
            trackingMap.put(FilterEventTracking.CustomDimension.CATEGORY_ID, trackingData.getCategoryId());
        }

        TrackApp.getInstance().getGTM().sendGeneralEvent(trackingMap);
    }

    public static void eventApplyFilterDetail(FilterTrackingData trackingData, String filterName) {

        Map<String, Object> trackingMap = TrackAppUtils.gtmData(
                trackingData.getEvent(),
                trackingData.getPrefix() + " - " + FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.SIMPAN_ON_LIHAT_SEMUA + filterName,
                ""
        );

        if (!TextUtils.isEmpty(trackingData.getCategoryId())) {
            trackingMap.put(FilterEventTracking.CustomDimension.CATEGORY_ID, trackingData.getCategoryId());
        }

        TrackApp.getInstance().getGTM().sendGeneralEvent(trackingMap);
    }

    public static void eventBackFromFilterDetail(FilterTrackingData trackingData, String filterName) {

        Map<String, Object> trackingMap = TrackAppUtils.gtmData(
                trackingData.getEvent(),
                trackingData.getPrefix() + " - " + FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.BACK_ON_LIHAT_SEMUA + filterName,
                ""
        );

        if (!TextUtils.isEmpty(trackingData.getCategoryId())) {
            trackingMap.put(FilterEventTracking.CustomDimension.CATEGORY_ID, trackingData.getCategoryId());
        }

        TrackApp.getInstance().getGTM().sendGeneralEvent(trackingMap);
    }

    public static void eventNavigateToFilterDetail(FilterTrackingData trackingData, String filterName) {
        Map<String, Object> trackingMap = TrackAppUtils.gtmData(
                trackingData.getEvent(),
                trackingData.getPrefix() + " - " + FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.CLICK_LIHAT_SEMUA + filterName,
                ""
        );

        if (!TextUtils.isEmpty(trackingData.getCategoryId())) {
            trackingMap.put(FilterEventTracking.CustomDimension.CATEGORY_ID, trackingData.getCategoryId());
        }

        TrackApp.getInstance().getGTM().sendGeneralEvent(trackingMap);
    }

    public static void eventOpenFilterPage(FilterTrackingData trackingData) {
        Map<String, Object> trackingMap = TrackAppUtils.gtmData(
                trackingData.getEvent(),
                trackingData.getPrefix() + " - " + trackingData.getFilterCategory(),
                FilterEventTracking.Action.CLICK_FILTER,
                ""
        );

        if (!TextUtils.isEmpty(trackingData.getCategoryId())) {
            trackingMap.put(FilterEventTracking.CustomDimension.CATEGORY_ID, trackingData.getCategoryId());
        }

        TrackApp.getInstance().getGTM().sendGeneralEvent(trackingMap);
    }

    public static void eventApplyFilter(FilterTrackingData trackingData,
                                        String screenName,
                                        Map<String, String> selectedFilter) {

        Map<String, Object> trackingMap = TrackAppUtils.gtmData(
                trackingData.getEvent(),
                trackingData.getPrefix() + " - " + trackingData.getFilterCategory(),
                FilterEventTracking.Action.APPLY_FILTER.toLowerCase() + " - " + screenName,
                generateFilterEventLabel(selectedFilter)
        );

        if (!TextUtils.isEmpty(trackingData.getCategoryId())) {
            trackingMap.put(FilterEventTracking.CustomDimension.CATEGORY_ID, trackingData.getCategoryId());
        }

        TrackApp.getInstance().getGTM().sendGeneralEvent(trackingMap);
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
