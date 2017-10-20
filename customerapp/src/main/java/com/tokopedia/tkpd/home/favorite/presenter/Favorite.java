package com.tokopedia.tkpd.home.favorite.presenter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.ShopItem;

import org.json.JSONObject;

import java.util.List;

/**
 * @author m.normansyah
 * @since 30/10/2015.
 * @version 2
 */
public interface Favorite  {
    String TAG = "MNORMANSYAH";
    String messageTAG = "Favorite : ";

    public String QUERY = "query";
    public String DEFAULT_QUERY = "";
    public String DEFAULT_PER_PAGE_KEY = 20 + "";
    public String OPTION_LOCATION = "option_location";
    public String OPTION_NAME = "option_name";
    public String DEFAULT_OPTION_NAME = "";
    public String DEFAULT_OPTION_LOCATION = "";
    public String DEFAULT_PER_PAGE = 20 + "";

    int FAVORITE_DATA_TYPE = 0;
    int WISHLIST_DATA_TYPE = 1;
    int TOPADS_DATA_TYPE = 2;
    int FAVORITE_SEND_DATA_TYPE = 3;
    int FETCHER_SIZE = 4;

    int DATA_FAVORITE_SIZE = -1;
    int DATA_WISHLIST_SIZE = 4;
    int DATA_TOPADS_SIZE = 3;

    String GET_FAV_SHOP = "get_fav_shop";
    String GET_WISHLIST = "get_wishlist";
    String GET_TOP_ADS_SHOP = "ad_shop_feed";
    String POST_FAV_SHOP = "do_fav_shop";

    String ACT_KEY = "act";
    String PAGE_KEY = "page";
    String PER_PAGE_KEY = "per_page";
    String IS_AD_KEY = "is_ad";
    String AD_KEY = "ad_key";
    String SHOP_ID_KEY = "shop_id";

    String POSITION_VIEW = "position_view";
    String DATA = "DATA";
    int defaultPositionView = 1;
    int shopIdKeyIndex = 0;
    int shopIdValueIndex = 1;
    int FAVORITE_START = 2;
    int WISHLIST_START = 0;
    int TOP_ADS_START = 1;

    // top ads parsing constant string
    String TOP_ADS_AD_SHOP_KEY = "list";
    String TOP_ADS_AD_SHOP_VALUE = "0";

    // wish list parsing constant string
    String WISHLIST_DATALIST_KEY = "list";

    // favorite parsing constant string
    String FAVORITE_SHOPS_KEY = "list";
    String FAVORITE_ISFAVORITE_VALUE = "1";

    /**
     * initiate instance both logic and view
     */
    void initInstances(Context context);

    /**
     * send AppsFlyer data
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
    void fetchListData(int listDataType);

    /**
     * have some POST to the internet
     * @param listDataType
     * @param objects
     */
    void sendListData(int listDataType, ShopItem shopItem, Object... objects);

    /**
     * parse json
     * @param Result
     * @return valid data
     */
    List<RecyclerViewItem> parseJSON(JSONObject Result, int dataType);

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
    void moveToOtherActivity(RecyclerViewItem data, Class<?> clazz, Object... moreDatas);

    /**
     * cache Page One
     * @param jsonObject
     * @param dataType
     */
    void cachePageOne(JSONObject jsonObject, int dataType);

    /**
     * if exceed one day, get hot list for page 1
     * @return true get page one from the cache - false get page one from the internet
     */
    boolean isCacheOneDay();

    /**
     * set timout policy and listener
     */
    void setTimeOut();

    /**
     * set retry policy and listener so that some logic can be done
     */
    void setRetryListener();

    /**
     * from top ads to favorite
     * @param view
     * @param item
     */
    void onRecommendShopClicked(View view, ShopItem item);

    /**
     * @return is hasNext Paging true means have next page, false means don't have next page
     */
    boolean isMorePage();

    /**
     * scroll to last position
     */
    void scrollToPosition();

    void sendAllClickEventGTM();

    void subscribe();

    void unSubscribe();

    void onDestroy();
}
