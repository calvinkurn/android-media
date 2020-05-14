package com.tokopedia.filter.newdynamicfilter.analytics

import android.text.TextUtils

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object FilterTracking {

    @JvmStatic
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

        TrackApp.getInstance().gtm.sendGeneralEvent(trackingMap)
    }

    @JvmStatic
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

        TrackApp.getInstance().gtm.sendGeneralEvent(trackingMap)
    }

    @JvmStatic
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

        TrackApp.getInstance().gtm.sendGeneralEvent(trackingMap)
    }

    @JvmStatic
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

        TrackApp.getInstance().gtm.sendGeneralEvent(trackingMap)
    }

    @JvmStatic
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

        TrackApp.getInstance().gtm.sendGeneralEvent(trackingMap)
    }

    @JvmStatic
    fun eventApplyFilter(trackingData: FilterTrackingData,
                         screenName: String,
                         selectedFilter: Map<String, String>?) {

        val trackingMap = TrackAppUtils.gtmData(
                trackingData.event,
                trackingData.prefix + " - " + trackingData.filterCategory,
                FilterEventTracking.Action.APPLY_FILTER.toLowerCase() + " - " + screenName,
                generateFilterEventLabel(selectedFilter)
        )

        if (!TextUtils.isEmpty(trackingData.categoryId)) {
            trackingMap.put(FilterEventTracking.CustomDimension.CATEGORY_ID, trackingData.categoryId)
        }

        TrackApp.getInstance().gtm.sendGeneralEvent(trackingMap)
    }

    @JvmStatic
    fun eventClickPricePills(pmin: Int,
                             pmax: Int,
                             position: Int,
                             isActive: Boolean) {

        val trackingMap = TrackAppUtils.gtmData(
                FilterEventTracking.Event.CLICK_FILTER,
                FilterEventTracking.Category.FILTER_JOURNEY,
                generateEventActionClickPricePills(pmin, pmax, position + 1),
                isActive.toString()
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(trackingMap)
    }

    private fun generateEventActionClickPricePills(pmin: Int, pmax: Int, position: Int): String {
        return String.format("click - Price Pills: %d&%d - %d - outside lihat semua", pmin, pmax, position)
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
