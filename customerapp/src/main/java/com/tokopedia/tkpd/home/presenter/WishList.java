package com.tokopedia.tkpd.home.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.tokopedia.tkpd.home.wishlist.domain.model.GqlWishListDataResponse;
import com.tokopedia.core.var.RecyclerViewItem;

import java.util.List;

/**
 * Created by m.normansyah on 01/12/2015.
 */
public interface WishList {
    String TAG = "MNORMANSYAH";
    String messageTAG = "WishList : ";
    String WISHLIST_MODEL = "WISHLIST_MODEL";
    String WISHLIST_ENTITY = "WISHLIST_ENTITY";
    String PAGINATION_MODEL = "PAGINATION_MODEL";

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

    void fetchDataFromCache(final Context context);

    void setData(GqlWishListDataResponse gqlWishListDataResponse);

    void deleteWishlist(Context context, String productId, int position);

    void addToCart(Activity activity, String productId);

    boolean isLoadedFirstPage();

    void searchWishlist(String query);

    void searchWishlistLoadMore();

    void fetchDataAfterClearSearch(Context context);

    void refreshDataOnSearch(CharSequence query);

    void onResume(Context context);
}
