package com.tokopedia.filter.newdynamicfilter.analytics;

import android.content.Context;

import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

/**
 * Created by henrypriyono on 1/5/18.
 */

public class FilterTracking {


    private static FilterTracking mInstance = new FilterTracking();

    public FilterTracking() {
    }

    public static FilterTracking getmInstance() {
        return mInstance;
    }

    public void eventSearchResultFilterJourney(Context context,
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

    public void eventSearchResultApplyFilterDetail(Context context, String filterName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                FilterEventTracking.Event.SEARCH_RESULT,
                FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.SIMPAN_ON_LIHAT_SEMUA + filterName,
                ""
        ));
    }

    public void eventSearchResultBackFromFilterDetail(Context context, String filterName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                FilterEventTracking.Event.SEARCH_RESULT,
                FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.BACK_ON_LIHAT_SEMUA + filterName,
                ""
        ));
    }

    public void eventSearchResultNavigateToFilterDetail(Context context, String filterName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                FilterEventTracking.Event.SEARCH_RESULT,
                FilterEventTracking.Category.FILTER_JOURNEY,
                FilterEventTracking.Action.CLICK_LIHAT_SEMUA + filterName,
                ""
        ));
    }

    public  void eventSearchResultOpenFilterPageProduct(Context context) {
        eventSearchResultOpenFilterPage(context, "product");
    }

    public  void eventSearchResultOpenFilterPageCatalog(Context context) {
        eventSearchResultOpenFilterPage(context, "catalog");
    }

    public  void eventSearchResultOpenFilterPageShop(Context context) {
        eventSearchResultOpenFilterPage(context, "shop");
    }

    private  void eventSearchResultOpenFilterPage(Context context, String tabName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                FilterEventTracking.Event.SEARCH_RESULT,
                FilterEventTracking.Category.FILTER.toLowerCase() + " " + tabName,
                FilterEventTracking.Action.CLICK_FILTER,
                ""
        ));
    }
}
