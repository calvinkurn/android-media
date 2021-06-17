package com.tokopedia.tokomart.search.presentation.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.applink.internal.ApplinkConstInternalTokopediaNow
import com.tokopedia.discovery.common.constants.SearchApiConst
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
import com.tokopedia.user.session.UserSessionInterface
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

        val applink = ApplinkConstInternalTokopediaNow.SEARCH + "?" +
                suggestionDataView.query

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
                listOf(getProductItemAsObjectDataLayer(productItemDataView)),
                getViewModel().query,
                userSession.userId,
        )
    }

    private fun getProductItemAsObjectDataLayer(productItemDataView: ProductItemDataView): Any {
        val queryParam = searchViewModel.queryParam
        val pageId = queryParam[SearchApiConst.SRP_PAGE_ID] ?: ""
        val sortFilterParams = getSortFilterParamsString(queryParam as Map<String?, Any?>)

        return productItemDataView.getAsObjectDataLayer(sortFilterParams, pageId)
    }

    override fun onProductClick(productItemDataView: ProductItemDataView) {
        SearchTracking.sendProductClickEvent(
                getProductItemAsObjectDataLayer(productItemDataView),
                getViewModel().query,
                userSession.userId,
        )

        super.onProductClick(productItemDataView)
    }
}