package com.tokopedia.search.result.presentation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.search.analytics.GeneralSearchTrackingModel
import com.tokopedia.search.result.presentation.model.*
import com.tokopedia.sortfilter.SortFilterItem
import org.json.JSONArray
import java.util.*

interface ProductListSectionContract {
    interface View : CustomerView {
        fun addProductList(list: List<Visitable<*>>)
        fun setProductList(list: List<Visitable<*>>)
        fun addRecommendationList(list: List<Visitable<*>>)
        fun showNetworkError(startRow: Int)
        val queryKey: String
        fun setEmptyProduct(globalNavDataView: GlobalNavDataView?, emptySearchProductDataView: EmptySearchProductDataView)
        fun setBannedProductsErrorMessage(bannedProductsErrorMessageAsList: List<Visitable<*>>)
        fun trackEventImpressionBannedProducts(isEmptySearch: Boolean)
        fun trackEventImpressionTicker(typeId: Int)
        fun backToTop()
        fun addLoading()
        fun removeLoading()
        fun stopTracePerformanceMonitoring()
        fun setAutocompleteApplink(autocompleteApplink: String?)
        fun sendTrackingEventAppsFlyerViewListingSearch(afProdIds: JSONArray?, query: String?, prodIdArray: ArrayList<String?>?)
        fun sendTrackingEventMoEngageSearchAttempt(query: String?, hasProductList: Boolean, category: HashMap<String?, String?>?)
        fun sendTrackingGTMEventSearchAttempt(generalSearchTrackingModel: GeneralSearchTrackingModel)
        fun sendImpressionGlobalNav(globalNavDataView: GlobalNavDataView)
        val isAnySortActive: Boolean
        fun clearLastProductItemPositionFromCache()
        fun saveLastProductItemPositionToCache(lastProductItemPositionToCache: Int)
        val lastProductItemPositionFromCache: Int
        fun updateScrollListener()
        val isAnyFilterActive: Boolean
        fun launchLoginActivity(productId: String?)
        fun showAdultRestriction()
        fun redirectSearchToAnotherPage(applink: String?)
        fun sendTrackingForNoResult(resultCode: String?, alternativeKeyword: String?, keywordProcess: String?)
        fun setDefaultLayoutType(defaultView: Int)
        fun showRefreshLayout()
        fun hideRefreshLayout()
        val isFirstActiveTab: Boolean
        fun setupSearchNavigation()
        fun trackScreenAuthenticated()
        fun reloadData()
        val abTestRemoteConfig: RemoteConfig?
        fun trackWishlistRecommendationProductLoginUser(isAddWishlist: Boolean)
        fun trackWishlistRecommendationProductNonLoginUser()
        fun trackWishlistProduct(wishlistTrackingModel: WishlistTrackingModel)
        fun updateWishlistStatus(productId: String?, isWishlisted: Boolean)
        fun showMessageSuccessWishlistAction(isWishlisted: Boolean)
        fun showMessageFailedWishlistAction(isWishlisted: Boolean)
        val previousKeyword: String
        val isLandingPage: Boolean
        fun logWarning(message: String?, throwable: Throwable?)
        fun sendTopAdsGTMTrackingProductImpression(item: ProductItemDataView)
        fun sendTopAdsGTMTrackingProductClick(item: ProductItemDataView)
        fun sendGTMTrackingProductClick(item: ProductItemDataView, userId: String, suggestedRelatedKeyword: String, dimension90: String)
        fun routeToProductDetail(item: ProductItemDataView?, adapterPosition: Int)
        fun stopPreparePagePerformanceMonitoring()
        fun startNetworkRequestPerformanceMonitoring()
        fun stopNetworkRequestPerformanceMonitoring()
        fun startRenderPerformanceMonitoring()
        fun sendProductImpressionTrackingEvent(item: ProductItemDataView, suggestedRelatedKeyword: String, dimension90: String)
        fun trackBroadMatchImpression(broadMatchItemDataView: BroadMatchItemDataView)
        fun onQuickFilterSelected(option: Option)
        fun initFilterControllerForQuickFilter(quickFilterList: List<Filter>)
        fun hideQuickFilterShimmering()
        fun setQuickFilter(items: List<SortFilterItem>)
        fun showOnBoarding(firstProductPosition: Int)
        fun isQuickFilterSelected(option: Option?): Boolean
        fun setProductCount(productCountText: String?)
        val className: String
        fun sendTrackingOpenFilterPage()
        fun openBottomSheetFilter(dynamicFilterModel: DynamicFilterModel?)
        fun setDynamicFilter(dynamicFilterModel: DynamicFilterModel)
        fun trackEventClickBroadMatchItem(broadMatchItemDataView: BroadMatchItemDataView)
        fun redirectionStartActivity(applink: String?, url: String?)
        fun trackEventLongPress(productID: String)
        fun showProductCardOptions(productCardOptionsModel: ProductCardOptionsModel)
        fun trackSuccessAddToCartEvent(isAds: Boolean, addToCartDataLayer: Any)
        fun showAddToCartSuccessMessage()
        fun showAddToCartFailedMessage(errorMessage: String)
        fun routeToShopPage(shopId: String?)
        fun trackEventGoToShopPage(dataLayer: Any)
        fun addLocalSearchRecommendation(visitableList: List<Visitable<*>>)
        fun trackEventSearchResultChangeView(viewType: String)
        fun switchSearchNavigationLayoutTypeToListView(position: Int)
        fun switchSearchNavigationLayoutTypeToBigGridView(position: Int)
        fun switchSearchNavigationLayoutTypeToSmallGridView(position: Int)
        val isChooseAddressWidgetEnabled: Boolean
        val chooseAddressData: LocalCacheModel?
        fun getIsLocalizingAddressHasUpdated(currentChooseAddressData: LocalCacheModel): Boolean
        fun refreshItemAtIndex(index: Int)
        fun trackInspirationCarouselChipsClicked(option: InspirationCarouselDataView.Option)
        fun trackDynamicProductCarouselImpression(dynamicProductCarousel: BroadMatchItemDataView, type: String)
        fun trackDynamicProductCarouselClick(dynamicProductCarousel: BroadMatchItemDataView, type: String)
    }

    interface Presenter : CustomerPresenter<View> {
        fun loadMoreData(searchParameter: Map<String, Any>)
        fun loadData(searchParameter: Map<String, Any>)
        val userId: String
        val isUserLoggedIn: Boolean
        val deviceId: String
        fun onPriceFilterTickerDismissed()
        val isTickerHasDismissed: Boolean
        fun hasNextPage(): Boolean
        fun clearData()
        val startFrom: Int
        fun onViewCreated()
        fun onViewVisibilityChanged(isViewVisible: Boolean, isViewAdded: Boolean)
        fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel?)
        fun onProductImpressed(item: ProductItemDataView?, adapterPosition: Int)
        fun onProductClick(item: ProductItemDataView?, adapterPosition: Int)
        val quickFilterOptionList: List<Option>
        fun getProductCount(mapParameter: Map<String, String>?)
        fun openFilterPage(searchParameter: Map<String, Any>?)
        val isBottomSheetFilterEnabled: Boolean
        fun onBottomSheetFilterDismissed()
        fun onBroadMatchItemImpressed(broadMatchItemDataView: BroadMatchItemDataView)
        fun onBroadMatchItemClick(broadMatchItemDataView: BroadMatchItemDataView)
        fun onThreeDotsClick(item: ProductItemDataView, adapterPosition: Int)
        fun handleAddToCartAction(productCardOptionModel: ProductCardOptionsModel)
        fun handleVisitShopAction()
        fun handleChangeView(position: Int, currentLayoutType: SearchConstant.ViewType)
        fun onViewResumed()
        fun onLocalizingAddressSelected()
        fun onInspirationCarouselChipsClick(
                adapterPosition: Int,
                inspirationCarouselViewModel: InspirationCarouselDataView,
                clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
                searchParameter: Map<String, Any>
        )
    }
}