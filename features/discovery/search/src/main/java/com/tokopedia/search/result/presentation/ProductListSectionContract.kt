package com.tokopedia.search.result.presentation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.search.analytics.GeneralSearchTrackingModel
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.DynamicFilterModelProvider
import com.tokopedia.search.result.product.broadmatch.BroadMatchPresenter
import com.tokopedia.search.result.product.cpm.BannerAdsPresenter
import com.tokopedia.search.result.product.filter.bottomsheetfilter.BottomSheetFilterPresenter
import com.tokopedia.search.result.product.grid.ProductGridType
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselPresenter
import com.tokopedia.search.result.product.pagination.Pagination
import com.tokopedia.search.result.product.safesearch.SafeSearchPresenter
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlesskeywordoptions.InspirationKeywordPresenter
import com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct.InspirationProductPresenter
import com.tokopedia.search.result.product.ticker.TickerPresenter
import com.tokopedia.search.result.product.wishlist.WishlistPresenter
import com.tokopedia.sortfilter.SortFilterItem
import org.json.JSONArray
import com.tokopedia.filter.quick.SortFilterItem as SortFilterItemReimagine

interface ProductListSectionContract {
    interface View : CustomerView {
        fun addProductList(list: List<Visitable<*>>)
        fun setProductList(list: List<Visitable<*>>)
        fun showNetworkError(throwable: Throwable?)
        val queryKey: String
        fun backToTop()
        fun immediateMoveToTop()
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
        fun showAdultRestriction()
        fun redirectSearchToAnotherPage(applink: String?)
        fun setDefaultLayoutType(defaultView: Int)
        fun setProductGridType(productGridType: ProductGridType)
        fun showRefreshLayout()
        fun hideRefreshLayout()
        val isFirstActiveTab: Boolean
        fun trackScreenAuthenticated()
        fun reloadData()
        val abTestRemoteConfig: RemoteConfig?
        val previousKeyword: String
        val isLandingPage: Boolean
        fun logWarning(message: String?, throwable: Throwable?)
        fun sendTopAdsGTMTrackingProductImpression(item: ProductItemDataView)
        fun sendTopAdsGTMTrackingProductClick(item: ProductItemDataView)
        fun sendGTMTrackingProductClick(item: ProductItemDataView, userId: String, suggestedRelatedKeyword: String)
        fun sendByteIOTrackingProductClick(item: ProductItemDataView)
        fun routeToProductDetail(item: ProductItemDataView?, adapterPosition: Int)
        fun sendProductImpressionTrackingEvent(item: ProductItemDataView, suggestedRelatedKeyword: String)
        fun openAddToCartToaster(message: String, isSuccess: Boolean)
        fun openVariantBottomSheet(data: ProductItemDataView)
        fun sendGTMTrackingProductATC(productItemDataView: ProductItemDataView?, cartId: String?)
        fun onQuickFilterSelected(filter: Filter, option: Option, pageSource: String, position: Int)
        fun initFilterController(quickFilterList: List<Filter>)
        fun setAutoFilterToggle(autoFilterParameter: String)
        fun setSortFilterIndicatorCounter()
        fun hideQuickFilterShimmering()
        fun setQuickFilter(items: List<SortFilterItem>)
        fun setQuickFilterReimagine(items: List<SortFilterItemReimagine>)
        fun showOnBoarding(firstProductPosition: Int)
        fun enableProductViewTypeOnBoarding()
        fun isFilterSelected(option: Option?): Boolean
        val className: String
        fun redirectionStartActivity(applink: String?, url: String?)
        fun trackEventLongPress(productID: String)
        fun trackEventThreeDotsClickByteIO(productItemDataView: ProductItemDataView)
        fun showProductCardOptions(productCardOptionsModel: ProductCardOptionsModel)
        fun addLocalSearchRecommendation(visitableList: List<Visitable<*>>)
        fun refreshItemAtIndex(index: Int)
        fun openBottomsheetMultipleOptionsQuickFilter(filter: Filter, position: Int)
        fun applyDropdownQuickFilter(optionList: List<Option>?)
        fun trackEventApplyDropdownQuickFilter(optionList: List<Option>?, pageSource: String)
        fun updateSearchBarNotification()
        fun isDarkMode(): Boolean
        fun sendTrackingByteIO(isSuccess: Boolean)
        fun cleanByteIOData()
    }

    interface Presenter :
        CustomerPresenter<View>,
        Pagination,
        BannerAdsPresenter,
        DynamicFilterModelProvider,
        BroadMatchPresenter,
        TickerPresenter,
        SafeSearchPresenter,
        WishlistPresenter,
        BottomSheetFilterPresenter,
        InspirationCarouselPresenter,
        InspirationKeywordPresenter,
        InspirationProductPresenter {

        fun loadMoreData(searchParameter: Map<String, Any>)
        fun loadData(searchParameter: Map<String, Any>)
        val searchId: String
        val pageComponentId: String
        val userId: String
        val isUserLoggedIn: Boolean
        fun onViewCreated()
        fun onViewVisibilityChanged(isViewVisible: Boolean, isViewAdded: Boolean)
        fun onProductImpressed(item: ProductItemDataView?, adapterPosition: Int)
        fun onProductClick(item: ProductItemDataView?, adapterPosition: Int)
        fun trackProductClick(item: ProductItemDataView)
        fun onProductAddToCart(item: ProductItemDataView)
        val quickFilterList: List<Filter>
        val threeDotsProductItem: ProductItemDataView?
        fun onThreeDotsClick(item: ProductItemDataView, adapterPosition: Int)
        fun onViewResumed()
        fun onLocalizingAddressSelected()
        fun onApplyDropdownQuickFilter(optionList: List<Option>?)
        fun getIsLocalSearch(): Boolean
    }
}
