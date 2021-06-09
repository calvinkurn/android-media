package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.curatedcampaign.CuratedCampaignDataView
import com.tokopedia.autocomplete.initialstate.data.InitialStateUniverse
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateSearchDataView
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchSeeMoreDataView
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchDataView
import com.tokopedia.autocomplete.jsonToObject
import com.tokopedia.autocomplete.shouldBe
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val initialStateWithSeeMoreRecentSearch = "autocomplete/initialstate/with-5-data-show-more-recent-search.json"

internal class OnInitialStateItemClickTest: InitialStatePresenterTestFixtures(){

    private val keyword = "sepatu"
    private val shopId = "8384142"
    private val shopName = "MizanBookCorner"
    private val applinkShop = "tokopedia://shop/$shopId?source=universe&st=product"
    private val slotRecentSearchDataView = slot<RecentSearchDataView>()

    @Test
    fun `test click recent search item`() {
        `Given view already get initial state`(initialStateCommonResponse)

        val data = findDataView<RecentSearchDataView>()
        val item = data.list.findByType(TYPE_KEYWORD)
        val position = data.list.indexOf(item)

        `when recent search item clicked` (item, position)
        `then verify view interaction is correct`(item, position)
    }

    private fun `given initial state use case capture request params`(list: List<InitialStateData>) {
        every { getInitialStateUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateData>>>().onStart()
            secondArg<Subscriber<List<InitialStateData>>>().onNext(list)
        }
    }

    private fun `when recent search item clicked`(item: BaseItemInitialStateSearch, position: Int) {
        initialStatePresenter.onRecentSearchItemClicked(item, position)
    }

    private fun `then verify view interaction is correct`(item: BaseItemInitialStateSearch, position: Int) {
        verifyOrder {
            initialStateView.onClickRecentSearch(item, position)
        }
    }

    private fun InitialStateContract.View.onClickRecentSearch(item: BaseItemInitialStateSearch, position: Int) {
        trackEventClickRecentSearch(getItemEventLabelForTracking(item, position))
        route(item.applink, initialStatePresenter.getSearchParameter())
        finish()
    }

    private fun getItemEventLabelForTracking(item: BaseItemInitialStateSearch, adapterPosition: Int): String {
        return String.format(
                "value: %s - po: %s - applink: %s",
                item.title,
                (adapterPosition + 1).toString(),
                item.applink
        )
    }

    @Test
    fun `test click recent shop item`() {
        `Given view already get initial state`(initialStateCommonResponse)

        val data = findDataView<RecentSearchDataView>()
        val item = data.list.findByType(TYPE_SHOP)
        val position = data.list.indexOf(item)

        `when recent search item clicked` (item, position)
        `then verify view interaction is correct for recent shop`(item)
    }

    private fun `then verify view interaction is correct for recent shop`(item: BaseItemInitialStateSearch) {
        initialStateView.onClickRecentShop(item)
    }

    private fun InitialStateContract.View.onClickRecentShop(item: BaseItemInitialStateSearch) {
        verifyOrder {
            trackEventClickRecentShop(getRecentShopLabelForTracking(item), any())
            route(item.applink, initialStatePresenter.getSearchParameter())
            finish()
        }
    }

    private fun getRecentShopLabelForTracking(item: BaseItemInitialStateSearch): String {
        return item.productId + " - keyword: " + item.title
    }

    @Test
    fun `Test click Show More Recent Search`() {
        `Given view already get initial state`(initialStateWithSeeMoreRecentSearch)

        `When recent search see more button is clicked`()

        `Then verify RecentSearchSeeMoreDataView has been removed`()
        `Then verify renderRecentSearch is called`()

        val initialStateData = initialStateWithSeeMoreRecentSearch.jsonToObject<InitialStateUniverse>().data
        `Then verify initial state show all recent search`(initialStateData)
    }

    private fun `When recent search see more button is clicked`() {
        initialStatePresenter.recentSearchSeeMoreClicked()
    }

    private fun `Then verify RecentSearchSeeMoreDataView has been removed`() {
        val recentSearchSeeMoreDataView = slotVisitableList.captured.find { it is RecentSearchSeeMoreDataView }
        assert(recentSearchSeeMoreDataView == null) {
            "There should be no RecentSearchSeeMoreDataView in visitable list"
        }
    }

    private fun `Then verify renderRecentSearch is called`() {
        verifyOrder {
            initialStateView.trackEventClickSeeMoreRecentSearch("0")
            initialStateView.dropKeyBoard()
            initialStateView.renderCompleteRecentSearch(capture(slotRecentSearchDataView))
        }
    }

    private fun `Then verify initial state show all recent search`(initialStateData: List<InitialStateData>) {

        val recentSearchDataView = slotRecentSearchDataView.captured
        val recentSearchResponse = initialStateData.find { it.featureId == "recent_search" }

        recentSearchDataView.list.size shouldBe recentSearchResponse?.items?.size
    }

    @Test
    fun `Test click Dynamic Section Search`() {
        `Given view already get initial state`(initialStateCommonResponse)

        val data = findDataView<DynamicInitialStateSearchDataView>()
        val item = data.list.findByType()
        val position = data.list.indexOf(item)

        `When recent dynamic section item is clicked`(item, position)
        `Then verify view interaction is correct for dynamic section`(item)
    }

    private fun `When recent dynamic section item is clicked`(item: BaseItemInitialStateSearch, position: Int) {
        initialStatePresenter.onDynamicSectionItemClicked(item, position)
    }

    private fun `Then verify view interaction is correct for dynamic section`(item: BaseItemInitialStateSearch) {
        initialStateView.onClickDynamicSectionItem(item)
    }

    private fun InitialStateContract.View.onClickDynamicSectionItem(item: BaseItemInitialStateSearch) {
        val expectedLabel = "value: ${item.title} - title: ${item.header} - po: 1"

        verifyOrder {
            trackEventClickDynamicSectionItem(any(), expectedLabel, item.featureId)
            route(item.applink, initialStatePresenter.getSearchParameter())
            finish()
        }
    }

    @Test
    fun `Test click Curated Campaign Card`() {
        `Given view already get initial state`(initialStateWithSeeMoreRecentSearch)

        val item = findDataView<CuratedCampaignDataView>()

        `When recent dynamic section item is clicked`(item)
        `Then verify view interaction is correct for dynamic section`(item)
    }

    private fun `When recent dynamic section item is clicked`(item: CuratedCampaignDataView) {
        initialStatePresenter.onCuratedCampaignCardClicked(item)
    }

    private fun `Then verify view interaction is correct for dynamic section`(item: CuratedCampaignDataView) {
        initialStateView.onClickCuratedCampaignCard(item)
    }

    private fun InitialStateContract.View.onClickCuratedCampaignCard(item: CuratedCampaignDataView) {
        val expectedLabel = "${item.title} - ${item.applink}"

        verifyOrder {
            trackEventClickCuratedCampaignCard(any(), expectedLabel, item.type)
            route(item.applink, initialStatePresenter.getSearchParameter())
            finish()
        }
    }
}