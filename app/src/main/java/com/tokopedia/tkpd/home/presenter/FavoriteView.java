package com.tokopedia.tkpd.home.presenter;

import android.content.res.Configuration;
import android.os.Bundle;

import com.tokopedia.tkpd.customadapter.BaseRecyclerViewAdapter;
import com.tokopedia.tkpd.var.RecyclerViewItem;

import java.util.List;

/**
 * Created by mnormansyah on 31/10/2015.
 * modified by m.normansyah on 24/11/2015
 */
public interface FavoriteView {
    String TAG = "MNORMANSYAH";
    // why this is saved in here -
    // to show other developer what value needed for this class
    int LANDSCAPE = Configuration.ORIENTATION_LANDSCAPE;
    int PORTRAIT = Configuration.ORIENTATION_PORTRAIT;
    int COLUMN_SIZE = 2;
    int DURATION_ANIMATOR = 1000;

    /**
     * STEP 1
     * create holder instance, conjunction with {@link com.tokopedia.tkpd.app.V2BaseFragment}
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
    void initLayoutManager();

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
     * @param bundle non null bundle, should be initiate
     * @param clazz class to move, show be based on Activity
     */
    void moveToOtherActivity(Bundle bundle, Class<?> clazz);

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
     * set retry at very bottom, clicked
     */
    void setOnRetryListenerRV();

    /**
     * is at landscape mode
     * @return true means landscape otherwise false means portrait
     */
    boolean isLandscape();

    /**
     * get screen rotation
     * @return LANDSCAPE or PORTRAIT
     */
    int getScreenRotation();

    /**
     * display progressbar
     * @param isDisplay true means display progressbar
     *                  false means hide progressbar
     */
    void displayProgressBar(boolean isDisplay);

    /**
     * display main content
     * @param isDisplay true means display main content,
     *                  false means hide main content
     */
    void displayMainContent(boolean isDisplay);

    /**
     * scroll to last position
     * @param index last position
     */
    void scrollTo(int index);

    /**
     * @return last see position
     */
    int getLastPosition();

    /**
     * display full try-again
     */
    void displayRetryFull();

    /**
     * hide full try-again
     */
    void hideRetryFull();

    /**
     * set adapter again to update data
     */
    void setAdapter();
}
