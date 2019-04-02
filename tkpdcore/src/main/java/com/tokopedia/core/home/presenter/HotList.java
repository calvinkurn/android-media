package com.tokopedia.core.home.presenter;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.core.var.RecyclerViewItem;

import org.json.JSONObject;

import java.util.List;

/**
 * @author m.normansyah
 * @since 28/10/2015.
 * @version 2
 */
public interface HotList {
    String TAG = "MNORMANSYAH";
    String messageTAG = "HotList : ";
    String CACHE_KEY = "HOTLIST_CACHE";

    String ACT_KEY = "act";
    String PAGE_KEY = "page";
    String QUERY_KEY = "query";
    String PER_PAGE_KEY = "per_page";

    String ACT_VALUE = "get_hot_list";
    int PER_PAGE_VALUE = 30;
    String POSITION_VIEW = "position_view";
    String VIEW_ = "VIEW_";
    int defaultPositionView = 1;
    String DATA = "HOTLIST_DATA";

    String LIST_KEY = "list";
    String HOT_KEY = "hot";
    String ALIAS_KEY ="alias";
    String TITLE_KEY = "title";
    String IMG_URL_KEY = "image_url";
    String IMG_URL_600_KEY = "image_url_600";
    String PRICE_START_FROM = "price_start";
    String URL_KEY = "url";
    String CATALOG_KEY = "catalog";
    String TOPPICKS_KEY = "toppicks";
    String CATEGORY = "p";
    String SEARCH = "search";
    String CATALOG_ID_KEY = "ctg_id";
    String DEPID_ID_KEY = "d_id";
    String JSON_QUERY_KEY = "json_query";
    String STATE_KEY = "state";
    int STATE_VALUE = 3;

    // last seen page 1
    String HOT_LIST_PAGE_1 = "hot_list_page_1";
    String EXPIRY = "expiry";
    int timeoutTime = 10000;
    int numberOftry = 10;

    /**
     * initiate instance both logic and view
     */
    void initHotListInstances(Context context);

    /**
     * send AppsFlyer analytics
     * @param context
     */
    void sendAppsFlyerData(Context context);

    /**
     * initiate first time data
     */
    void initData();

    /**
     * when user reach very bottom,
     * load next page data
     */
    void loadMore();

    /**
     * construct network fetcher
     */
    void fetchHotListData();

    /**
     * parse json
     * @param Result
     * @return valid data
     */
    @Deprecated
    List<RecyclerViewItem> parseJSON(JSONObject Result);

    /**
     * when swiep to refresh occured,
     * then reset all the data to page one
     */
    void resetToPageOne();

    /**
     * save some data before rotate
     * @param outState
     */
    void onSaveDataBeforeRotate(Bundle outState);

    /**
     * get some data after rotate
     * @param outState
     */
    void onFetchDataAfterRotate(Bundle outState);

    /**
     * check whether fragment is recreate after rotation.
     * @return
     */
    boolean isAfterRotate();

    /**
     * move to other activity based on data, move usually
     * after get Clicked
     * @param data not null data
     */
    void moveToOtherActivity(RecyclerViewItem data);

    /**
     * set retry policy and listener so that some logic can be done
     */
    void setRetryListener();

    void setData(List<RecyclerViewItem> items, boolean hasNext, int nextPage);

    void ariseRetry();

    void onMessageError(String text);

    void onNetworkError(String text);

    void updateViewData(int type, Object... data);

    void initDataAfterRotate();

    int getDataSize();

    int getItemDataType(int position);

    void subscribe();

    void unSubscribe();
}
