package com.tokopedia.search.result.product.visitable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.presentation.model.ProductDataView
import com.tokopedia.search.result.presentation.model.SearchProductTitleDataView
import com.tokopedia.search.result.presentation.model.TickerDataView
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.banner.BannerPresenterDelegate
import com.tokopedia.search.result.product.broadmatch.BroadMatchPresenterDelegate
import com.tokopedia.search.result.product.cpm.CpmDataView
import com.tokopedia.search.result.product.emptystate.EmptyStateDataView
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselPresenterDelegate
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetPresenterDelegate
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetVisitable
import com.tokopedia.search.result.product.lastfilter.LastFilterDataView
import com.tokopedia.search.result.product.pagination.Pagination
import com.tokopedia.search.result.product.performancemonitoring.*
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_HEADLINE_ADS
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_INSPIRATION_CAROUSEL
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_INSPIRATION_WIDGET
import com.tokopedia.search.result.product.performancemonitoring.runCustomMetric
import com.tokopedia.search.result.product.searchintokopedia.SearchInTokopediaDataView
import com.tokopedia.search.result.product.separator.VerticalSeparator
import com.tokopedia.search.result.product.suggestion.SuggestionPresenter
import com.tokopedia.search.result.product.tdn.TopAdsImageViewPresenterDelegate
import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.utils.TopAdsHeadlineHelper
import timber.log.Timber
import javax.inject.Inject

@SearchScope
class VisitableFactory @Inject constructor(
    private val suggestionPresenter: SuggestionPresenter,
    performanceMonitoringProvider: PerformanceMonitoringProvider,
    private val topAdsHeadlineHelper : TopAdsHeadlineHelper,
    private val inspirationCarouselPresenter: InspirationCarouselPresenterDelegate,
    private val inspirationWidgetPresenter: InspirationWidgetPresenterDelegate,
    private val bannerDelegate: BannerPresenterDelegate,
    private val broadMatchDelegate: BroadMatchPresenterDelegate,
    private val topAdsImageViewPresenterDelegate: TopAdsImageViewPresenterDelegate,
    private val pagination: Pagination
) {

    private var isGlobalNavWidgetAvailable = false
    private var isShowHeadlineAdsBasedOnGlobalNav = false
    private val performanceMonitoring: PageLoadTimePerformanceInterface? =
        performanceMonitoringProvider.get()

    fun createFirstPageVisitableList(data: VisitableFactoryFirstPageData): List<Visitable<*>> {
        val visitableList = mutableListOf<Visitable<*>>()
        val productDataView = data.productDataView

        addPageTitle(visitableList, data.pageTitle)
        addGlobalNavWidget(
            visitableList,
            productDataView.globalNavDataView,
            data.isGlobalNavWidgetAvailable,
        )
        addLastFilter(visitableList, productDataView.lastFilterDataView)
        addChooseAddress(visitableList)
        addTicker(visitableList, productDataView.tickerModel, data.isTickerHasDismissed)
        addSuggestion(visitableList, data.responseCode)
        addProductList(visitableList, data.productList)
        runCustomMetric(performanceMonitoring, SEARCH_RESULT_PLT_RENDER_LOGIC_HEADLINE_ADS) {
            processHeadlineAdsFirstPage(
                data.searchProductModel,
                visitableList,
                data.isLocalSearch,
                data.productList,
            )
        }
        runCustomMetric(performanceMonitoring, SEARCH_RESULT_PLT_RENDER_LOGIC_INSPIRATION_CAROUSEL) {
            addInspirationCarousel(
                productDataView.inspirationCarouselDataView,
                visitableList,
                data.productList,
                data.externalReference
            )
        }
        runCustomMetric(performanceMonitoring, SEARCH_RESULT_PLT_RENDER_LOGIC_INSPIRATION_WIDGET) {
            addInspirationWidget(
                productDataView.inspirationWidgetDataView,
                visitableList,
                data.productList,
            )
        }
        processBannerAndBroadMatchInSamePosition(visitableList, data.productList, data.responseCode)
        addBanner(visitableList, data.productList)
        runCustomMetric(performanceMonitoring, SEARCH_RESULT_PLT_RENDER_LOGIC_BROADMATCH) {
            addBroadMatch(visitableList, data.productList, data.responseCode)
        }
        runCustomMetric(performanceMonitoring, SEARCH_RESULT_PLT_RENDER_LOGIC_TDN) {
            topAdsImageViewPresenterDelegate.setTopAdsImageViewModelList(
                data.searchProductModel.getTopAdsImageViewModelList()
            )
            processTopAdsImageViewModel(visitableList, data.productList)
        }
        addSearchInTokopedia(visitableList, data.isLocalSearch, data.globalSearchApplink)

        return visitableList
    }

    private fun addPageTitle(visitableList: MutableList<Visitable<*>>, pageTitle: String) {
        if (pageTitle.isEmpty()) return

        visitableList.add(SearchProductTitleDataView(pageTitle, isRecommendationTitle = false))
    }

    private fun addGlobalNavWidget(
        visitableList: MutableList<Visitable<*>>,
        globalNavDataView: GlobalNavDataView?,
        isGlobalNavWidgetAvailable: Boolean,
    ) {
        this.isGlobalNavWidgetAvailable = isGlobalNavWidgetAvailable

        if (isGlobalNavWidgetAvailable) {
            globalNavDataView?.let {
                visitableList.add(it)
                isShowHeadlineAdsBasedOnGlobalNav = it.isShowTopAds
            }
        }
    }

    private fun addLastFilter(
        visitableList: MutableList<Visitable<*>>,
        lastFilterDataView: LastFilterDataView,
    ) {
        if (lastFilterDataView.shouldShow()) {
            visitableList.add(lastFilterDataView)
        }
    }

    private fun addChooseAddress(visitableList: MutableList<Visitable<*>>) {
        visitableList.add(ChooseAddressDataView())
    }

    private fun addTicker(
        visitableList: MutableList<Visitable<*>>,
        tickerDataView: TickerDataView?,
        isTickerHasDismissed: Boolean,
    ) {
        tickerDataView?.let {
            if (!isTickerHasDismissed && it.text.isNotEmpty())
                visitableList.add(it)
        }
    }

    private fun addSuggestion(visitableList: MutableList<Visitable<*>>, responseCode: String) {
        suggestionPresenter.processSuggestion(responseCode) {
            visitableList.add(it)
        }
    }

    private fun addProductList(
        visitableList: MutableList<Visitable<*>>,
        productList: List<Visitable<*>>,
    ) {
        visitableList.addAll(productList)
    }

    private fun processHeadlineAdsFirstPage(
        searchProductModel: SearchProductModel,
        list: MutableList<Visitable<*>>,
        isLocalSearch: Boolean,
        productList: List<Visitable<*>>,
    ) {
        if (!isHeadlineAdsAllowed(isLocalSearch)) return
        topAdsHeadlineHelper.processHeadlineAds(searchProductModel.cpmModel, 1) { index, cpmDataList,  isUseSeparator ->
            val verticalSeparator = if(isUseSeparator && index != 0)
                VerticalSeparator.Both
            else VerticalSeparator.None
            val cpmDataView = createCpmDataView(
                searchProductModel.cpmModel,
                cpmDataList,
                verticalSeparator
            )

            if (index == 0)
                processHeadlineAdsAtTop(list, cpmDataView, productList)
            else
                processHeadlineAdsAtPosition(list, productList.size, cpmDataView, productList)
        }
    }

    private fun processHeadlineAdsAtTop(
        visitableList: MutableList<Visitable<*>>,
        cpmDataView: CpmDataView,
        productList: List<Visitable<*>>,
    ) {
        if (productList.isEmpty()) return

        val firstProductIndex = visitableList.indexOf(productList[0])
        if (firstProductIndex !in visitableList.indices) return

        visitableList.add(firstProductIndex, cpmDataView)
    }


    private fun isHeadlineAdsAllowed(isLocalSearch: Boolean): Boolean {
        return !isLocalSearch
            && (!isGlobalNavWidgetAvailable || isShowHeadlineAdsBasedOnGlobalNav)
    }

    private fun processHeadlineAdsAtPosition(
        visitableList: MutableList<Visitable<*>>,
        position: Int,
        cpmDataView: CpmDataView,
        productList: List<Visitable<*>>,
    ) {
        val headlineAdsVisitableList = arrayListOf<Visitable<ProductListTypeFactory>>()
        headlineAdsVisitableList.add(cpmDataView)

        val product = productList[position - 1]
        val headlineAdsIndex = visitableList.indexOf(product) + 1
        visitableList.addAll(headlineAdsIndex, headlineAdsVisitableList)
    }

    private fun createCpmDataView(
        cpmModel: CpmModel,
        cpmData: ArrayList<CpmData>,
        verticalSeparator: VerticalSeparator,
    ): CpmDataView {
        val cpmForViewModel = createCpmForViewModel(cpmModel, cpmData)
        return CpmDataView(cpmForViewModel, verticalSeparator)
    }

    private fun createCpmForViewModel(cpmModel: CpmModel, cpmData: ArrayList<CpmData>): CpmModel {
        return CpmModel().apply {
            header = cpmModel.header
            status = cpmModel.status
            error = cpmModel.error
            data = cpmData
        }
    }

    private fun addInspirationCarousel(
        inspirationCarouselDataView: List<InspirationCarouselDataView>,
        list: MutableList<Visitable<*>>,
        productList: List<Visitable<*>>,
        externalReference: String,
    ) {
        inspirationCarouselPresenter.setInspirationCarouselDataViewList(
            inspirationCarouselDataView
        )
        processInspirationCarouselPosition(list, productList, externalReference)
    }

    private fun addInspirationWidget(
        inspirationWidgetDataView: List<InspirationWidgetVisitable>,
        list: MutableList<Visitable<*>>,
        productList: List<Visitable<*>>,
    ) {
        inspirationWidgetPresenter.setInspirationWidgetDataViewList(
            inspirationWidgetDataView
        )
        processInspirationWidgetPosition(list, productList)
    }

    private fun processInspirationWidgetPosition(
        list: MutableList<Visitable<*>>,
        productList: List<Visitable<*>>,
    ) {
        inspirationWidgetPresenter.processInspirationWidgetPosition(productList) { position, data ->
            val visitableIndex = getVisitableIndex(productList, list, position)
            list.add(visitableIndex, data)
        }
    }

    private fun processInspirationCarouselPosition(
        list: MutableList<Visitable<*>>,
        productList: List<Visitable<*>>,
        externalReference: String,
    ) {
        inspirationCarouselPresenter.processInspirationCarouselPosition(
            productList,
            externalReference,
        ) { position, inspirationCarouselVisitableList ->
            val visitableIndex = getVisitableIndex(productList, list, position)
            list.addAll(visitableIndex, inspirationCarouselVisitableList)
        }
    }

    private fun getVisitableIndex(
        productList: List<Visitable<*>>,
        list: List<Visitable<*>>,
        widgetPosition: Int,
    ): Int {
        val productListPosition = maxOf(widgetPosition, 1)
        val product = productList[productListPosition - 1]
        val addIndex = minOf(widgetPosition, 1)

        return list.indexOf(product) + addIndex
    }

    private fun processBannerAndBroadMatchInSamePosition(
        list: MutableList<Visitable<*>>,
        productList: List<Visitable<*>>,
        responseCode: String,
    ) {
        if (!willShowBroadMatchAndBanner(responseCode)) return

        if (isShowBroadMatchAndBannerAtBottom())
            processBroadMatchAndBannerAtBottom(list)
        else if (isShowBannerAndBroadMatchAtTop())
            processBroadMatchAndBannerAtTop(list, productList)
    }

    private fun willShowBroadMatchAndBanner(responseCode: String) =
        bannerDelegate.isShowBanner() && broadMatchDelegate.isShowBroadMatch(responseCode)

    private fun isShowBroadMatchAndBannerAtBottom() =
        bannerDelegate.isLastPositionBanner && broadMatchDelegate.isLastPositionBroadMatch

    private fun processBroadMatchAndBannerAtBottom(list: MutableList<Visitable<*>>) {
        broadMatchDelegate.processBroadMatchAtBottom(list) { _, broadMatch ->
            list.addAll(broadMatch)
        }

        bannerDelegate.processBannerAtBottom(list) { _, banner ->
            list.add(banner)
        }
    }

    private fun isShowBannerAndBroadMatchAtTop() =
        broadMatchDelegate.isFirstPositionBroadMatch && bannerDelegate.isFirstPositionBanner

    private fun processBroadMatchAndBannerAtTop(
        list: MutableList<Visitable<*>>,
        productList: List<Visitable<*>>,
    ) {
        broadMatchDelegate.processBroadMatchAtTop(productList, list) { index, broadMatch ->
            list.addAll(index, broadMatch)
        }

        bannerDelegate.processBannerAtTop(list, productList) { index, banner ->
            list.add(index, banner)
        }
    }

    private fun addBanner(visitableList: MutableList<Visitable<*>>, productList: List<Visitable<*>>) {
        bannerDelegate.processBanner(visitableList, productList) { index, banner ->
            visitableList.add(index, banner)
        }
    }

    private fun addBroadMatch(
        visitableList: MutableList<Visitable<*>>,
        productList: List<Visitable<*>>,
        responseCode: String,
    ) {
        broadMatchDelegate.processBroadMatch(
            responseCode,
            productList,
            visitableList,
        ) { index, broadMatch ->
            visitableList.addAll(index, broadMatch)
        }
    }

    private fun processTopAdsImageViewModel(
        list: MutableList<Visitable<*>>,
        productList: List<Visitable<*>>,
    ) {
        topAdsImageViewPresenterDelegate.processTopAdsImageViewModel(
            list,
            productList,
            action = { index, topAdsImageView -> list.add(index, topAdsImageView) },
            { exception ->
                Timber.w(exception)
            },
        )
    }

    private fun addSearchInTokopedia(
        list: MutableList<Visitable<*>>,
        isLocalSearch: Boolean,
        globalSearchApplink: String,
    ) {
        if (pagination.isLastPage() && isLocalSearch) {
            val searchInTokopediaDataView = SearchInTokopediaDataView(globalSearchApplink)
            list.add(searchInTokopediaDataView)
        }
    }

    private fun processHeadlineAdsLoadMore(
        searchProductModel: SearchProductModel,
        list: MutableList<Visitable<*>>,
        isLocalSearch: Boolean,
        productList: List<Visitable<*>>,
    ) {
        if (!isHeadlineAdsAllowed(isLocalSearch)) return

        topAdsHeadlineHelper.processHeadlineAds(searchProductModel.cpmModel) { _, cpmDataList, isUseSeparator ->
            val verticalSeparator = if (isUseSeparator)
                VerticalSeparator.Both
            else VerticalSeparator.None
            val cpmDataView = createCpmDataView(
                searchProductModel.cpmModel,
                cpmDataList,
                verticalSeparator
            )
            processHeadlineAdsAtPosition(
                list,
                productList.size,
                cpmDataView,
                productList
            )
        }
    }

    fun createLoadMoreVisitableList(data: VisitableFactorySecondPageData): List<Visitable<*>> {
        val visitableList = mutableListOf<Visitable<*>>()

        addProductList(visitableList, data.loadMoreProductList)
        processHeadlineAdsLoadMore(
            data.searchProductModel,
            visitableList,
            data.isLocalSearch,
            data.allProductList,
        )
        processTopAdsImageViewModel(visitableList, data.allProductList)
        processInspirationWidgetPosition(visitableList, data.allProductList)
        processInspirationCarouselPosition(visitableList, data.allProductList, data.externalReference)
        processBannerAndBroadMatchInSamePosition(
            visitableList,
            data.allProductList,
            data.responseCode,
        )
        addBanner(visitableList, data.allProductList)
        broadMatchDelegate.processBroadMatch(
            data.responseCode,
            data.allProductList,
            visitableList,
        ) { index, broadMatch ->
            visitableList.addAll(index, broadMatch)
        }
        addSearchInTokopedia(visitableList, data.isLocalSearch, data.globalSearchApplink)

        return visitableList
    }

    fun createEmptyResultDuringLoadMoreVisitableList(
        responseCode: String,
        productList: List<Visitable<*>>,
        isLocalSearch: Boolean,
        globalSearchApplink: String,
    ): List<Visitable<*>> {
        val visitableList = mutableListOf<Visitable<*>>()

        broadMatchDelegate.processBroadMatch(responseCode, productList, visitableList) { index, broadMatch ->
            visitableList.addAll(index, broadMatch)
        }
        addSearchInTokopedia(visitableList, isLocalSearch, globalSearchApplink)

        return visitableList
    }

    fun createViolationVisitableList(
        productDataView: ProductDataView,
        globalNavDataView: GlobalNavDataView?,
    ) : List<Visitable<*>> {
        val violation = productDataView.violation ?: return emptyList()
        return mutableListOf<Visitable<*>>().apply {
            globalNavDataView?.let { add(it) }

            add(violation)
        }
    }

    fun constructEmptyStateProductList(
        globalNavDataView: GlobalNavDataView?,
        emptyStateDataView: EmptyStateDataView,
    ): List<Visitable<*>> =
        mutableListOf<Visitable<*>>().apply {
            globalNavDataView?.let { add(it) }
            add(emptyStateDataView)
        }
}
