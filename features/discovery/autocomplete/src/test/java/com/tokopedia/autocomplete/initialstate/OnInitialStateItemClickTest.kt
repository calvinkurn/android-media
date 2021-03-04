package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.curatedcampaign.CuratedCampaignViewModel
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
            initialStateView.renderCompleteRecentSearch(capture(slotRecentSearchViewModel))
        }
    }

    private fun `Then verify initial state show all recent search`(initialStateData: List<InitialStateData>) {
        val recentSearchViewModel = slotRecentSearchViewModel.captured
        val recentSearchResponse = initialStateData.find { it.featureId == "recent_search" }

        recentSearchViewModel.list.size shouldBe recentSearchResponse?.items?.size
    }

    @Test
    fun `Test click Dynamic Section Search`() {
        val item = BaseItemInitialStateSearch(
            template = "list_double_line",
            imageUrl = "https://ecs7.tokopedia.net/img/cache/100-square/product-1/2020/5/23/21722219/21722219_a958c3c3-1599-435b-92a3-3fdcef496102_600_600",
            applink = "tokopedia://search?q=Samsung+A11&source=universe&st=product",
            url =  "/search?q=Samsung+A11&source=universe&st=product",
            title =  "Samsung A11",
            subtitle =  "914 pencarian"
        )

        `given initial state use case capture request params`(initialStateCommonData)

        `When recent dynamic section item is clicked`(item, 0)
        `Then verify view interaction is correct for dynamic section`(item)
    }

    private fun `When recent dynamic section item is clicked`(item: BaseItemInitialStateSearch, position: Int) {
        initialStatePresenter.onDynamicSectionItemClicked(item, position)
    }

    private fun `Then verify view interaction is correct for dynamic section`(item: BaseItemInitialStateSearch) {
        verifyOrder {
            initialStateView.onClickDynamicSectionItem(item)
        }

        confirmVerified(initialStateView)
    }

    private fun InitialStateContract.View.onClickDynamicSectionItem(item: BaseItemInitialStateSearch) {
        val expectedLabel = "value: ${item.title} - title: ${item.header} - po: 1"
        val userId = "0"

        trackEventClickDynamicSectionItem(userId, expectedLabel, item.featureId)
        route(item.applink, initialStatePresenter.getSearchParameter())
        finish()
    }

    @Test
    fun `Test click Curated Campaign Card`() {
        val item = CuratedCampaignViewModel(
                imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/8/24/4814934/4814934_8de36a7f-e5e3-4053-8089-1f42cdb17030_1414_1414",
                applink = "tokopedia://product/20100686",
                url = "/bgsport/bola-sepak-3",
                title = "Waktu Indonesia Belanja",
                subtitle = "Flashsale Rp50 rb & Cashback 90%"
        )
        val initialStateData = initialStateWithSeeMoreRecentSearch.jsonToObject<InitialStateUniverse>().data

        `given initial state use case capture request params`(initialStateData)

        `When recent dynamic section item is clicked`(item)
        `Then verify view interaction is correct for dynamic section`(item)
    }

    private fun `When recent dynamic section item is clicked`(item: CuratedCampaignViewModel) {
        initialStatePresenter.onCuratedCampaignCardClicked(item)
    }

    private fun `Then verify view interaction is correct for dynamic section`(item: CuratedCampaignViewModel) {
        verifyOrder {
            initialStateView.onClickCuratedCampaignCard(item)
        }

        confirmVerified(initialStateView)
    }

    private fun InitialStateContract.View.onClickCuratedCampaignCard(item: CuratedCampaignViewModel) {
        val expectedLabel = "${item.title} - ${item.applink}"
        val userId = "0"
        trackEventClickCuratedCampaignCard(userId, expectedLabel, item.type)
        route(item.applink, initialStatePresenter.getSearchParameter())
        finish()
    }
}