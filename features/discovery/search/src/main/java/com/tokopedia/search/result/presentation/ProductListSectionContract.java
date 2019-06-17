package com.tokopedia.search.result.presentation;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.discovery.common.data.DataValue;
import com.tokopedia.discovery.common.data.Filter;
import com.tokopedia.discovery.common.data.Option;
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ProductListSectionContract {

    interface View extends SearchSectionContract.View {
        boolean isUserHasLogin();

        String getUserId();

        void initTopAdsParams();

        void incrementStart();

        boolean isEvenPage();

        void storeTotalData(int totalData);

        int getStartFrom();

        void setHeaderTopAds(boolean hasHeader);

        void addProductList(List<Visitable> list);

        void setProductList(List<Visitable> list);

        void disableWishlistButton(String productId);

        void enableWishlistButton(String productId);

        void showNetworkError(int startRow);

        String getQueryKey();

        void setEmptyProduct();

        Map<String, Object> getSearchParameterMap();

        void backToTop();

        List<Option> getQuickFilterOptions(DataValue dynamicFilterModel);

        void addLoading();

        void removeLoading();

        void onSuccessAddWishlist(String productId);

        void onErrorAddWishList(String errorMessage, String productId);

        void notifyAdapter();

        void stopTracePerformanceMonitoring();

        void initQuickFilter(List<Filter> quickFilterList);

        void setAdditionalParams(String additionalParams);

        void sendTrackingEventAppsFlyerViewListingSearch(JSONArray afProdIds, String query, ArrayList<String> prodIdArray);

        void sendTrackingEventMoEngageSearchAttempt(String query, boolean hasProductList, HashMap<String, String> category);

        void setFirstTimeLoad(boolean isFirstTimeLoad);

        void sendImpressionGlobalNav(GlobalNavViewModel globalNavViewModel);

        void clearLastProductItemPositionFromCache();

        void saveLastProductItemPositionToCache(int lastProductItemPositionToCache);

        int getLastProductItemPositionFromCache();

        void updateScrollListener();

        boolean isAnyFilterActive();

        Map<String, String> getAdditionalParamsMap();

        void launchLoginActivity(String productId);

        void sendImpressionGuidedSearch();
    }

    interface Presenter extends SearchSectionContract.Presenter<View> {
        void setWishlistActionListener(WishListActionListener wishlistActionListener);

        void loadMoreData(Map<String, Object> searchParameter, Map<String, String> additionalParams);

        void loadData(Map<String, Object> searchParameter, Map<String, String> additionalParams, boolean isFirstTimeLoad);

        void handleWishlistButtonClicked(final ProductItemViewModel productItem);
    }
}
