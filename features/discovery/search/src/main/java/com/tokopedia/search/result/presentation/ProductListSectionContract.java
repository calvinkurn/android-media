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
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.search.analytics.GeneralSearchTrackingModel;
import com.tokopedia.search.result.domain.model.SearchProductModel;
import com.tokopedia.search.result.presentation.model.BroadMatchItemDataView;
import com.tokopedia.search.result.presentation.model.EmptySearchProductDataView;
import com.tokopedia.search.result.presentation.model.GlobalNavDataView;
import com.tokopedia.search.result.presentation.model.ProductItemDataView;
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView;
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

        void setEmptyProduct(GlobalNavDataView globalNavDataView, EmptySearchProductDataView emptySearchProductDataView);

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

        void sendImpressionGlobalNav(GlobalNavDataView globalNavDataView);

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

        void showMessageFailedWishlistAction(boolean isWishlisted);

        String getPreviousKeyword();

        boolean isLandingPage();

        void logWarning(String message, @Nullable Throwable throwable);

        void sendTopAdsGTMTrackingProductImpression(ProductItemDataView item);

        void sendTopAdsGTMTrackingProductClick(ProductItemDataView item);

        void sendGTMTrackingProductClick(ProductItemDataView item, String userId, String suggestedRelatedKeyword, String dimension90);

        void routeToProductDetail(ProductItemDataView item, int adapterPosition);

        void stopPreparePagePerformanceMonitoring();

        void startNetworkRequestPerformanceMonitoring();

        void stopNetworkRequestPerformanceMonitoring();

        void startRenderPerformanceMonitoring();

        void sendProductImpressionTrackingEvent(ProductItemDataView item, String suggestedRelatedKeyword, String dimension90);

        void trackBroadMatchImpression(BroadMatchItemDataView broadMatchItemDataView);

        void onQuickFilterSelected(Option option);

        void initFilterControllerForQuickFilter(List<Filter> quickFilterList);

        void hideQuickFilterShimmering();

        void setQuickFilter(List<SortFilterItem> items);

        void showOnBoarding(int firstProductPosition);

        boolean isQuickFilterSelected(Option option);

        void setProductCount(String productCountText);

        String getClassName();

        void sendTrackingOpenFilterPage();

        void openBottomSheetFilter(@Nullable DynamicFilterModel dynamicFilterModel);

        void setDynamicFilter(@NotNull DynamicFilterModel dynamicFilterModel);

        void trackEventClickBroadMatchItem(BroadMatchItemDataView broadMatchItemDataView);

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

        boolean isChooseAddressWidgetEnabled();

        LocalCacheModel getChooseAddressData();

        boolean getIsLocalizingAddressHasUpdated(LocalCacheModel currentChooseAddressData);

        void refreshItemAtIndex(int index);

        void trackInspirationCarouselChipsClicked(@NotNull InspirationCarouselDataView.Option option);
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

        void onProductImpressed(ProductItemDataView item, int adapterPosition);

        void onProductClick(ProductItemDataView item, int adapterPosition);

        List<Option> getQuickFilterOptionList();

        void getProductCount(Map<String, String> mapParameter);

        void openFilterPage(Map<String, Object> searchParameter);

        boolean isBottomSheetFilterEnabled();

        void onBottomSheetFilterDismissed();

        void onBroadMatchItemImpressed(@NotNull BroadMatchItemDataView broadMatchItemDataView);

        void onBroadMatchItemClick(@NotNull BroadMatchItemDataView broadMatchItemDataView);

        void onThreeDotsClick(ProductItemDataView item, int adapterPosition);

        void handleAddToCartAction(@NotNull ProductCardOptionsModel productCardOptionModel);

        void handleVisitShopAction();

        void handleChangeView(int position, SearchConstant.ViewType currentLayoutType);

        void onViewResumed();

        void onLocalizingAddressSelected();

        void onInspirationCarouselChipsClick(
                int adapterPosition,
                InspirationCarouselDataView inspirationCarouselViewModel,
                InspirationCarouselDataView.Option clickedInspirationCarouselOption,
                Map<String, Object> searchParameter
        );
    }
}
