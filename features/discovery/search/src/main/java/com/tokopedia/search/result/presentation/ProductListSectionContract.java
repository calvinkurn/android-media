package com.tokopedia.search.result.presentation;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.discovery.common.data.DataValue;
import com.tokopedia.discovery.common.data.Filter;
import com.tokopedia.discovery.common.data.Option;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface ProductListSectionContract {

    interface View extends SearchSectionContract.View {

        void launchLoginActivity(Bundle extras);

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

        SearchParameter getSearchParameter();

        void setSearchParameter(SearchParameter searchParameter);

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
    }

    interface Presenter extends SearchSectionContract.Presenter<View> {

        void attachView(View viewListener, WishListActionListener wishlistActionListener);

        void loadMoreData(SearchParameter searchParameter, HashMap<String, String> additionalParams);

        void loadData(SearchParameter searchParameter, boolean isForceSearch, HashMap<String, String> additionalParams, boolean isFirstTimeLoad);

        void handleWishlistButtonClicked(final ProductItemViewModel productItem);

        void setIsUsingFilterV4(boolean isUsingFilterV4);
    }
}
