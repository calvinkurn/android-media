package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.data.InitialStateUniverse
import com.tokopedia.autocomplete.jsonToObject
import io.mockk.verify
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
        verify {
            initialStateView.onRecentViewImpressed(capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(capture(slotRecentSearchItemList))
            initialStateView.onPopularSearchImpressed(capture(slotPopularSearchItemList))
        }
    }

    private fun `Then verify list impression data`() {
        val recentViewItemList = slotRecentViewItemList.captured
        val recentSearchItemList = slotRecentSearchItemList.captured
        val popularSearchItemList = slotPopularSearchItemList.captured

        val recentViewListResponse = getDataLayerForRecentView(initialStateCommonData[0].items)
        val recentSearchListResponse = getDataLayerForPromo(initialStateCommonData[1].items)
        val popularSearchListResponse = getDataLayerForPromo(initialStateCommonData[2].items)

        assert(recentViewItemList.containsAll(recentViewListResponse))
        assert(recentSearchItemList.containsAll(recentSearchListResponse))
        assert(popularSearchItemList.containsAll(popularSearchListResponse))
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

        `Then verify initial state impression is called`()
        `Then verify list impression data`(initialStateData)

        `When see more recent search is clicked`()
        `Then verify recent search impression is called`()
        `Then verify recent search impressed the hidden item`(initialStateData)
    }

    private fun `Then verify initial state impression is called`() {
        verify {
            initialStateView.onRecentViewImpressed(capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(capture(slotRecentSearchItemList))
            initialStateView.onSeeMoreRecentSearchImpressed(any())
            initialStateView.onPopularSearchImpressed(capture(slotPopularSearchItemList))
        }
    }

    private fun `Then verify list impression data`(initialStateData: List<InitialStateData>) {
        val recentViewItemList = slotRecentViewItemList.captured
        val recentSearchItemList = slotRecentSearchItemList.captured
        val popularSearchItemList = slotPopularSearchItemList.captured

        val recentViewListResponse = getDataLayerForRecentView(initialStateData[0].items)
        val recentSearchListResponse = getDataLayerForPromo(initialStateData[1].items.take(3))
        val popularSearchListResponse = getDataLayerForPromo(initialStateData[2].items)

        assert(recentViewItemList.containsAll(recentViewListResponse))
        assert(recentSearchItemList.containsAll(recentSearchListResponse))
        assert(popularSearchItemList.containsAll(popularSearchListResponse))
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

        val recentSearchListResponse = getDataLayerForPromo(initialStateData[1].items).takeLast(2)

        assert(recentSearchItemList.containsAll(recentSearchListResponse))
    }

    @Test
    fun `Test initial state impression with empty items`() {
        val listInitialStateData = initialStateEmptyDataResponse.jsonToObject<InitialStateUniverse>().data
        `Given view already get initial state`(listInitialStateData)

        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify initial state view do nothing behavior`()
    }

    private fun `Then verify initial state view will call showInitialStateResult behavior`() {
        verify {
            initialStateView.showInitialStateResult(capture(slotVisitableList))
        }
    }
}