package com.tokopedia.tkpd.home.presenter;

import android.support.v7.widget.GridLayoutManager;

import com.tokopedia.tkpd.home.wishlist.domain.model.GqlWishListDataResponse;
import com.tokopedia.core.network.entity.wishlist.Wishlist;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.transaction.common.sharedata.AddToCartResult;

import java.util.List;

/**
 * Created by m.normansyah on 01/12/2015.
 */
public interface WishListView {
    String TAG = "MNORMANSYAH";
    String messageTAG = "WishListView : ";

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

    void setEmptyState();

    void setAdapter();

    void displayContentList(boolean isShow);

    void setSearchNotFound();

    void displayLoading(boolean isShow);

    void displayDeleteWishlistDialog(String productId, int position);

    void displayAddToCart(String productId);

    void showProgressDialog();

    void dismissProgressDialog();

    void onSuccessDeleteWishlist(String searchTerm, int position);

    void displayErrorNetwork(Boolean isAction);

    void showAddToCartMessage(String message);

    void showAddToCartErrorMessage(String message);

    String getUserId();

    void clearSearchView();

    void sendAddToCartAnalytics(Wishlist dataDetail, AddToCartResult addToCartResult);

    void sendWishlistImpressionAnalysis(GqlWishListDataResponse.GqlWishList wishListData, int currentSize);

}
