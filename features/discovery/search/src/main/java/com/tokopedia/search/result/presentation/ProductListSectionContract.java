package com.tokopedia.search.result.presentation;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.discovery.common.model.ProductCardOptionsModel;
import com.tokopedia.discovery.common.model.WishlistTrackingModel;
import com.tokopedia.filter.common.data.DynamicFilterModel;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.search.analytics.GeneralSearchTrackingModel;
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;
import com.tokopedia.search.result.presentation.model.InspirationCarouselViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ProductListSectionContract {

    interface View extends CustomerView {
        String getUserId();

        void addProductList(List<Visitable> list);

        void setProductList(List<Visitable> list);

        void addRecommendationList(List<Visitable> list);

        void showNetworkError(int startRow);

        String getQueryKey();

        void setEmptyProduct(GlobalNavViewModel globalNavViewModel);

        void setBannedProductsErrorMessage(List<Visitable> bannedProductsErrorMessageAsList);

        void trackEventImpressionBannedProducts(boolean isEmptySearch);

        void trackEventImpressionSortPriceMinTicker();

        void backToTop();

        void addLoading();

        void removeLoading();

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

        void redirectSearchToAnotherPage(String applink);

        void sendTrackingForNoResult(String resultCode, String alternativeKeyword, String keywordProcess);

        void setDefaultLayoutType(int defaultView);

        void showFreeOngkirShowCase(boolean hasFreeOngkirBadge);

        void redirectToBrowser(String url);

        HashMap<String, String> getSelectedSort();

        void setSelectedSort(HashMap<String, String> selectedSort);

        HashMap<String, String> getSelectedFilter();

        void refreshFilterController(HashMap<String, String> selectedFilter);

        void showRefreshLayout();

        void hideRefreshLayout();

        String getScreenNameId();

        void setTotalSearchResultCount(String formattedResultCount);

        BaseAppComponent getBaseAppComponent();

        void renderDynamicFilter(DynamicFilterModel dynamicFilterModel);

        void renderFailRequestDynamicFilter();

        boolean isFirstActiveTab();

        void setupSearchNavigation();

        void trackScreenAuthenticated();

        void reloadData();

        void showBottomNavigation();

        void hideBottomNavigation();

        void sendImpressionInspirationCarousel(final InspirationCarouselViewModel inspirationCarouselViewModel);

        RemoteConfig getABTestRemoteConfig();

        void trackWishlistRecommendationProductLoginUser(boolean isAddWishlist);

        void trackWishlistRecommendationProductNonLoginUser();

        void trackWishlistProduct(WishlistTrackingModel wishlistTrackingModel);

        void updateWishlistStatus(String productId, boolean isWishlisted);

        void showMessageSuccessWishlistAction(boolean isWishlisted);

        void showMessageFailedWishlistAction(boolean isWishlisited);

        String getPreviousKeyword();

        boolean isLandingPage();
    }

    interface Presenter extends CustomerPresenter<View> {

        void requestDynamicFilter(Map<String, Object> searchParameter);

        void loadMoreData(Map<String, Object> searchParameter);

        void loadData(Map<String, Object> searchParameter);

        void onBannedProductsGoToBrowserClick(String url);

        boolean isUsingBottomSheetFilter();

        String getUserId();

        boolean isUserLoggedIn();

        String getDeviceId();

        void onPriceFilterTickerDismissed();

        boolean getIsTickerHasDismissed();

        boolean hasNextPage();

        void clearData();

        void setStartFrom(int startFrom);

        int getStartFrom();

        void onViewCreated();

        void onViewVisibilityChanged(boolean isViewVisible, boolean isViewAdded);

        void handleWishlistAction(ProductCardOptionsModel productCardOptionsModel);
    }
}
