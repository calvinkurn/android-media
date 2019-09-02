package com.tokopedia.tkpd.home.presenter;

import androidx.recyclerview.widget.GridLayoutManager;

import com.tokopedia.core.var.RecyclerViewItem;

import java.util.List;

/**
 * Created by m.normansyah on 01/12/2015.
 */
public interface ProductHistoryView {
    String TAG = "MNORMANSYAH";
    String messageTAG = "ProductHistoryView : ";

    /**
     * set Listener
     */
    void setListener();

    /**
     * set layout manager to recyclerview
     */
    void prepareView();

    /**
     * init recyclerview's grid layout manager - thing that responsible for create that
     */
    void initGridLayoutManager();

    /**
     * initiate item decoration
     */
    void initItemDecoration();

    /**
     * initiate {@code ProductFeedAdapter} data
     * @param data not null List<{@code RecyclerViewItem}>
     */
    void initAdapterWithData(List<RecyclerViewItem> data);

    /**
     * @return change span size according to orientation
     */
    GridLayoutManager.SpanSizeLookup onSpanSizeLookup();

    /**
     * @return calculate column size according to orientation
     */
    int calcColumnSize();


    /**
     * @return refresh flag that showed that refresh layout is swiped down or not
     */
    boolean isPullToRefresh();

    /**
     * @return loadmore flag that indicated to load new page
     */
    boolean isLoading();

    /**
     * @param isLoadMore add load more at very bottom
     */
    void displayLoadMore(boolean isLoadMore);

    /**
     * show display swipe
     * @param isShow true means show swipe, false means hide swipe
     */
    void displayPull(boolean isShow);

    /**
     * is swipe enable?
     * @param isPullEnabled true means enable swipe, false means disable swipe
     */
    void setPullEnabled(boolean isPullEnabled);

    void loadDataChange();

    void displayMainContent(boolean isShow);

    void displayLoading(boolean isShow);
}
