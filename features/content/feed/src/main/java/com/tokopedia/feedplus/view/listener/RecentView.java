package com.tokopedia.feedplus.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

import java.util.ArrayList;

/**
 * @author by nisie on 7/4/17.
 */

public interface RecentView {
    public interface View extends CustomerView {
        void onWishlistClicked(int adapterPosition, Integer productId, boolean wishlist);

        void onGoToProductDetail(String productId);

        void showLoading();

        void showLoadingProgress();

        void dismissLoadingProgress();

        void onErrorGetRecentView(String errorMessage);

        void onSuccessGetRecentView(ArrayList<Visitable> listProduct);

        void onEmptyGetRecentView();

        Context getContext();
    }

    public interface Presenter extends CustomerPresenter<View> {

        void getRecentViewProduct();

        void addToWishlist(int adapterPosition, String productId);

        void removeFromWishlist(int adapterPosition, String productId);
    }
}