package com.tokopedia.tokomart.search.presentation.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.utils.UrlParamUtils
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheet.ApplySortFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.getSortFilterParamsString
import com.tokopedia.minicart.common.analytics.MiniCartAnalytics
import com.tokopedia.searchbar.data.HintData
import com.tokopedia.tokomart.search.di.SearchComponent
import com.tokopedia.tokomart.search.presentation.listener.SuggestionListener
import com.tokopedia.tokomart.search.presentation.model.SuggestionDataView
import com.tokopedia.tokomart.search.presentation.typefactory.SearchTypeFactoryImpl
import com.tokopedia.tokomart.search.presentation.viewmodel.SearchViewModel
import com.tokopedia.tokomart.search.utils.SearchTracking
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.view.BaseSearchCategoryFragment
import com.tokopedia.tokomart.searchcategory.utils.TOKONOW
import javax.inject.Inject

class SearchFragment: BaseSearchCategoryFragment(), SuggestionListener {

    companion object {

        @JvmStatic
        fun create(): SearchFragment {
            return SearchFragment()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var searchViewModel: SearchViewModel

    override val toolbarPageName = "TokoNow Search"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
    }

    private fun initViewModel() {
        activity?.let {
            searchViewModel = ViewModelProvider(it, viewModelFactory).get(SearchViewModel::class.java)
        }
    }

    override fun getNavToolbarHint() =
            listOf(HintData(searchViewModel.query, searchViewModel.query))

    override fun getBaseAutoCompleteApplink() =
            super.getBaseAutoCompleteApplink() + "?" +
                    "${SearchApiConst.Q}=${searchViewModel.query}" + "&" +
                    "${SearchApiConst.NAVSOURCE}=$TOKONOW"

    override fun getScreenName() = ""

    override fun initInjector() {
        getComponent(SearchComponent::class.java).inject(this)
    }

    override fun observeViewModel() {
        super.observeViewModel()

        getViewModel().generalSearchEventLiveData.observe(this::sendTrackingGeneralEvent)
        getViewModel().addToCartTrackingLiveData.observe(this::sendAddToCartTrackingEvent)
        getViewModel().increaseQtyTrackingLiveData.observe(this::sendIncreaseQtyTrackingEvent)
        getViewModel().decreaseQtyTrackingLiveData.observe(this::sendDecreaseQtyTrackingEvent)
    }

    private fun sendTrackingGeneralEvent(dataLayer: Map<String, Any>) {
        SearchTracking.sendGeneralEvent(dataLayer)
    }

    override fun createTypeFactory() = SearchTypeFactoryImpl(
            chooseAddressListener = this,
            titleListener = this,
            bannerListener = this,
            quickFilterListener = this,
            categoryFilterListener = this,
            productItemListener = this,
            emptyProductListener = this,
            suggestionListener = this,
    )

    override val miniCartWidgetPageName: MiniCartAnalytics.Page
        get() = MiniCartAnalytics.Page.SEARCH_PAGE

    override fun getViewModel() = searchViewModel

    override fun onSuggestionClicked(suggestionDataView: SuggestionDataView) {
        val context = context ?: return

        SearchTracking.sendSuggestionClickEvent(getViewModel().query, suggestionDataView.suggestion)

        val applink = ApplinkConstInternalTokopediaNow.SEARCH + "?" + suggestionDataView.query
        RouteManager.route(context, applink)
    }

    override fun onGoToGlobalSearch() {
        super.onGoToGlobalSearch()

        val queryParams = "${SearchApiConst.Q}=${searchViewModel.query}"
        val applinkToSearchResult = "${ApplinkConstInternalDiscovery.SEARCH_RESULT}?$queryParams"

        RouteManager.route(context, applinkToSearchResult)
    }

    override fun onProductImpressed(productItemDataView: ProductItemDataView) {
        val trackingQueue = trackingQueue ?: return

        SearchTracking.sendProductImpressionEvent(
                trackingQueue,
                listOf(getProductItemAsImpressionClickObjectDataLayer(productItemDataView)),
                getViewModel().query,
                getUserId(),
        )
    }

    private fun getUserId(): String {
        val userId = userSession.userId ?: ""

        return if (userId.isEmpty()) "0" else userId
    }

    private fun getProductItemAsImpressionClickObjectDataLayer(
            productItemDataView: ProductItemDataView
    ): Any {
        val queryParam = searchViewModel.queryParam
        val pageId = queryParam[SearchApiConst.SRP_PAGE_ID] ?: ""
        val sortFilterParams = getSortFilterParamsString(queryParam as Map<String?, Any?>)

        return productItemDataView.getAsImpressionClickObjectDataLayer(sortFilterParams, pageId)
    }

    override fun onProductClick(productItemDataView: ProductItemDataView) {
        SearchTracking.sendProductClickEvent(
                getProductItemAsImpressionClickObjectDataLayer(productItemDataView),
                getViewModel().query,
                getUserId(),
        )

        super.onProductClick(productItemDataView)
    }

    override fun openFilterPage() {
        SearchTracking.sendOpenFilterPageEvent()

        super.openFilterPage()
    }

    override fun onApplySortFilter(applySortFilterModel: ApplySortFilterModel) {
        val paramMap = applySortFilterModel.selectedFilterMapParameter as Map<String?, String>
        val filterParams = UrlParamUtils.generateUrlParamString(paramMap)
        SearchTracking.sendApplySortFilterEvent(filterParams)

        super.onApplySortFilter(applySortFilterModel)
    }

    override fun onCategoryFilterChipClick(option: Option, isSelected: Boolean) {
        SearchTracking.sendApplyCategoryL2FilterEvent(option.name)

        super.onCategoryFilterChipClick(option, isSelected)
    }

    private fun sendAddToCartTrackingEvent(atcData: Pair<Int, ProductItemDataView>) {
        val quantity = atcData.first
        val productItemDataView = atcData.second

        val queryParam = searchViewModel.queryParam
        val pageId = queryParam[SearchApiConst.SRP_PAGE_ID] ?: ""
        val sortFilterParams = getSortFilterParamsString(queryParam as Map<String?, Any?>)

        val atcDataLayer = productItemDataView.getAsATCObjectDataLayer(sortFilterParams, pageId, quantity)

        SearchTracking.sendAddToCartEvent(atcDataLayer, getViewModel().query, getUserId())
    }

    private fun sendIncreaseQtyTrackingEvent(productId: String) {
        SearchTracking.sendIncreaseQtyEvent(searchViewModel.query, productId)
    }

    private fun sendDecreaseQtyTrackingEvent(productId: String) {
        SearchTracking.sendDecreaseQtyEvent(searchViewModel.query, productId)
    }
}