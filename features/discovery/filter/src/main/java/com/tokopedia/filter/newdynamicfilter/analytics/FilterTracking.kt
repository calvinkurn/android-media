package com.tokopedia.filter.newdynamicfilter.analytics

import android.text.TextUtils

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

import java.util.ArrayList

/**
 * Created by henrypriyono on 1/5/18.
 */

object FilterTracking {

    fun eventFilterJourney(trackingData: FilterTrackingData,
                           filterName: String,
                           filterValue: String,
                           isInsideDetail: Boolean,
                           isActive: Boolean,
                           isAnnotation: Boolean) {

        val trackingMap = TrackAppUtils.gtmData(
                trackingData.event,
                trackingData.prefix + " - " + FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.CLICK.toLowerCase() + " - "
                        + filterName + ": " + filterValue + " - "
                        + (if (isInsideDetail) "inside lihat semua" else "outside lihat semua") + " - "
                        + if (isAnnotation) "annotation" else "standard",
                isActive.toString()
        )

        if (!TextUtils.isEmpty(trackingData.categoryId)) {
            trackingMap.put(FilterEventTracking.CustomDimension.CATEGORY_ID, trackingData.categoryId)
        }

        TrackApp.getInstance().getGTM().sendGeneralEvent(trackingMap)
    }

    fun eventApplyFilterDetail(trackingData: FilterTrackingData, filterName: String) {

        val trackingMap = TrackAppUtils.gtmData(
                trackingData.event,
                trackingData.prefix + " - " + FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.SIMPAN_ON_LIHAT_SEMUA + filterName,
                ""
        )

        if (!TextUtils.isEmpty(trackingData.categoryId)) {
            trackingMap.put(FilterEventTracking.CustomDimension.CATEGORY_ID, trackingData.categoryId)
        }

        TrackApp.getInstance().getGTM().sendGeneralEvent(trackingMap)
    }

    fun eventBackFromFilterDetail(trackingData: FilterTrackingData, filterName: String) {

        val trackingMap = TrackAppUtils.gtmData(
                trackingData.event,
                trackingData.prefix + " - " + FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.BACK_ON_LIHAT_SEMUA + filterName,
                ""
        )

        if (!TextUtils.isEmpty(trackingData.categoryId)) {
            trackingMap.put(FilterEventTracking.CustomDimension.CATEGORY_ID, trackingData.categoryId)
        }

        TrackApp.getInstance().getGTM().sendGeneralEvent(trackingMap)
    }

    fun eventNavigateToFilterDetail(trackingData: FilterTrackingData, filterName: String) {
        val trackingMap = TrackAppUtils.gtmData(
                trackingData.event,
                trackingData.prefix + " - " + FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.CLICK_LIHAT_SEMUA + filterName,
                ""
        )

        if (!TextUtils.isEmpty(trackingData.categoryId)) {
            trackingMap.put(FilterEventTracking.CustomDimension.CATEGORY_ID, trackingData.categoryId)
        }

        TrackApp.getInstance().getGTM().sendGeneralEvent(trackingMap)
    }

    fun eventOpenFilterPage(trackingData: FilterTrackingData) {
        val trackingMap = TrackAppUtils.gtmData(
                trackingData.event,
                trackingData.prefix + " - " + trackingData.filterCategory,
                FilterEventTracking.Action.CLICK_FILTER,
                ""
        )

        if (!TextUtils.isEmpty(trackingData.categoryId)) {
            trackingMap.put(FilterEventTracking.CustomDimension.CATEGORY_ID, trackingData.categoryId)
        }

        TrackApp.getInstance().getGTM().sendGeneralEvent(trackingMap)
    }

    fun eventApplyFilter(trackingData: FilterTrackingData,
                         screenName: String,
                         selectedFilter: Map<String, String>) {

        val trackingMap = TrackAppUtils.gtmData(
                trackingData.event,
                trackingData.prefix + " - " + trackingData.filterCategory,
                FilterEventTracking.Action.APPLY_FILTER.toLowerCase() + " - " + screenName,
                generateFilterEventLabel(selectedFilter)
        )

        if (!TextUtils.isEmpty(trackingData.categoryId)) {
            trackingMap.put(FilterEventTracking.CustomDimension.CATEGORY_ID, trackingData.categoryId)
        }

        TrackApp.getInstance().getGTM().sendGeneralEvent(trackingMap)
    }

    private fun generateFilterEventLabel(selectedFilter: Map<String, String>?): String {
        if (selectedFilter == null) {
            return ""
        }
        val filterList = arrayListOf<String>()
        for (entry in selectedFilter.entries) {
            filterList.add(entry.key + "=" + entry.value)
        }
        return TextUtils.join("&", filterList)
    }
}
