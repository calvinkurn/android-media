package com.tokopedia.checkout.view.feature.emptycart;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.recentview.RecentView;
import com.tokopedia.checkout.view.feature.emptycart.viewmodel.RecentViewViewModel;
import com.tokopedia.checkout.view.feature.emptycart.viewmodel.WishlistViewModel;
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist;

import java.util.List;

/**
 * Created by Irfan Khoirul on 14/09/18.
 */

public interface EmptyCartContract {

    interface View extends CustomerView {

        Context getContext();

        void showLoading();

        void hideLoading();

        void showErrorToast(String message);

        void renderCancelAutoApplyCouponSuccess();

        void renderEmptyCart(String autoApplyMessage);

        void renderHasWishList();

        void renderHasNoWishList();

        void renderHasRecentView();

        void renderHasNoRecentView();

        TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
                TKPDMapParam<String, String> originParams
        );

        void navigateToCartFragment(CartListData cartListData);

    }

    interface Presenter extends CustomerPresenter<View> {

        void processInitialGetCartData();

        void processGetWishlistData();

        void processGetRecentViewData(int userId);

        void processCancelAutoApply();

        void setWishListViewModels(List<Wishlist> wishLists);

        List<WishlistViewModel> getWishlistViewModels();

        void setRecentViewListModels(List<RecentView> recentViewList);

        List<RecentViewViewModel> getRecentViewListModels();

    }

}
