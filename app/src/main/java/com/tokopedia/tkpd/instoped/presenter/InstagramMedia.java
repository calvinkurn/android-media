package com.tokopedia.tkpd.instoped.presenter;

import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;

import com.tokopedia.tkpd.instoped.model.InstagramMediaModel;
import com.tokopedia.tkpd.instoped.model.InstagramUserModel;
import com.tokopedia.tkpd.var.RecyclerViewItem;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Toped18 on 5/18/2016.
 */
public interface InstagramMedia {

    String ITEM_COUNT_GETTER = "10";
    boolean ITEM_INCREMENT = true;
    boolean ITEM_DECREMENT = false;

    /**
     * initiate instance logic and view
     */
    void initIntagramMediaInstances(Context context);

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

    void updateMaxCount(int instaMediacount);

    void updateItemSelection(boolean increment, int pos);

    SparseArray<InstagramMediaModel> getSelectedModel();
    void clearSelectedModel();

    void setModel(InstagramUserModel model);

    boolean isSelected(int position);

    boolean getHasLoadMore();
}
