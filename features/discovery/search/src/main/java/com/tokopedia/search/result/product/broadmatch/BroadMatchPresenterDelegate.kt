package com.tokopedia.search.result.product.broadmatch

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.di.scope.SearchScope
import com.tokopedia.search.result.product.ClassNameProvider
import com.tokopedia.search.result.product.ViewUpdater
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDynamicProductView
import com.tokopedia.search.result.product.localsearch.EMPTY_LOCAL_SEARCH_RESPONSE_CODE
import com.tokopedia.search.result.product.pagination.Pagination
import com.tokopedia.search.result.product.separator.VerticalSeparatorMapper
import com.tokopedia.search.result.product.suggestion.SuggestionPresenter
import com.tokopedia.search.utils.applinkmodifier.ApplinkModifier
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import timber.log.Timber
import javax.inject.Inject

@SearchScope
class BroadMatchPresenterDelegate @Inject constructor(
    private val broadMatchView: BroadMatchView,
    private val dynamicProductView: InspirationCarouselDynamicProductView,
    private val topAdsUrlHitter: TopAdsUrlHitter,
    private val classNameProvider: ClassNameProvider,
    private val applinkModifier: ApplinkModifier,
    private val pagination: Pagination,
    private val suggestionPresenter: SuggestionPresenter,
) : BroadMatchPresenter {

    var relatedDataView: RelatedDataView? = null
        private set

    private val broadMatchPosition: Int
        get() = relatedDataView?.position ?: -1

    val isFirstPositionBroadMatch: Boolean
        get() = broadMatchPosition == FIRST_POSITION

    val isLastPositionBroadMatch: Boolean
        get() = broadMatchPosition == LAST_POSITION

    fun setRelatedDataView(relatedDataView: RelatedDataView?) {
        this.relatedDataView = relatedDataView
    }

    fun isShowBroadMatch(responseCode: String) =
        showBroadMatchResponseCodeList.contains(responseCode) && hasBroadMatch()

    private fun hasBroadMatch() = relatedDataView?.broadMatchDataViewList?.isNotEmpty() == true

    fun processBroadMatchReplaceEmptySearch(
        action: (List<Visitable<*>>) -> Unit
    ) {
        action(createBroadMatchToVisitableList())
    }

    private fun createBroadMatchToVisitableList(): List<Visitable<*>> {
        val visitableList = mutableListOf<Visitable<*>>()

        suggestionPresenter.appendSuggestion(visitableList)
        appendBroadMatch(visitableList)

        return visitableList
    }

    private fun appendBroadMatch(list: MutableList<Visitable<*>>) {
        relatedDataView?.let {
            list.addAll(it.broadMatchDataViewList)
            relatedDataView = null
        }
    }

    fun processBroadMatchInEmptyLocalSearch(
        responseCode: String,
        action: (List<Visitable<*>>) -> Unit,
    ) {
        if (isShowBroadMatchWithEmptyLocalSearch(responseCode)) {
            action(createBroadMatchToVisitableList())
        }
    }

    private fun isShowBroadMatchWithEmptyLocalSearch(responseCode: String) =
        responseCode == EMPTY_LOCAL_SEARCH_RESPONSE_CODE
            && hasBroadMatch()

    fun processBroadMatch(
        responseCode: String,
        productList: List<Visitable<*>>,
        visitableList: List<Visitable<*>>,
        action: (Int, List<Visitable<*>>) -> Unit,
    ) {
        try {
            if (!isShowBroadMatch(responseCode)) return

            val broadMatchPosition = relatedDataView?.position ?: -1
            if (isLastPositionBroadMatch)
                processBroadMatchAtBottom(visitableList, action)
            else if (isFirstPositionBroadMatch)
                processBroadMatchAtTop(productList, visitableList, action)
            else if (broadMatchPosition > 1)
                processBroadMatchAtPosition(productList, action, visitableList, broadMatchPosition)
        } catch (exception: Throwable) {
            Timber.w(exception)
        }
    }

    fun processBroadMatchAtBottom(
        visitableList: List<Visitable<*>>,
        action: (Int, List<Visitable<*>>) -> Unit,
    ) {
        if (pagination.isLastPage()) {
            val broadMatchVisitableList = createBroadMatchToVisitableList()
            action(
                visitableList.size,
                VerticalSeparatorMapper.addVerticalSeparator(
                    broadMatchVisitableList,
                    addBottomSeparator = false
                )
            )
        }
    }

    fun processBroadMatchAtTop(
        productList: List<Visitable<*>>,
        visitableList: List<Visitable<*>>,
        action: (Int, List<Visitable<*>>) -> Unit,
    ) {
        val broadMatchVisitableList = createBroadMatchToVisitableList()

        action(
            visitableList.indexOf(productList[0]),
            VerticalSeparatorMapper.addVerticalSeparator(
                broadMatchVisitableList,
                addTopSeparator = false
            )
        )
    }

    private fun processBroadMatchAtPosition(
        productList: List<Visitable<*>>,
        action: (Int, List<Visitable<*>>) -> Unit,
        visitableList: List<Visitable<*>>,
        broadMatchPosition: Int,
    ) {
        if (productList.size < broadMatchPosition) return

        val broadMatchVisitableList = createBroadMatchToVisitableList()

        val productItemAtBroadMatchPosition = productList[broadMatchPosition - 1]
        val broadMatchIndex = visitableList.indexOf(productItemAtBroadMatchPosition) + 1

        action(
            broadMatchIndex,
            VerticalSeparatorMapper.addVerticalSeparator(broadMatchVisitableList)
        )
    }

    override fun onBroadMatchItemImpressed(broadMatchItemDataView: BroadMatchItemDataView) {
        if (broadMatchItemDataView.isOrganicAds)
            sendTrackingImpressBroadMatchAds(broadMatchItemDataView)

        when (val carouselProductType = broadMatchItemDataView.carouselProductType) {
            is BroadMatchProduct ->
                broadMatchView.trackEventImpressionBroadMatchItem(broadMatchItemDataView)
            is DynamicCarouselProduct ->
                dynamicProductView.trackDynamicProductCarouselImpression(
                    broadMatchItemDataView,
                    carouselProductType.type,
                    carouselProductType.inspirationCarouselProduct,
                )
        }
    }

    private fun sendTrackingImpressBroadMatchAds(broadMatchItemDataView: BroadMatchItemDataView) {
        topAdsUrlHitter.hitImpressionUrl(
            classNameProvider.className,
            broadMatchItemDataView.topAdsViewUrl,
            broadMatchItemDataView.id,
            broadMatchItemDataView.name,
            broadMatchItemDataView.imageUrl,
            SearchConstant.TopAdsComponent.BROAD_MATCH_ADS
        )
    }

    override fun onBroadMatchItemClick(broadMatchItemDataView: BroadMatchItemDataView) {
        when (val carouselProductType = broadMatchItemDataView.carouselProductType) {
            is BroadMatchProduct ->
                broadMatchView.trackEventClickBroadMatchItem(broadMatchItemDataView)
            is DynamicCarouselProduct ->
                dynamicProductView.trackDynamicProductCarouselClick(
                    broadMatchItemDataView,
                    carouselProductType.type,
                    carouselProductType.inspirationCarouselProduct,
                )
        }

        broadMatchView.openLink(broadMatchItemDataView.applink, broadMatchItemDataView.url)

        if (broadMatchItemDataView.isOrganicAds)
            sendTrackingClickBroadMatchAds(broadMatchItemDataView)
    }

    private fun sendTrackingClickBroadMatchAds(broadMatchItemDataView: BroadMatchItemDataView) {
        topAdsUrlHitter.hitClickUrl(
            classNameProvider.className,
            broadMatchItemDataView.topAdsClickUrl,
            broadMatchItemDataView.id,
            broadMatchItemDataView.name,
            broadMatchItemDataView.imageUrl,
            SearchConstant.TopAdsComponent.BROAD_MATCH_ADS
        )
    }

    override fun onBroadMatchImpressed(broadMatchDataView: BroadMatchDataView) {
        if (broadMatchDataView.carouselOptionType == BroadMatch)
            broadMatchView.trackEventImpressionBroadMatch(broadMatchDataView)
    }

    override fun onBroadMatchSeeMoreClick(broadMatchDataView: BroadMatchDataView) {
        val applink = applinkModifier.modifyApplink(broadMatchDataView.applink)
        handleBroadMatchSeeMoreClick(broadMatchDataView, applink)
    }

    override fun onBroadMatchViewAllCardClicked(broadMatchDataView: BroadMatchDataView) {
        val applink = applinkModifier.modifyApplink(broadMatchDataView.cardButton.applink)
        handleBroadMatchSeeMoreClick(broadMatchDataView, applink)
    }

    private fun handleBroadMatchSeeMoreClick(
        broadMatchDataView: BroadMatchDataView,
        applink: String,
    ) {
        trackBroadMatchSeeMoreClick(broadMatchDataView)

        broadMatchView.openLink(applink, broadMatchDataView.url)
    }

    private fun trackBroadMatchSeeMoreClick(broadMatchDataView: BroadMatchDataView) {
        when (val carouselOptionType = broadMatchDataView.carouselOptionType) {
            is BroadMatch ->
                broadMatchView.trackEventClickSeeMoreBroadMatch(broadMatchDataView)
            is DynamicCarouselOption ->
                dynamicProductView.trackEventClickSeeMoreDynamicProductCarousel(
                    broadMatchDataView,
                    carouselOptionType.option.inspirationCarouselType,
                    carouselOptionType.option,
                )
        }
    }
}
