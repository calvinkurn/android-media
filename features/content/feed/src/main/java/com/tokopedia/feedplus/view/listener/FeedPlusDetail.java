package com.tokopedia.feedplus.view.listener;

import android.app.Activity;
import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.common.utils.paging.PagingHandler;
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderViewModel;
import com.tokopedia.feedplus.view.viewmodel.feeddetail.SingleFeedDetailViewModel;

import java.util.ArrayList;

/**
 * @author by nisie on 5/18/17.
 */

public interface FeedPlusDetail {

    interface View extends CustomerView {

        Activity getActivity();

        String getString(int resId);

        PagingHandler getPagingHandler();

        Resources getResources();

        int getColor(int resId);

        void onWishlistClicked(int adapterPosition, Integer productId, boolean wishlist);

        void onGoToShopDetail(Integer shopId);

        void onErrorGetFeedDetail(String errorMessage);

        void onSuccessGetFeedDetail(FeedDetailHeaderViewModel header,
                                    ArrayList<Visitable> listDetail,
                                    boolean hasNextPage);

        void showLoading();

        void showLoadingProgress();

        void onGoToProductDetail(String productId, boolean wishlist, int adapterPosition);

        void onEmptyFeedDetail();

        void onBackPressed();

        void onGoToBuyProduct(String productId, String price, String imageSource);

        void onSuccessGetSingleFeedDetail(FeedDetailHeaderViewModel headerViewModel,
                                          SingleFeedDetailViewModel feedDetailViewModel);
    }

    interface Presenter extends CustomerPresenter<View> {
        void attachView(FeedPlusDetail.View view, WishlistListener wishlistListener);

        void getFeedDetail(String detailId, int page);

        void addToWishlist(int adapterPosition, String productId);

        void removeFromWishlist(int adapterPosition, String productId);
    }
}
