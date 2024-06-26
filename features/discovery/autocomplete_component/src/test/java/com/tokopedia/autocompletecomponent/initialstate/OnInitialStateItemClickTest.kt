package com.tokopedia.autocompletecomponent.initialstate

import com.tokopedia.analytics.byteio.search.AppLogSearch
import com.tokopedia.autocompletecomponent.initialstate.chips.InitialStateChipWidgetDataView
import com.tokopedia.autocompletecomponent.initialstate.curatedcampaign.CuratedCampaignDataView
import com.tokopedia.autocompletecomponent.initialstate.data.InitialStateUniverse
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.popularsearch.PopularSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.productline.InitialStateProductListDataView
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchSeeMoreDataView
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewDataView
import com.tokopedia.autocompletecomponent.initialstate.searchbareducation.SearchBarEducationDataView
import com.tokopedia.autocompletecomponent.jsonToObject
import com.tokopedia.autocompletecomponent.shouldBe
import com.tokopedia.discovery.common.constants.SearchApiConst
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

private const val initialStateWithSeeMoreRecentSearch = "autocomplete/initialstate/with-5-data-show-more-recent-search.json"
private const val initialStateWithSearchBarEducation = "autocomplete/initialstate/with-searchbar-education.json"

internal class OnInitialStateItemClickTest: InitialStatePresenterTestFixtures(){

    private val shopId = "8384142"
    private val shopName = "MizanBookCorner"
    private val applinkShop = "tokopedia://shop/$shopId?source=universe&st=product"
    private val slotRecentSearchDataView = slot<RecentSearchDataView>()

    private val searchProductPageTitle = "Toko Now"
    private val searchParameter = mapOf(
            SearchApiConst.NAVSOURCE to "tokonow",
            SearchApiConst.SRP_PAGE_TITLE to searchProductPageTitle,
            SearchApiConst.SRP_PAGE_ID to "1234"
    )

    @Test
    fun `test click recent search item`() {
        `Given view already get initial state`(initialStateCommonResponse)

        val data = findDataView<RecentSearchDataView>()
        val item = data.list.findByType(TYPE_KEYWORD)

        `when recent search item clicked` (item)
        `then verify view interaction is correct`(item)
    }

    private fun `when recent search item clicked`(item: BaseItemInitialStateSearch) {
        initialStatePresenter.onRecentSearchItemClicked(item)
    }

    private fun `then verify view interaction is correct`(item: BaseItemInitialStateSearch) {
        initialStateView.onClickRecentSearch(item)
    }

    private fun InitialStateContract.View.onClickRecentSearch(item: BaseItemInitialStateSearch) {
        verifyOrder {
            trackEventClickRecentSearch(item, getItemEventLabelForTracking(item))
            route(item.applink, initialStatePresenter.getSearchParameter(), AppLogSearch.ParamValue.SEARCH_HISTORY)
            finish()
        }
    }

    private fun getItemEventLabelForTracking(item: BaseItemInitialStateSearch): String {
        return String.format(
                "value: %s - po: %s - applink: %s",
                item.title,
                item.position,
                item.applink
        )
    }

    @Test
    fun `test click recent shop item`() {
        `Given view already get initial state`(initialStateCommonResponse)

        val data = findDataView<RecentSearchDataView>()
        val item = data.list.findByType(TYPE_SHOP)

        `when recent search item clicked` (item)
        `then verify view interaction is correct for recent shop`(item)
    }

    private fun `then verify view interaction is correct for recent shop`(item: BaseItemInitialStateSearch) {
        initialStateView.onClickRecentShop(item)
    }

    private fun InitialStateContract.View.onClickRecentShop(item: BaseItemInitialStateSearch) {
        verifyOrder {
            trackEventClickRecentShop(item, getRecentShopLabelForTracking(item), any())
            route(item.applink, initialStatePresenter.getSearchParameter(), any())
            finish()
        }
    }

    private fun getRecentShopLabelForTracking(item: BaseItemInitialStateSearch): String {
        return item.productId + " - keyword: " + item.title
    }

    @Test
    fun `Test click Show More Recent Search`() {
        `Given rollance is off`()
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

        `When recent dynamic section item is clicked`(item)
        `Then verify view interaction is correct for dynamic section`(item)
    }

    private fun `When recent dynamic section item is clicked`(item: BaseItemInitialStateSearch) {
        initialStatePresenter.onDynamicSectionItemClicked(item)
    }

    private fun `Then verify view interaction is correct for dynamic section`(item: BaseItemInitialStateSearch) {
        initialStateView.onClickDynamicSectionItem(item)
    }

    private fun InitialStateContract.View.onClickDynamicSectionItem(item: BaseItemInitialStateSearch) {
        val expectedLabel = "value: ${item.title} - title: ${item.header} - po: 1"

        verifyOrder {
            trackEventClickDynamicSectionItem(
                any(),
                expectedLabel,
                item,
                item.featureId,
                item.dimension90
            )
            route(item.applink, initialStatePresenter.getSearchParameter(), "")
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
        val baseItemInitialState = item.baseItemInitialState
        val expectedLabel = "${baseItemInitialState.title} - ${baseItemInitialState.applink}"

        verifyOrder {
            trackEventClickCuratedCampaignCard(
                any(),
                expectedLabel,
                baseItemInitialState,
                baseItemInitialState.type,
                baseItemInitialState.campaignCode
            )
            route(baseItemInitialState.applink, initialStatePresenter.getSearchParameter(), "")
            finish()
        }
    }

    @Test
    fun `Test click Recent View`() {
        `Given view already get initial state`(initialStateCommonResponse)

        val item = findDataView<RecentViewDataView>().list[0]

        `When click recent view`(item)
        `Then verify view interaction is correct for recent view`(item)
    }

    private fun `When click recent view`(item: BaseItemInitialStateSearch) {
        initialStatePresenter.onRecentViewClicked(item)
    }

    private fun `Then verify view interaction is correct for recent view`(item: BaseItemInitialStateSearch) {
        initialStateView.onClickRecentView(item)
    }

    private fun InitialStateContract.View.onClickRecentView(item: BaseItemInitialStateSearch) {
        val expectedLabel = "po: ${item.position} - applink: ${item.applink}"

        verifyOrder {
            trackEventClickRecentView(item, expectedLabel)
            route(item.applink, initialStatePresenter.getSearchParameter(), "")
            finish()
        }
    }

    @Test
    fun `Test click Product Line`() {
        `Given view already get initial state`(initialStateWithSeeMoreRecentSearch)

        val item = findDataView<InitialStateProductListDataView>().list[0]

        `When click product line`(item)
        `Then verify view interaction is correct for product line`(item)
    }

    private fun `When click product line`(item: BaseItemInitialStateSearch) {
        initialStatePresenter.onProductLineClicked(item)
    }

    private fun `Then verify view interaction is correct for product line`(item: BaseItemInitialStateSearch) {
        initialStateView.onClickProductLine(item)
    }

    private fun InitialStateContract.View.onClickProductLine(item: BaseItemInitialStateSearch) {
        val expectedLabel = "po: ${item.position} - applink: ${item.applink}"
        val userId = "0"

        verifyOrder {
            trackEventClickProductLine(item, userId, expectedLabel)
            route(item.applink, initialStatePresenter.getSearchParameter(), "")
            finish()
        }
    }

    @Test
    fun `Test click refresh popular search`() {
        `Given view already get initial state`(initialStateWithSeeMoreRecentSearch)

        val item = findDataView<PopularSearchDataView>()

        `When refresh popular search`(item)
        `Then verify view interaction is correct for refresh popular search`()
    }

    private fun `When refresh popular search`(item: PopularSearchDataView) {
        initialStatePresenter.refreshPopularSearch(item.featureId)
    }

    private fun `Then verify view interaction is correct for refresh popular search`() {
        verifyOrder {
            initialStateView.onRefreshPopularSearch()
        }
    }

    @Test
    fun `Test click refresh TokoNow popular search`() {
        `Given view already get initial state`(initialStateWithSeeMoreRecentSearch, searchParameter)

        val item = findDataView<PopularSearchDataView>()

        `When refresh popular search`(item)
        `Then verify view interaction is correct for refresh tokonow popular search`(item)
    }

    private fun `Then verify view interaction is correct for refresh tokonow popular search`(item: PopularSearchDataView) {
        verifyOrder {
            initialStateView.onRefreshTokoNowPopularSearch()
        }
    }

    @Test
    fun `Test click TokoNow Dynamic Section Search`() {
        `Given view already get initial state`(initialStateCommonResponse, searchParameter)

        val data = findDataView<DynamicInitialStateSearchDataView>()
        val item = data.list.findByType()

        `When recent dynamic section item is clicked`(item)
        `Then verify view interaction is correct for tokonow dynamic section`(item)
    }

    private fun `Then verify view interaction is correct for tokonow dynamic section`(
        item: BaseItemInitialStateSearch
    ) {
        val expectedLabel = "value: ${item.title} - po: ${item.position} - page: ${item.applink}"
        verify {
            initialStateView.trackEventClickTokoNowDynamicSectionItem(expectedLabel, item)
        }
    }

    @Test
    fun `Test click Chip Widget`() {
        `Given view already get initial state`(initialStateWithSeeMoreRecentSearch)

        val item = findDataView<InitialStateChipWidgetDataView>().list[0]

        `When click chip`(item)
        `Then verify view interaction is correct for chip widget`(item)
    }

    private fun `When click chip`(item: BaseItemInitialStateSearch) {
        initialStatePresenter.onChipClicked(item)
    }

    private fun `Then verify view interaction is correct for chip widget`(item: BaseItemInitialStateSearch) {
        initialStateView.onClickChip(item)
    }

    private fun InitialStateContract.View.onClickChip(item: BaseItemInitialStateSearch) {
        val expectedLabel = "value: ${item.title} - title: ${item.header} - po: 1"
        verifyOrder {
            trackEventClickChip(any(), expectedLabel, item, item.featureId, item.dimension90)
            route(item.applink, initialStatePresenter.getSearchParameter(), AppLogSearch.ParamValue.RECOM_SEARCH)
            finish()
        }
    }

    @Test
    fun `Test click searchbar education widget`() {
        `Given view already get initial state`(initialStateWithSearchBarEducation)

        val item = findDataView<SearchBarEducationDataView>().item

        `When click searchbar education`(item)
        `Then verify searchbar education track`(item)
    }

    private fun `When click searchbar education`(item: BaseItemInitialStateSearch) {
        initialStatePresenter.onSearchBarEducationClick(item)
    }

    private fun `Then verify searchbar education track`(item: BaseItemInitialStateSearch) {
        verify {
            initialStateView.trackEventClickSearchBarEducation(item)
            initialStateView.route(item.applink, initialStatePresenter.getSearchParameter(), "")
            initialStateView.finish()
        }
    }

}
