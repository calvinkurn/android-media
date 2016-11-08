package com.tokopedia.core.home.presenter;

import android.content.Context;

import com.tokopedia.core.home.adapter.DataFeedAdapter;
import com.tokopedia.core.home.adapter.ProductFeedAdapter;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.RecyclerViewItem;

import java.util.List;

/**
 * Created by m.normansyah on 26/10/2015.
 */
public interface ProductFeedView{
    String TAG = "MNORMANSYAH";
    /**
     * create holder instance
     */
    void initHolder();

    /**
     * initiate {@code ProductFeedAdapter} data
     * @param data not null List<{@code RecyclerViewItem}>
     */
    void initProductFeedRecyclerViewAdapter(List<RecyclerViewItem> data);

    /**
     * initiate item decoration
     */
    void initItemDecoration();

    /**
     * getter of {@link ProductFeedAdapter}
     * @return not null adapter
     */
    DataFeedAdapter getAdapter();

    /**
     * @return view context
     */
    Context getMainContext();
    /**
     * calculate Column Size of grid recyclerview
     * @return 1 column to be displayed if screen cannot be determined
     */
    int calcColumnSize();

    /**
     * set layout manager to recyclerview
     */
    void prepareView();

    /**
     * init recyclerview's grid layout manager - thing that responsible for create that
     */
    void initGridLayoutManager();

    /**
     * @return refresh flag that showed that refresh layout is swiped down or not
     */
    boolean isRefresh();

    boolean isLoading();

    /**
     * ketika selesai timeout,
     * when timeout finished, clear the view that trigger when fetch the data
     */
    void finishLoading();

    void scrolltoPositon(int index);

    int getLastPosition();

    void loadDataChange();


    /**
     * @param isLoadMore add load more at very bottom
     */
    void displayLoadMore(boolean isLoadMore);


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

    void displayLoading(boolean isLoading);

    void displayMainContent(boolean isMain);

    void displayRetry(boolean isRetry);

    void setListener();

    /**
     * display full try-again
     */
    void displayRetryFull();

    /**
     * hide full try-again
     */
    void hideRetryFull();

    void addAll(List<RecyclerViewItem> items);

    void addNextPage(List<RecyclerViewItem> items);

    boolean isAfterRotate();

    List<RecyclerViewItem> getData();

    void addTopAds(List<ProductItem> product, int page);

    int getTopAdsPagging();

    void resetPaging();
}
