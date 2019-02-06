package com.tokopedia.recentview.view.listener;

import android.content.Context;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by nisie on 7/4/17.
 */

public interface RecentView {
    interface View extends CustomerView {
        void onWishlistClicked(int adapterPosition, Integer productId, boolean wishlist);

        void onGoToProductDetail(String productId, String productname, String  productPrice, String productImage);

        void showLoading();

        void showLoadingProgress();

        void dismissLoadingProgress();

        void onErrorGetRecentView(String errorMessage);

        void onSuccessGetRecentView(ArrayList<Visitable> listProduct);

        void onEmptyGetRecentView();

        void sendRecentViewImpressionTracking(List<RecentViewDetailProductViewModel> recentViewModel);

        void sendRecentViewClickTracking(RecentViewDetailProductViewModel element);


        Context getContext();
    }

    interface Presenter extends CustomerPresenter<View> {

        void getRecentViewProduct();

        void addToWishlist(int adapterPosition, String productId);

        void removeFromWishlist(int adapterPosition, String productId);
    }
}