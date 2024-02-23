package com.tokopedia.search.result.product.visitable

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.domain.model.SearchProductModel
import com.tokopedia.search.result.presentation.model.ChooseAddressDataView
import com.tokopedia.search.result.presentation.model.SearchProductTitleDataView
import com.tokopedia.search.result.presentation.model.TickerDataView
import com.tokopedia.search.result.product.byteio.ByteIOTrackingData
import com.tokopedia.search.result.product.QueryKeyProvider
import com.tokopedia.search.result.product.banner.BannerPresenterDelegate
import com.tokopedia.search.result.product.broadmatch.BroadMatchPresenterDelegate
import com.tokopedia.search.result.product.byteio.ByteIOTrackingDataFactory
import com.tokopedia.search.result.product.cpm.CpmDataView
import com.tokopedia.search.result.product.globalnavwidget.GlobalNavDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselPresenterDelegate
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetPresenterDelegate
import com.tokopedia.search.result.product.inspirationwidget.InspirationWidgetVisitable
import com.tokopedia.search.result.product.lastfilter.LastFilterDataView
import com.tokopedia.search.result.product.pagination.Pagination
import com.tokopedia.search.result.product.performancemonitoring.PerformanceMonitoringProvider
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_BROADMATCH
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_HEADLINE_ADS
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_INSPIRATION_CAROUSEL
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_INSPIRATION_WIDGET
import com.tokopedia.search.result.product.performancemonitoring.SEARCH_RESULT_PLT_RENDER_LOGIC_TDN
import com.tokopedia.search.result.product.performancemonitoring.runCustomMetric
import com.tokopedia.search.result.product.productitem.ProductItemVisitable
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
    private val pagination: Pagination,
    private val byteIOTrackingDataFactory: ByteIOTrackingDataFactory,
) {

    private var isGlobalNavWidgetAvailable = false
    private var isShowHeadlineAdsBasedOnGlobalNav = false
    private val visitableList = mutableListOf<Visitable<*>>()
    private val performanceMonitoring: PageLoadTimePerformanceInterface? =
        performanceMonitoringProvider.get()

    fun createFirstPageVisitableList(data: VisitableFactoryFirstPageData): List<Visitable<*>> {
        visitableList.clear()

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
        addInspirationCarouselSeamless(
            visitableList,
            data.externalReference,
            data.keyword,
            productDataView.seamlessCarouselDataViewList,
        )
        runCustomMetric(performanceMonitoring, SEARCH_RESULT_PLT_RENDER_LOGIC_HEADLINE_ADS) {
            processHeadlineAdsFirstPage(
                data.searchProductModel,
                visitableList,
                data.isLocalSearch,
            )
        }
        runCustomMetric(performanceMonitoring, SEARCH_RESULT_PLT_RENDER_LOGIC_INSPIRATION_CAROUSEL) {
            addInspirationCarousel(
                productDataView.inspirationCarouselDataView,
                visitableList,
                data.externalReference,
            )
        }
        runCustomMetric(performanceMonitoring, SEARCH_RESULT_PLT_RENDER_LOGIC_INSPIRATION_WIDGET) {
            addInspirationWidget(
                productDataView.inspirationWidgetDataView,
                visitableList,
            )
        }
        processBannerAndBroadMatchInSamePosition(visitableList, data.responseCode)
        addBanner(visitableList)
        runCustomMetric(performanceMonitoring, SEARCH_RESULT_PLT_RENDER_LOGIC_BROADMATCH) {
            addBroadMatch(data.responseCode, visitableList)
        }
        runCustomMetric(performanceMonitoring, SEARCH_RESULT_PLT_RENDER_LOGIC_TDN) {
            topAdsImageViewPresenterDelegate.setTopAdsImageViewModelList(
                data.searchProductModel.getTopAdsImageViewModelList()
            )
            processTopAdsImageViewModel(visitableList)
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
        suggestionPresenter.processSuggestion(responseCode, visitableList::add)
    }

    private fun addProductList(
        visitableList: MutableList<Visitable<*>>,
        productList: List<Visitable<*>>,
    ) {
        visitableList.addAll(productList)
    }

    private fun addInspirationCarouselSeamless(
        visitableList: MutableList<Visitable<*>>,
        externalReference: String,
        keyword: String,
        seamlessCarouselDataViewList: List<InspirationCarouselDataView>,
    ) {
        inspirationCarouselPresenter
            .setInspirationCarouselSeamlessDataViewList(seamlessCarouselDataViewList)

        processInspirationCarouselSeamlessPosition(visitableList, externalReference, keyword)
    }

    private fun processInspirationCarouselSeamlessPosition(
        visitableList: MutableList<Visitable<*>>,
        externalReference: String,
        keyword: String,
    ) {
        inspirationCarouselPresenter.processInspirationCarouselSeamlessPosition(
            visitableList.getTotalProductItem(),
            externalReference,
            keyword,
        ) { position, inspirationSeamlessVisitable ->
            visitableList.addAll(
                visitableList.getIndexForWidgetPosition(position),
                inspirationSeamlessVisitable,
            )
        }
    }

    private fun processHeadlineAdsFirstPage(
        searchProductModel: SearchProductModel,
        list: MutableList<Visitable<*>>,
        isLocalSearch: Boolean,
    ) {
        if (!isHeadlineAdsAllowed(isLocalSearch)) return

        topAdsHeadlineHelper.processHeadlineAds(
            cpmModel = searchProductModel.cpmModel,
            pageNumber = 1,
        ) { index, cpmDataList, isUseSeparator ->
            val verticalSeparator = headlineAdsVerticalSeparator(isUseSeparator, index)
            val cpmDataView = createCpmDataView(
                searchProductModel.cpmModel,
                cpmDataList,
                verticalSeparator,
                byteIOTrackingDataFactory.create(true),
            )

            if (index == 0) processHeadlineAdsAtTop(list, cpmDataView)
            else processHeadlineAdsAtBottom(list, cpmDataView)
        }
    }

    private fun headlineAdsVerticalSeparator(isUseSeparator: Boolean, index: Int) =
        if (isUseSeparator && index != 0) VerticalSeparator.Both
        else VerticalSeparator.None

    private fun processHeadlineAdsAtTop(
        visitableList: MutableList<Visitable<*>>,
        cpmDataView: CpmDataView,
    ) {
        val firstProductIndex = visitableList.indexOfFirstProductItem()
        if (firstProductIndex !in visitableList.indices) return

        visitableList.add(firstProductIndex, cpmDataView)
    }

    private fun isHeadlineAdsAllowed(isLocalSearch: Boolean): Boolean {
        return !isLocalSearch
            && (!isGlobalNavWidgetAvailable || isShowHeadlineAdsBasedOnGlobalNav)
    }

    private fun processHeadlineAdsAtBottom(
        visitableList: MutableList<Visitable<*>>,
        cpmDataView: CpmDataView,
    ) {
        visitableList.add(cpmDataView)
    }

    private fun createCpmDataView(
        cpmModel: CpmModel,
        cpmData: ArrayList<CpmData>,
        verticalSeparator: VerticalSeparator,
        byteIOTrackingData: ByteIOTrackingData,
    ): CpmDataView {
        val cpmForViewModel = createCpmForViewModel(cpmModel, cpmData)
        return CpmDataView(cpmForViewModel, verticalSeparator, byteIOTrackingData)
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
        externalReference: String,
    ) {
        inspirationCarouselPresenter.setInspirationCarouselDataViewList(
            inspirationCarouselDataView
        )
        processInspirationCarouselPosition(
            list,
            externalReference,
            true,
        )
    }

    private fun processInspirationCarouselPosition(
        list: MutableList<Visitable<*>>,
        externalReference: String,
        isFirstPage: Boolean,
    ) {
        inspirationCarouselPresenter.processInspirationCarouselPosition(
            visitableList.getTotalProductItem(),
            externalReference,
            isFirstPage,
        ) { position, inspirationCarouselVisitableList ->
            val visitableIndex = visitableList.getIndexForWidgetPosition(position)
            list.addAll(visitableIndex, inspirationCarouselVisitableList)
        }
    }

    private fun addInspirationWidget(
        inspirationWidgetDataView: List<InspirationWidgetVisitable>,
        visitableList: MutableList<Visitable<*>>,
    ) {
        inspirationWidgetPresenter.setInspirationWidgetDataViewList(inspirationWidgetDataView)
        processInspirationWidgetPosition(visitableList)
    }

    private fun processInspirationWidgetPosition(visitableList: MutableList<Visitable<*>>) {
        inspirationWidgetPresenter.processInspirationWidgetPosition(
            visitableList.getTotalProductItem(),
        ) { position, data ->
            val visitableIndex = visitableList.getIndexForWidgetPosition(position)
            visitableList.add(visitableIndex, data)
        }
    }

    private fun processBannerAndBroadMatchInSamePosition(
        visitableList: MutableList<Visitable<*>>,
        responseCode: String,
    ) {
        if (!willShowBroadMatchAndBanner(responseCode)) return

        if (isShowBroadMatchAndBannerAtBottom())
            processBroadMatchAndBannerAtBottom(visitableList)
        else if (isShowBannerAndBroadMatchAtTop())
            processBroadMatchAndBannerAtTop(visitableList)
    }

    private fun willShowBroadMatchAndBanner(responseCode: String) =
        bannerDelegate.isShowBanner() && broadMatchDelegate.isShowBroadMatch(responseCode)

    private fun isShowBroadMatchAndBannerAtBottom() =
        bannerDelegate.isLastPositionBanner && broadMatchDelegate.isLastPositionBroadMatch

    private fun processBroadMatchAndBannerAtBottom(list: MutableList<Visitable<*>>) {
        broadMatchDelegate.processBroadMatchAtBottom { _, broadMatch ->
            list.addAll(broadMatch)
        }

        bannerDelegate.processBannerAtBottom { _, banner ->
            list.add(banner)
        }
    }

    private fun isShowBannerAndBroadMatchAtTop() =
        broadMatchDelegate.isFirstPositionBroadMatch && bannerDelegate.isFirstPositionBanner

    private fun processBroadMatchAndBannerAtTop(
        visitableList: MutableList<Visitable<*>>,
    ) {
        broadMatchDelegate.processBroadMatchAtTop { _, broadMatch ->
            visitableList.addAll(visitableList.indexOfFirstProductItem(), broadMatch)
        }

        bannerDelegate.processBannerAtTop { _, banner ->
            visitableList.add(visitableList.indexOfFirstProductItem(), banner)
        }
    }

    private fun addBanner(visitableList: MutableList<Visitable<*>>) {
        bannerDelegate.processBanner(visitableList.getTotalProductItem()) { position, banner ->
            val visitableIndex =
                if (bannerDelegate.isFirstPositionBanner) visitableList.indexOfFirstProductItem()
                else if (bannerDelegate.isLastPositionBanner) visitableList.size
                else visitableList.getIndexForWidgetPosition(position)

            visitableList.add(visitableIndex, banner)
        }
    }

    private fun addBroadMatch(
        responseCode: String,
        visitableList: MutableList<Visitable<*>>,
    ) {
        broadMatchDelegate.processBroadMatch(
            visitableList.getTotalProductItem(),
            responseCode,
        ) { position, broadMatch ->
            val visitableIndex =
                if (broadMatchDelegate.isFirstPositionBroadMatch) visitableList.indexOfFirstProductItem()
                else if (broadMatchDelegate.isLastPositionBroadMatch) visitableList.size
                else visitableList.getIndexForWidgetPosition(position)

            visitableList.addAll(visitableIndex, broadMatch)
        }
    }

    private fun processTopAdsImageViewModel(visitableList: MutableList<Visitable<*>>) {
        topAdsImageViewPresenterDelegate.processTopAdsImageViewModel(
            totalProductItem = visitableList.getTotalProductItem(),
            action = { position, topAdsImageView ->
                visitableList.add(
                    getVisitableIndexForTDN(position),
                    topAdsImageView,
                )
            },
            logAction = Timber::w,
        )
    }

    private fun getVisitableIndexForTDN(position: Int): Int =
        if (position == TopAdsImageViewPresenterDelegate.TOP_POSITION) {
            visitableList.indexOfFirst {
                it is CpmDataView || it is ProductItemVisitable
            }
        } else {
            visitableList.getIndexForWidgetPosition(position)
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
    ) {
        if (!isHeadlineAdsAllowed(isLocalSearch)) return

        topAdsHeadlineHelper.processHeadlineAds(searchProductModel.cpmModel) { _, cpmDataList, isUseSeparator ->
            val verticalSeparator = headlineAdsVerticalSeparator(isUseSeparator, 1)
            val cpmDataView = createCpmDataView(
                searchProductModel.cpmModel,
                cpmDataList,
                verticalSeparator,
                byteIOTrackingDataFactory.create(false),
            )

            processHeadlineAdsAtBottom(list, cpmDataView)
        }
    }

    fun createLoadMoreVisitableList(data: VisitableFactorySecondPageData): List<Visitable<*>> {
        val previousVisitableList = visitableList.toList()

        addProductList(visitableList, data.loadMoreProductList)
        processInspirationCarouselSeamlessPosition(
            visitableList,
            data.externalReference,
            data.keyword,
        )
        processHeadlineAdsLoadMore(
            data.searchProductModel,
            visitableList,
            data.isLocalSearch,
        )
        processTopAdsImageViewModel(visitableList)
        processInspirationWidgetPosition(visitableList)
        processInspirationCarouselPosition(
            visitableList,
            data.externalReference,
            false,
        )
        processBannerAndBroadMatchInSamePosition(visitableList, data.responseCode)
        addBanner(visitableList)
        addBroadMatch(data.responseCode, visitableList)
        addSearchInTokopedia(visitableList, data.isLocalSearch, data.globalSearchApplink)

        return visitableList - previousVisitableList.toSet()
    }

    fun createEmptyResultDuringLoadMoreVisitableList(
        responseCode: String,
        isLocalSearch: Boolean,
        globalSearchApplink: String,
    ): List<Visitable<*>> {
        val visitableList = mutableListOf<Visitable<*>>()

        broadMatchDelegate.processBroadMatch(
            visitableList.getTotalProductItem(),
            responseCode,
        ) { index, broadMatch ->
            visitableList.addAll(index, broadMatch)
        }

        addSearchInTokopedia(visitableList, isLocalSearch, globalSearchApplink)

        return visitableList
    }
}
