package com.tokopedia.tkpd.home.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.network.entity.home.recentView.RecentViewData;
import com.tokopedia.core.var.RecyclerViewItem;

import java.util.List;

/**
 * Created by m.normansyah on 01/12/2015.
 */
public interface ProductHistory {
    String TAG = "MNORMANSYAH";
    String messageTAG = "WishList : ";
    String PRODUCT_HISTORY_MODEL = "PRODUCT_HISTORY_MODEL";

    void initDataInstance(Context context);

    /**
     * grab some data from rotation
     * @param savedInstanceState
     */
    void fetchSavedsInstance(Bundle savedInstanceState);

    /**
     * initiate analytic variable
     * @param context
     */
    void initAnalyticsHandler(Context context);

    /**
     * set data to adapter, lebih baik jadi loadData
     */
    void setData();

    /**
     * get 8 product feed data and 4 recent last seen data
     */
    void refreshData(Context context);

    /**
     * when user scroll to the bottom of reccylerview than load next 8
     * product feed data
     */
    void loadMore(Context context);

    /**
     * do some activities in onSavedInstanceState
     * @param saved
     */
    void saveDataBeforeRotate(Bundle saved);

    /**
     * use this method so that it will skip the flow after rotation
     * @return
     */
    boolean isAfterRotation();

    /**
     * @return data for this
     */
    List<RecyclerViewItem> getData();

    void fetchDataFromInternet(Context context);

    void subscribe();

    void unSubscribe();

    void fetchDataFromCache(Context context);

    void setData(RecentViewData recentViewData);
}
