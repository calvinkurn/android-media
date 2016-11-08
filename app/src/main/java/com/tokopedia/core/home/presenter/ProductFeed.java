package com.tokopedia.core.home.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.var.RecyclerViewItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by m.normansyah on 26/10/2015.
 */
public interface ProductFeed {
    String TAG = "MNORMANSYAH";
    String messageTag = "ProductFeed : ";
    String SWIPE_SHOW = "SWIPE_SHOW";
    String POSITION_VIEW = "position_view";
    String MODEL_FLAG = "MODEL_FLAG";
    String LIST_OF_SHOP_ID = "LIST_OF_SHOP_ID";
    public String QUERY = "query";
    public String PER_PAGE = "per_page";
    public String PAGE = "page";
    public String DEFAULT_QUERY = "";
    public String DEFAULT_PER_PAGE = 6 + "";
    int defaultPositionView = 1;

    int LAST_SEEN_TYPE = 0;
    int PRODUCT_FEED_TYPE = 1;

    int NETWORK_HANDLER_SIZE = 2;
    int NUMBER_CACHE = 2;
    /**
     * initiate instance both logic and view
     */
    void initProductFeedInstances(Context context);
    void initProductFeedDatas();

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

    List<RecyclerViewItem> getData();

    /**
     * fetch recent 4 last seen product from the internet
     */
    void getRecentProduct();

    /**
     * fetch recent 4 last seen product from the internet
     */
    void getRecentProductFromCache();

    /**
     * fetch product feed from the internet
     */
    void getProductFeed();

    /**
     * set data to adapter, lebih baik jadi loadData
     */
    void setData();

    void setDataLoadMore();

    /**
     * get 8 product feed data and 4 recent last seen data
     */
    void refreshData();

    /**
     * when user scroll to the bottom of reccylerview than load next 8
     * product feed data
     */
    void loadMore();

    /**
     * move to {@link com.tokopedia.core.AddProduct} activity
     * @param context
     */
    void moveToAddProduct(Context context);

    /**
     * send Data to Localitycs
     * @param context
     * @param screenName
     */
    void setLocalyticFlow(Context context, String screenName);

    /**
     * send Data to Appsflyer
     * @param context
     */
    void sendAppsFlyerData(Context context);

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

    void saveCache(int type, JSONObject jsonToSave)  throws JSONException;

    List<RecyclerViewItem> parseJSON(int type,JSONObject jsonToParse) throws JSONException;

    void subscribe();
    void unsubscribe();

    void fecthRecentProductFromNetwork();
}
