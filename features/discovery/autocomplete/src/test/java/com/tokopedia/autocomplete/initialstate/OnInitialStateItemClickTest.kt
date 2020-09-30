package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.data.InitialStateUniverse
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchSeeMoreViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchViewModel
import com.tokopedia.autocomplete.jsonToObject
import com.tokopedia.autocomplete.shouldBe
import io.mockk.*
import org.junit.Test
import rx.Subscriber

private const val initialStateWithSeeMoreRecentSearch = "autocomplete/initialstate/with-5-data-show-more-recent-search.json"

internal class OnInitialStateItemClickTest: InitialStatePresenterTestFixtures(){

    private val keyword = "sepatu"
    private val applink = "tokopedia://search?q=$keyword&source=universe&st=product"
    private val shopId = "8384142"
    private val shopName = "MizanBookCorner"
    private val applinkShop = "tokopedia://shop/$shopId?source=universe&st=product"
    private val slotRecentSearchViewModel = slot<RecentSearchViewModel>()

    @Test
    fun `test click recent search item`() {
        val item = BaseItemInitialStateSearch(
                applink = applink,
                title = keyword
        )

        `given initial state use case capture request params`(initialStateCommonData)
        `when recent search item clicked` (item, 0)
        `then verify view interaction is correct`(item)
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

    private fun `then verify view interaction is correct`(item: BaseItemInitialStateSearch) {
        verifyOrder {
            initialStateView.onClickRecentSearch(item)
        }

        confirmVerified(initialStateView)
    }

    private fun InitialStateContract.View.onClickRecentSearch(item: BaseItemInitialStateSearch) {
        trackEventClickRecentSearch(getItemEventLabelForTracking(item, 0), 0)
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
        val item = BaseItemInitialStateSearch(
                applink = applinkShop,
                title = shopName,
                type = TYPE_SHOP,
                productId = shopId
        )

        `given initial state use case capture request params`(initialStateCommonData)
        `when recent search item clicked` (item, 0)
        `then verify view interaction is correct for recent shop`(item)
    }

    private fun `then verify view interaction is correct for recent shop`(item: BaseItemInitialStateSearch) {
        verifyOrder {
            initialStateView.onClickRecentShop(item)
        }

        confirmVerified(initialStateView)
    }

    private fun InitialStateContract.View.onClickRecentShop(item: BaseItemInitialStateSearch) {
        trackEventClickRecentShop(getRecentShopLabelForTracking(item), "0")
        route(item.applink, initialStatePresenter.getSearchParameter())
        finish()
    }

    private fun getRecentShopLabelForTracking(item: BaseItemInitialStateSearch): String {
        return item.productId + " - keyword: " + item.title
    }

    @Test
    fun `Test click Show More Recent Search`() {
        val initialStateData = initialStateWithSeeMoreRecentSearch.jsonToObject<InitialStateUniverse>().data
        `Given initial state view will call showInitialStateResult`()
        `Given view already get initial state`(initialStateData)

        `When recent search see more button is clicked`()

        `Then verify RecentSearchSeeMoreViewModel has been removed`()
        `Then verify renderRecentSearch is called`()
        `Then verify initial state show all recent search`(initialStateData)
    }

    private fun `Given initial state view will call showInitialStateResult`() {
        every { initialStateView.showInitialStateResult(capture(slotVisitableList)) } just runs
    }

    private fun `When recent search see more button is clicked`() {
        initialStatePresenter.recentSearchSeeMoreClicked()
    }

    private fun `Then verify RecentSearchSeeMoreViewModel has been removed`() {
        val recentSearchSeeMoreViewModel = slotVisitableList.captured.find { it is RecentSearchSeeMoreViewModel }
        assert(recentSearchSeeMoreViewModel == null) {
            "There should be no RecentSearchSeeMoreViewModel in visitable list"
        }
    }

    private fun `Then verify renderRecentSearch is called`() {
        verifyOrder {
            initialStateView.trackEventClickSeeMoreRecentSearch("0")
            initialStateView.dropKeyBoard()
            initialStateView.removeSeeMoreButtonAndRenderRecentSearch(capture(slotRecentSearchViewModel))
        }
    }

    private fun `Then verify initial state show all recent search`(initialStateData: List<InitialStateData>) {
        val recentSearchViewModel = slotRecentSearchViewModel.captured
        val recentSearchResponse = initialStateData.find { it.featureId == "recent_search" }

        recentSearchViewModel.list.size shouldBe recentSearchResponse?.items?.size
    }
}