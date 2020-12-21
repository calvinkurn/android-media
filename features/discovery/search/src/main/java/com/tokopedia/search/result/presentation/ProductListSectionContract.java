package com.tokopedia.search.result.presentation;

import androidx.annotation.Nullable;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.discovery.common.constants.SearchConstant;
import com.tokopedia.discovery.common.model.ProductCardOptionsModel;
import com.tokopedia.discovery.common.model.WishlistTrackingModel;
import com.tokopedia.filter.common.data.DynamicFilterModel;
import com.tokopedia.filter.common.data.Filter;
import com.tokopedia.filter.common.data.Option;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.search.analytics.GeneralSearchTrackingModel;
import com.tokopedia.search.result.presentation.model.BroadMatchItemViewModel;
import com.tokopedia.search.result.presentation.model.EmptySearchProductViewModel;
import com.tokopedia.search.result.presentation.model.GlobalNavViewModel;
import com.tokopedia.search.result.presentation.model.ProductItemViewModel;
import com.tokopedia.sortfilter.SortFilterItem;

import org.jetbrains.annotations.NotNull;
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

        void setEmptyProduct(GlobalNavViewModel globalNavViewModel, EmptySearchProductViewModel emptySearchProductViewModel);

        void setBannedProductsErrorMessage(List<Visitable> bannedProductsErrorMessageAsList);

        void trackEventImpressionBannedProducts(boolean isEmptySearch);

        void trackEventImpressionTicker(int typeId);

        void backToTop();

        void addLoading();

        void removeLoading();

        void stopTracePerformanceMonitoring();

        void setAutocompleteApplink(String autocompleteApplink);

        void sendTrackingEventAppsFlyerViewListingSearch(JSONArray afProdIds, String query, ArrayList<String> prodIdArray);

        void sendTrackingEventMoEngageSearchAttempt(String query, boolean hasProductList, HashMap<String, String> category);

        void sendTrackingGTMEventSearchAttempt(GeneralSearchTrackingModel generalSearchTrackingModel);

        void sendImpressionGlobalNav(GlobalNavViewModel globalNavViewModel);

        boolean isAnySortActive();

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

        void showRefreshLayout();

        void hideRefreshLayout();

        String getScreenNameId();

        boolean isFirstActiveTab();

        void setupSearchNavigation();

        void trackScreenAuthenticated();

        void reloadData();

        RemoteConfig getABTestRemoteConfig();

        void trackWishlistRecommendationProductLoginUser(boolean isAddWishlist);

        void trackWishlistRecommendationProductNonLoginUser();

        void trackWishlistProduct(WishlistTrackingModel wishlistTrackingModel);

        void updateWishlistStatus(String productId, boolean isWishlisted);

        void showMessageSuccessWishlistAction(boolean isWishlisted);

        void showMessageFailedWishlistAction(boolean isWishlisited);

        String getPreviousKeyword();

        boolean isLandingPage();

        void logWarning(String message, @Nullable Throwable throwable);

        void sendTopAdsGTMTrackingProductImpression(ProductItemViewModel item);

        void sendTopAdsGTMTrackingProductClick(ProductItemViewModel item);

        void sendGTMTrackingProductClick(ProductItemViewModel item, String userId);

        void routeToProductDetail(ProductItemViewModel item, int adapterPosition);

        void stopPreparePagePerformanceMonitoring();

        void startNetworkRequestPerformanceMonitoring();

        void stopNetworkRequestPerformanceMonitoring();

        void startRenderPerformanceMonitoring();

        void sendProductImpressionTrackingEvent(ProductItemViewModel item);

        void trackBroadMatchImpression(BroadMatchItemViewModel broadMatchItemViewModel);

        void onQuickFilterSelected(Option option);

        void initFilterControllerForQuickFilter(List<Filter> quickFilterList);

        void hideQuickFilterShimmering();

        void setQuickFilter(List<SortFilterItem> items);

        void showOnBoarding(int firstProductPosition, boolean showThreeDotsOnBoarding);

        boolean isQuickFilterSelected(Option option);

        void setProductCount(String productCountText);

        String getClassName();

        void sendTrackingOpenFilterPage();

        void openBottomSheetFilter(@Nullable DynamicFilterModel dynamicFilterModel);

        void setDynamicFilter(@NotNull DynamicFilterModel dynamicFilterModel);

        void trackEventClickBroadMatchItem(BroadMatchItemViewModel broadMatchItemViewModel);

        void redirectionStartActivity(String applink, String url);

        void trackEventLongPress(String productID);

        void showProductCardOptions(ProductCardOptionsModel productCardOptionsModel);

        void trackSuccessAddToCartEvent(boolean isAds, Object addToCartDataLayer);

        void showAddToCartSuccessMessage();

        void showAddToCartFailedMessage(String errorMessage);

        void routeToShopPage(String shopId);

        void trackEventGoToShopPage(Object dataLayer);

        void addLocalSearchRecommendation(List<Visitable> visitableList);

        void trackEventSearchResultChangeView(String viewType);

        void switchSearchNavigationLayoutTypeToListView(int position);

        void switchSearchNavigationLayoutTypeToBigGridView(int position);

        void switchSearchNavigationLayoutTypeToSmallGridView(int position);
    }

    interface Presenter extends CustomerPresenter<View> {

        void loadMoreData(Map<String, Object> searchParameter);

        void loadData(Map<String, Object> searchParameter);

        String getUserId();

        boolean isUserLoggedIn();

        String getDeviceId();

        void onPriceFilterTickerDismissed();

        boolean getIsTickerHasDismissed();

        boolean hasNextPage();

        void clearData();

        int getStartFrom();

        void onViewCreated();

        void onViewVisibilityChanged(boolean isViewVisible, boolean isViewAdded);

        void handleWishlistAction(ProductCardOptionsModel productCardOptionsModel);

        void onProductImpressed(ProductItemViewModel item);

        void onProductClick(ProductItemViewModel item, int adapterPosition);

        List<Option> getQuickFilterOptionList();

        void getProductCount(Map<String, String> mapParameter);

        void onFreeOngkirOnBoardingShown();

        void openFilterPage(Map<String, Object> searchParameter);

        void onBroadMatchItemImpressed(@NotNull BroadMatchItemViewModel broadMatchItemViewModel);

        void onBroadMatchItemClick(@NotNull BroadMatchItemViewModel broadMatchItemViewModel);

        void onThreeDotsClick(ProductItemViewModel item, int adapterPosition);

        void handleAddToCartAction(@NotNull ProductCardOptionsModel productCardOptionModel);

        void handleVisitShopAction();

        void handleChangeView(int position, SearchConstant.ViewType currentLayoutType);
    }
}
