package com.tokopedia.core.home.presenter;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.tokopedia.core.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.var.RecyclerViewItem;

import java.util.List;

/**
 * Created by m.normansyah on 28/10/2015.
 */
public interface HotListView extends BaseView {
    String TAG = "MNORMANSYAH";
    // why this is saved in here -
    // to show other developer what value needed for this class
    int LANDSCAPE = Configuration.ORIENTATION_LANDSCAPE;
    int PORTRAIT = Configuration.ORIENTATION_PORTRAIT;
    int COLUMN_SIZE = 2;

    /**
     * STEP 1
     * create holder instance, conjunction with {@link com.tokopedia.core.app.V2BaseFragment}
     */
    void initHolder();

    /**
     * STEP 1
     * 1. init adapter
     * 2. throw empty arraylist if no data
     * @param data not null data
     */
    void initAdapter(List<RecyclerViewItem> data);

    /**
     * STEP 1
     * init linearlayout
     */
    void initLinLayManager();

    /**
     * 1. init layout manager
     * 2. set adapter
     */
    void prepareView();

    /**
     * is swipe enable?
     * @param isPullEnabled true means enable swipe, false means disable swipe
     */
    void setPullEnabled(boolean isPullEnabled);

    /**
     * show display swipe
     * @param isShow true means show swipe, false means hide swipe
     */
    void displayPull(boolean isShow);

    /**
     * ATOMIC METHOD
     * @return is swipe shown ? true means show, false means hide
     */
    boolean isSwipeShow();

    /**
     * notify data has changed to adapter
     */
    void loadDataChange();

    /**
     * @param isLoadMore add load more at very bottom
     */
    void displayLoadMore(boolean isLoadMore);

    /**
     * @return true if loading at very bottom show, false if loading at very bottom hide
     */
    boolean isLoadMoreShow();

    /**
     * some clicked trigger to move to other activity
     * @param bundle
     */
    void moveToOtherActivity(Bundle bundle);

    void openHotlistActivity(String url);

    void openCategory(String categoryUrl);

    /**
     * @return current used adapter
     */
    BaseRecyclerViewAdapter getAdapter();

    /**
     * @param isRetry true means show retry at bottom recyclerview
     */
    void displayRetry(boolean isRetry);

    /**
     * show timeout dialog
     */
    void displayTimeout();

    /**
     * notify adapter to add retry
     * @param isRetry true means add retry, false means remove retry at very bottom
     */
    void setRetry(boolean isRetry);

    /**
     * set retry at very bottom, clicked
     */
    void setOnRetryListenerRV();

    /**
     * is at landscape mode
     * @return
     */
    boolean isLandscape();

    /**
     * get screen rotation
     * @return
     */
    int getScreenRotation();
    /**
     * some clicked trigger to move to other activity
     */
    void startIntentActivity(Intent intent);

    void openSearch(String url);
}
