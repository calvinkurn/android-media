package com.tokopedia.feedplus.view.listener;

import android.app.Activity;
import android.content.res.Resources;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailHeaderViewModel;
import com.tokopedia.feedplus.view.viewmodel.feeddetail.FeedDetailViewModel;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import java.util.ArrayList;

/**
 * @author by nisie on 5/18/17.
 */

public interface FeedPlusDetail {

    interface View extends CustomerView {

        Activity getActivity();

        String getString(int resId);

        Resources getResources();

        int getColor(int resId);

        void onWishlistClicked(int adapterPosition, Integer productId, boolean wishlist);

        void onGoToShopDetail(Integer shopId);

        void onErrorGetFeedDetail(String errorMessage);

        void onSuccessGetFeedDetail(FeedDetailHeaderViewModel header,
                                    ArrayList<Visitable> listDetail,
                                    boolean hasNextPage);

        void showLoading();

        void dismissLoading();

        void showLoadingMore();

        void dismissLoadingMore();

        void showLoadingProgress();

        void onGoToProductDetail(FeedDetailViewModel feedDetailViewModel, int adapterPosition);

        void onEmptyFeedDetail();

        void onBackPressed();

        void setHasNextPage(boolean hasNextPage);
    }

    interface Presenter extends CustomerPresenter<View> {
        void attachView(FeedPlusDetail.View view, WishListActionListener wishlistListener);

        void getFeedDetail(String detailId, int page);

        void addToWishlist(int adapterPosition, String productId);

        void removeFromWishlist(int adapterPosition, String productId);
    }
}
