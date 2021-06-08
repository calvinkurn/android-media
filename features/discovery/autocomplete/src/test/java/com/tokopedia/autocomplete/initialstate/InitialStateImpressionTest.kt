package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.curatedcampaign.CuratedCampaignDataView
import com.tokopedia.autocomplete.initialstate.data.InitialStateUniverse
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateItemTrackingModel
import com.tokopedia.autocomplete.jsonToObject
import com.tokopedia.autocomplete.shouldBe
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

private const val initialStateEmptyDataResponse = "autocomplete/initialstate/empty-response.json"
private const val initialStateWithShowMoreResponse = "autocomplete/initialstate/with-5-data-show-more-recent-search.json"

internal class InitialStateImpressionTest: InitialStatePresenterTestFixtures() {

    @Test
    fun `Test get initial state impression`() {
        `Given view already get initial state`(initialStateCommonData)

        `Then verify initial state impression is called without see more recent search`()
        `Then verify list impression data`()
    }

    private fun `Then verify initial state impression is called without see more recent search`() {
        verifyOrder {
            initialStateView.onRecentViewImpressed(capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(capture(slotRecentSearchItemList))
            initialStateView.onPopularSearchImpressed(capture(slotPopularSearchTrackingModel))
            initialStateView.onDynamicSectionImpressed(capture(slotDynamicSectionTrackingModel))
        }
    }

    private fun `Then verify list impression data`() {
        val recentViewItemList = slotRecentViewItemList.captured
        val recentSearchItemList = slotRecentSearchItemList.captured
        val popularSearchTrackingModel = slotPopularSearchTrackingModel.captured
        val dynamicSectionTrackingModel = slotDynamicSectionTrackingModel.captured

        val recentViewListResponse = getDataLayerForRecentView(initialStateCommonData[0].items)
        val recentSearchListResponse = getDataLayerForPromo(initialStateCommonData[1].items)
        val popularSearchListResponse = getDataLayerForPromo(initialStateCommonData[2].items)
        val dynamicSectionResponse = getDataLayerForPromo(initialStateCommonData[3].items)

        assert(recentViewItemList.containsAll(recentViewListResponse))
        assert(recentSearchItemList.containsAll(recentSearchListResponse))
        popularSearchTrackingModel.assertTrackerModel(popularSearchListResponse, initialStateCommonData[2])
        dynamicSectionTrackingModel.assertTrackerModel(dynamicSectionResponse, initialStateCommonData[3])
    }

    private fun DynamicInitialStateItemTrackingModel.assertTrackerModel(list: MutableList<Any>, initialStateData: InitialStateData) {
        this.list shouldBe list
        this.title shouldBe initialStateData.header
        this.type shouldBe initialStateData.featureId
        this.userId shouldBe "0"
    }

    private fun getDataLayerForRecentView(list: List<InitialStateItem>): MutableList<Any> {
        val dataLayerList: MutableList<Any> = mutableListOf()

        list.forEachIndexed { index, item ->
            val position = index + 1
            dataLayerList.add(item.getObjectDataLayerForRecentView(position))
        }
        return dataLayerList
    }

    private fun getDataLayerForPromo(list: List<InitialStateItem>): MutableList<Any> {
        val dataLayerList: MutableList<Any> = mutableListOf()

        list.forEachIndexed { index, item ->
            val position = index + 1
            dataLayerList.add(item.getObjectDataLayerForPromo(position))
        }
        return dataLayerList
    }

    @Test
    fun `Test get initial state impression with see more`() {
        val initialStateData = initialStateWithShowMoreResponse.jsonToObject<InitialStateUniverse>().data
        `Given view already get initial state`(initialStateData)

        val item = CuratedCampaignDataView(
                imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2020/8/24/4814934/4814934_8de36a7f-e5e3-4053-8089-1f42cdb17030_1414_1414",
                applink = "tokopedia://product/20100686",
                url = "/bgsport/bola-sepak-3",
                title = "Waktu Indonesia Belanja",
                subtitle = "Flashsale Rp50 rb & Cashback 90%"
        )
        `Then verify initial state impression is called`(item)
        `Then verify list impression data`(initialStateData)

        `When see more recent search is clicked`()
        `Then verify recent search impression is called`()
        `Then verify recent search impressed the hidden item`(initialStateData)
    }

    private fun `Then verify initial state impression is called`(curatedCampaignDataView: CuratedCampaignDataView) {
        verifyOrder {
            initialStateView.onCuratedCampaignCardImpressed(curatedCampaignDataView)
            initialStateView.onRecentViewImpressed(capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(capture(slotRecentSearchItemList))
            initialStateView.onSeeMoreRecentSearchImpressed(any())
            initialStateView.onPopularSearchImpressed(capture(slotPopularSearchTrackingModel))
            initialStateView.onDynamicSectionImpressed(capture(slotDynamicSectionTrackingModel))
        }
    }

    private fun InitialStateContract.View.onCuratedCampaignCardImpressed(curatedCampaignDataView: CuratedCampaignDataView) {
        val expectedLabel = "${curatedCampaignDataView.title} - ${curatedCampaignDataView.applink}"
        val userId = "0"
        onCuratedCampaignCardImpressed(userId, expectedLabel, curatedCampaignDataView.type)
    }

    private fun `Then verify list impression data`(initialStateData: List<InitialStateData>) {
        val recentViewItemList = slotRecentViewItemList.captured
        val recentSearchItemList = slotRecentSearchItemList.captured
        val popularSearchTrackingModel = slotPopularSearchTrackingModel.captured
        val dynamicSectionTrackingModel = slotDynamicSectionTrackingModel.captured

        val recentViewListResponse = getDataLayerForRecentView(initialStateData[1].items)
        val recentSearchListResponse = getDataLayerForPromo(initialStateData[2].items.take(3))
        val popularSearchListResponse = getDataLayerForPromo(initialStateData[3].items)
        val dynamicSectionResponse = getDataLayerForPromo(initialStateCommonData[3].items)

        assert(recentViewItemList.containsAll(recentViewListResponse))
        assert(recentSearchItemList.containsAll(recentSearchListResponse))
        popularSearchTrackingModel.assertTrackerModel(popularSearchListResponse, initialStateCommonData[2])
        dynamicSectionTrackingModel.assertTrackerModel(dynamicSectionResponse, initialStateCommonData[3])
    }

    private fun `When see more recent search is clicked`() {
        initialStatePresenter.recentSearchSeeMoreClicked()
    }

    private fun `Then verify recent search impression is called`() {
        verify {
            initialStateView.onRecentSearchImpressed(capture(slotRecentSearchItemList))
        }
    }

    private fun `Then verify recent search impressed the hidden item`(initialStateData: List<InitialStateData>) {
        val recentSearchItemList = slotRecentSearchItemList.captured

        val recentSearchListResponse = getDataLayerForPromo(initialStateData[2].items).takeLast(2)

        assert(recentSearchItemList.containsAll(recentSearchListResponse))
    }

    @Test
    fun `Test initial state impression with empty items`() {
        val listInitialStateData = initialStateEmptyDataResponse.jsonToObject<InitialStateUniverse>().data
        `Given view already get initial state`(listInitialStateData)

        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify view interaction for load data failed with exception`()
    }

    private fun `Then verify initial state view will call showInitialStateResult behavior`() {
        verify {
            initialStateView.showInitialStateResult(capture(slotVisitableList))
        }
    }
}