package com.tokopedia.checkout.view.feature.emptycart;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.checkout.domain.datamodel.cartlist.AutoApplyData;
import com.tokopedia.checkout.domain.datamodel.cartlist.CartListData;
import com.tokopedia.checkout.domain.datamodel.recentview.RecentView;
import com.tokopedia.checkout.view.feature.emptycart.viewmodel.RecentViewViewModel;
import com.tokopedia.checkout.view.feature.emptycart.viewmodel.WishlistViewModel;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist;

import java.util.List;
import java.util.Map;

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

        void renderEmptyCart(AutoApplyData autoApplyMessage);

        void renderHasWishList(boolean hasMoreItem);

        void renderHasNoWishList();

        void renderHasRecentView(boolean hasMoreItem);

        void renderHasNoRecentView();

        TKPDMapParam<String, String> getGeneratedAuthParamNetwork(
                TKPDMapParam<String, String> originParams
        );

        void navigateToCartFragment(CartListData cartListData);

        void stopCartTrace();

        boolean isCartTraceStopped();

        void stopAllTrace();

        boolean isAllTraceStopped();

    }

    interface Presenter extends CustomerPresenter<View> {

        void processInitialGetCartData();

        void processGetWishlistData();

        void processGetRecentViewData(int userId);

        void processCancelAutoApply();

        void setLoadApiStatus(@EmptyCartApi int key, boolean status);

        boolean hasLoadAllApi();

        void setWishListViewModels(List<Wishlist> wishLists);

        List<WishlistViewModel> getWishlistViewModels();

        void setRecentViewListModels(List<RecentView> recentViewList);

        List<RecentViewViewModel> getRecentViewListModels();

        void setRecommendationList(List<Item> list);

        Map<String, Object> generateEmptyCartAnalyticProductClickDataLayer(Wishlist wishlist, int index);

        Map<String, Object> generateEmptyCartAnalyticProductClickDataLayer(RecentView recentView, int index);

        Map<String, Object> generateEmptyCartAnalyticProductClickDataLayer(Product product, int index);

        Map<String, Object> generateEmptyCartAnalyticViewProductWishlistDataLayer();

        Map<String, Object> generateEmptyCartAnalyticViewProductRecentViewDataLayer();

        Map<String, Object> generateEmptyCartAnalyticViewProductRecommendationDataLayer();

    }

}
