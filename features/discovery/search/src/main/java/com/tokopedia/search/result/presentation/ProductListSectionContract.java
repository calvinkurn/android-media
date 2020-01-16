package com.tokopedia.search.result.presentation;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.filter.common.data.DataValue;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.search.analytics.GeneralSearchTrackingModel;
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
        String getUserId();

        void incrementStart();

        void storeTotalData(int totalData);

        void setHeaderTopAds(boolean hasHeader);

        void addProductList(List<Visitable> list);

        void setProductList(List<Visitable> list);

        void addRecommendationList(List<Visitable> list);

        void disableWishlistButton(String productId);

        void enableWishlistButton(String productId);

        void showNetworkError(int startRow);

        String getQueryKey();

        void setEmptyProduct(GlobalNavViewModel globalNavViewModel);

        void setBannedProductsErrorMessage(List<Visitable> bannedProductsErrorMessageAsList);

        void trackEventImpressionBannedProducts(boolean isEmptySearch);

        void trackEventImpressionSortPriceMinTicker();

        void backToTop();

        void addLoading();

        void removeLoading();

        void successAddWishlist(ProductItemViewModel productItemViewModel);

        void errorAddWishList(String errorMessage, String productId);

        void successRemoveWishlist(ProductItemViewModel productItemViewModel);

        void errorRemoveWishlist(String errorMessage, String productId);

        void notifyAdapter();

        void stopTracePerformanceMonitoring();

        void initQuickFilter(List<Filter> quickFilterList);

        void setAutocompleteApplink(String autocompleteApplink);

        void sendTrackingEventAppsFlyerViewListingSearch(JSONArray afProdIds, String query, ArrayList<String> prodIdArray);

        void sendTrackingEventMoEngageSearchAttempt(String query, boolean hasProductList, HashMap<String, String> category);

        void sendTrackingGTMEventSearchAttempt(GeneralSearchTrackingModel generalSearchTrackingModel);

        void sendImpressionGlobalNav(GlobalNavViewModel globalNavViewModel);

        void clearLastProductItemPositionFromCache();

        void saveLastProductItemPositionToCache(int lastProductItemPositionToCache);

        int getLastProductItemPositionFromCache();

        void updateScrollListener();

        boolean isAnyFilterActive();

        void launchLoginActivity(String productId);

        void showAdultRestriction();

        void sendTrackingWishlistNonLogin(ProductItemViewModel productItemViewModel);

        void redirectSearchToAnotherPage(String applink);

        void sendTrackingForNoResult(String resultCode, String alternativeKeyword, String keywordProcess);

        void setDefaultLayoutType(int defaultView);

        void successRemoveRecommendationWishlist(String productId);

        void successAddRecommendationWishlist(String productId);

        void errorRecommendationWishlist(String errorMessage, String productId);

        void showFreeOngkirShowCase(boolean hasFreeOngkirBadge);

        boolean isTickerHasDismissed();

        void redirectToBrowser(String url);
    }

    interface Presenter extends SearchSectionContract.Presenter<View> {

        void loadMoreData(Map<String, Object> searchParameter);

        void loadData(Map<String, Object> searchParameter);

        void handleWishlistButtonClicked(final ProductItemViewModel productItem);

        void handleWishlistButtonClicked(final RecommendationItem recommendationItem);

        void onBannedProductsGoToBrowserClick(String url);

        boolean isUsingBottomSheetFilter();

        String getUserId();

        boolean isUserLoggedIn();

        void setIsFirstTimeLoad(boolean isFirstTimeLoad);

        void setIsTickerHasDismissed(boolean isTickerHasDismissed);

        boolean getIsTickerHasDismissed();
    }
}
