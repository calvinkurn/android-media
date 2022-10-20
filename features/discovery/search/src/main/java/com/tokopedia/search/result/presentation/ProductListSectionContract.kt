package com.tokopedia.search.result.presentation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.search.analytics.GeneralSearchTrackingModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.broadmatch.BroadMatchPresenter
import com.tokopedia.search.result.product.cpm.BannerAdsPresenter
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.pagination.Pagination
import com.tokopedia.search.result.product.safesearch.SafeSearchPresenter
import com.tokopedia.search.result.product.ticker.TickerPresenter
import com.tokopedia.sortfilter.SortFilterItem
import org.json.JSONArray

interface ProductListSectionContract {
    interface View : CustomerView {
        fun addProductList(list: List<Visitable<*>>)
        fun setProductList(list: List<Visitable<*>>)
        fun addRecommendationList(list: List<Visitable<*>>)
        fun showNetworkError(throwable: Throwable?)
        val queryKey: String
        fun backToTop()
        fun addLoading()
        fun removeLoading()
        fun setAutocompleteApplink(autocompleteApplink: String?)
        fun sendTrackingEventAppsFlyerViewListingSearch(afProdIds: JSONArray?, query: String?, prodIdArray: ArrayList<String?>?, allProdIdArray: ArrayList<String?>? = null)
        fun sendTrackingEventMoEngageSearchAttempt(query: String?, hasProductList: Boolean, category: HashMap<String?, String?>?)
        fun sendTrackingGTMEventSearchAttempt(generalSearchTrackingModel: GeneralSearchTrackingModel)
        val isAnySortActive: Boolean
        fun clearLastProductItemPositionFromCache()
        fun saveLastProductItemPositionToCache(lastProductItemPositionToCache: Int)
        val lastProductItemPositionFromCache: Int
        fun updateScrollListener()
        val isAnyFilterActive: Boolean
        fun launchLoginActivity(productId: String?)
        fun showAdultRestriction()
        fun redirectSearchToAnotherPage(applink: String?)
        fun setDefaultLayoutType(defaultView: Int)
        fun showRefreshLayout()
        fun hideRefreshLayout()
        val isFirstActiveTab: Boolean
        fun trackScreenAuthenticated()
        fun reloadData()
        val abTestRemoteConfig: RemoteConfig?
        fun trackWishlistRecommendationProductLoginUser(isAddWishlist: Boolean)
        fun trackWishlistRecommendationProductNonLoginUser()
        fun trackWishlistProduct(wishlistTrackingModel: WishlistTrackingModel)
        fun updateWishlistStatus(productId: String?, isWishlisted: Boolean)
        fun hitWishlistClickUrl(productCardOptionsModel: ProductCardOptionsModel)
        fun showMessageSuccessWishlistAction(wishlistResult: ProductCardOptionsModel.WishlistResult)
        fun showMessageFailedWishlistAction(wishlistResult: ProductCardOptionsModel.WishlistResult)
        val previousKeyword: String
        val isLandingPage: Boolean
        fun logWarning(message: String?, throwable: Throwable?)
        fun sendTopAdsGTMTrackingProductImpression(item: ProductItemDataView)
        fun sendTopAdsGTMTrackingProductClick(item: ProductItemDataView)
        fun sendGTMTrackingProductClick(item: ProductItemDataView, userId: String, suggestedRelatedKeyword: String)
        fun routeToProductDetail(item: ProductItemDataView?, adapterPosition: Int)
        fun sendProductImpressionTrackingEvent(item: ProductItemDataView, suggestedRelatedKeyword: String)
        fun onQuickFilterSelected(filter: Filter, option: Option)
        fun initFilterController(quickFilterList: List<Filter>)
        fun hideQuickFilterShimmering()
        fun setQuickFilter(items: List<SortFilterItem>)
        fun showOnBoarding(firstProductPosition: Int)
        fun isFilterSelected(option: Option?): Boolean
        fun setProductCount(productCountText: String?)
        val className: String
        fun sendTrackingOpenFilterPage()
        fun openBottomSheetFilter(dynamicFilterModel: DynamicFilterModel?)
        fun setDynamicFilter(dynamicFilterModel: DynamicFilterModel)
        fun redirectionStartActivity(applink: String?, url: String?)
        fun trackEventLongPress(productID: String)
        fun showProductCardOptions(productCardOptionsModel: ProductCardOptionsModel)
        fun addLocalSearchRecommendation(visitableList: List<Visitable<*>>)
        fun refreshItemAtIndex(index: Int)
        fun trackInspirationCarouselChipsClicked(option: InspirationCarouselDataView.Option)
        fun trackEventImpressionInspirationCarouselGridItem(product: InspirationCarouselDataView.Option.Product)
        fun trackEventImpressionInspirationCarouselListItem(product: InspirationCarouselDataView.Option.Product)
        fun trackEventImpressionInspirationCarouselChipsItem(product: InspirationCarouselDataView.Option.Product)
        fun trackEventClickInspirationCarouselGridItem(product: InspirationCarouselDataView.Option.Product)
        fun trackEventClickInspirationCarouselListItem(product: InspirationCarouselDataView.Option.Product)
        fun trackEventClickInspirationCarouselChipsItem(product: InspirationCarouselDataView.Option.Product)
        fun openBottomsheetMultipleOptionsQuickFilter(filter: Filter)
        fun applyDropdownQuickFilter(optionList: List<Option>?)
        fun trackEventClickDropdownQuickFilter(filterTitle: String)
        fun trackEventApplyDropdownQuickFilter(optionList: List<Option>?)
    }

    interface Presenter :
        CustomerPresenter<View>,
        Pagination,
        BannerAdsPresenter,
        BroadMatchPresenter,
        TickerPresenter,
        SafeSearchPresenter {

        fun loadMoreData(searchParameter: Map<String, Any>)
        fun loadData(searchParameter: Map<String, Any>)
        val pageComponentId: String
        val userId: String
        val isUserLoggedIn: Boolean
        fun onViewCreated()
        fun onViewVisibilityChanged(isViewVisible: Boolean, isViewAdded: Boolean)
        fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel?)
        fun onProductImpressed(item: ProductItemDataView?, adapterPosition: Int)
        fun onProductClick(item: ProductItemDataView?, adapterPosition: Int)
        val quickFilterList: List<Filter>
        fun getProductCount(mapParameter: Map<String, String>?)
        fun openFilterPage(searchParameter: Map<String, Any>?)
        val isBottomSheetFilterEnabled: Boolean
        fun onBottomSheetFilterDismissed()
        fun onApplySortFilter(mapParameter: Map<String, Any>)
        fun onInspirationCarouselProductImpressed(product: InspirationCarouselDataView.Option.Product)
        fun onInspirationCarouselProductClick(product: InspirationCarouselDataView.Option.Product)
        fun onThreeDotsClick(item: ProductItemDataView, adapterPosition: Int)
        fun onViewResumed()
        fun onLocalizingAddressSelected()
        fun onInspirationCarouselChipsClick(
            adapterPosition: Int,
            inspirationCarouselViewModel: InspirationCarouselDataView,
            clickedInspirationCarouselOption: InspirationCarouselDataView.Option,
            searchParameter: Map<String, Any>
        )
        fun onApplyDropdownQuickFilter(optionList: List<Option>?)
    }
}
