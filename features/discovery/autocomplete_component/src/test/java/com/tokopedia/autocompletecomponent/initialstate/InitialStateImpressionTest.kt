package com.tokopedia.autocompletecomponent.initialstate

import com.tokopedia.autocompletecomponent.initialstate.data.InitialStateUniverse
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateItem
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateItemTrackingModel
import com.tokopedia.autocompletecomponent.jsonToObject
import com.tokopedia.autocompletecomponent.shouldBe
import io.mockk.slot
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test

private const val initialStateEmptyDataResponse = "autocomplete/initialstate/empty-response.json"
private const val initialStateWithShowMoreResponse = "autocomplete/initialstate/with-5-data-show-more-recent-search.json"

internal class InitialStateImpressionTest: InitialStatePresenterTestFixtures() {

    private val slotRecentSearchBaseItemList = slot<List<BaseItemInitialStateSearch>>()

    @Test
    fun `Test get initial state impression`() {
        `Given view already get initial state`(initialStateCommonData)

        `Then verify initial state impression is called without see more recent search`()
        `Then verify list impression data`()
    }

    private fun `Then verify initial state impression is called without see more recent search`() {
        verifyOrder {
            initialStateView.onRecentViewImpressed(any(), capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(
                capture(slotRecentSearchBaseItemList),
                capture(slotRecentSearchItemList)
            )
            initialStateView.onPopularSearchImpressed(any(), capture(slotPopularSearchTrackingModel))
            initialStateView.onDynamicSectionImpressed(any(), capture(slotDynamicSectionTrackingModel))
        }
    }

    private fun `Then verify list impression data`() {
        val recentViewItemList = slotRecentViewItemList.captured
        val recentSearchItemList = slotRecentSearchItemList.last()
        val popularSearchTrackingModel = slotPopularSearchTrackingModel.captured
        val dynamicSectionTrackingModel = slotDynamicSectionTrackingModel.captured

        val initialStateDataList = initialStateCommonData.data
        val recentViewListResponse = getDataLayerForRecentView(initialStateDataList[0].items)
        val recentSearchListResponse = getDataLayerForPromo(initialStateDataList[1].items)
        val popularSearchListResponse = getDataLayerForPromo(initialStateDataList[2].items)
        val dynamicSectionResponse = getDataLayerForPromo(initialStateDataList[3].items)

        assert(recentViewItemList.containsAll(recentViewListResponse))
        assert(recentSearchItemList.containsAll(recentSearchListResponse))
        assert(slotRecentSearchBaseItemList.captured.size == recentSearchListResponse.size)
        popularSearchTrackingModel.assertTrackerModel(popularSearchListResponse, initialStateDataList[2])
        dynamicSectionTrackingModel.assertTrackerModel(dynamicSectionResponse, initialStateDataList[3])
    }

    private fun DynamicInitialStateItemTrackingModel.assertTrackerModel(
        list: MutableList<Any>,
        initialStateData: InitialStateData,
    ) {
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
        val initialStateData = initialStateWithShowMoreResponse.jsonToObject<InitialStateUniverse>()
        `Given rollance is off`()
        `Given view already get initial state`(initialStateData)

        `Then verify initial state impression is called`()
        `Then verify list impression data`(initialStateData.data)

        `When see more recent search is clicked`()
        `Then verify recent search impression is called`()
        `Then verify recent search impressed the hidden item`(initialStateData.data)
    }

    private fun `Then verify initial state impression is called`() {
        verifyOrder {
            initialStateView.onCuratedCampaignCardImpressed(
                "0",
                capture(slotCuratedCampaignLabel),
                any(),
                capture(slotCuratedCampaignType),
                capture(slotCuratedCampaignCode)
            )
            initialStateView.onRecentViewImpressed(any(), capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(
                capture(slotRecentSearchBaseItemList),
                capture(slotRecentSearchItemList)
            )
            initialStateView.onSeeMoreRecentSearchImpressed(any())
            initialStateView.onPopularSearchImpressed(any(), capture(slotPopularSearchTrackingModel))
            initialStateView.onDynamicSectionImpressed(any(), capture(slotDynamicSectionTrackingModel))
        }
    }

    private fun `Then verify list impression data`(initialStateData: List<InitialStateData>) {
        val recentViewItemList = slotRecentViewItemList.captured
        val recentSearchItemList = slotRecentSearchItemList.last()
        val popularSearchTrackingModel = slotPopularSearchTrackingModel.captured
        val dynamicSectionTrackingModel = slotDynamicSectionTrackingModel.captured
        val curatedCampaignLabel = slotCuratedCampaignLabel.captured
        val curatedCampaignType = slotCuratedCampaignType.captured
        val curatedCampaignCode = slotCuratedCampaignCode.captured

        val recentViewListResponse = getDataLayerForRecentView(initialStateData[1].items)
        val recentSearchListResponse = getDataLayerForPromo(initialStateData[2].items.take(RECENT_SEARCH_SEE_MORE_LIMIT))
        val popularSearchListResponse = getDataLayerForPromo(initialStateData[3].items)
        val dynamicSectionResponse = getDataLayerForPromo(initialStateData[4].items)

        assert(recentViewItemList.containsAll(recentViewListResponse))
        assert(recentSearchItemList.containsAll(recentSearchListResponse))
        assert(slotRecentSearchBaseItemList.captured.size == RECENT_SEARCH_SEE_MORE_LIMIT)
        popularSearchTrackingModel.assertTrackerModel(popularSearchListResponse, initialStateData[3])
        dynamicSectionTrackingModel.assertTrackerModel(dynamicSectionResponse, initialStateData[4])

        val curatedCampaignItem = initialStateData[0].items[0]
        val expectedCuratedCampaignLabel = "${curatedCampaignItem.title} - ${curatedCampaignItem.applink}"
        assert(curatedCampaignLabel == expectedCuratedCampaignLabel) {
            "curated campaign label is \"$curatedCampaignLabel\", expected is \"$expectedCuratedCampaignLabel\""
        }
        assert(curatedCampaignType == curatedCampaignItem.type) {
            "curated campaign type is \"$curatedCampaignType\", expected is \"${curatedCampaignItem.type}\""
        }
        assert(curatedCampaignCode == curatedCampaignItem.campaignCode) {
            "curated campaign code is \"$curatedCampaignCode\", expected is \"${curatedCampaignItem.campaignCode}\""
        }
    }

    private fun `When see more recent search is clicked`() {
        initialStatePresenter.recentSearchSeeMoreClicked()
    }

    private fun `Then verify recent search impression is called`() {
        verify {
            initialStateView.onRecentSearchImpressed(any(), capture(slotRecentSearchItemList))
        }
    }

    private fun `Then verify recent search impressed the hidden item`(initialStateData: List<InitialStateData>) {
        val recentSearchItemList = slotRecentSearchItemList.last()

        val recentSearchListResponse = getDataLayerForPromo(initialStateData[2].items).takeLast(2)

        assert(recentSearchItemList.containsAll(recentSearchListResponse))
    }

    @Test
    fun `Test initial state impression with empty items`() {
        val listInitialStateData = initialStateEmptyDataResponse.jsonToObject<InitialStateUniverse>()
        `Given view already get initial state`(listInitialStateData)

        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify view interaction for load data with empty item`()
    }

    private fun `Then verify initial state view will call showInitialStateResult behavior`() {
        verify {
            initialStateView.showInitialStateResult(capture(slotVisitableList))
        }
    }
}
