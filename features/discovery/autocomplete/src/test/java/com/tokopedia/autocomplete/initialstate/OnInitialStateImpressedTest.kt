package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.testinstance.initialStateCommonResponse
import com.tokopedia.autocomplete.initialstate.testinstance.initialStateEmptyDataResponse
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.junit.Test
import rx.Subscriber

internal class OnInitialStateImpressedTest: InitialStatePresenterTestFixtures() {
    @Test
    fun `test initial state impression`() {
        `given initial state use case capture request params`()
        `when presenter get initial state data`()
        `then verify initial state API is called`()
        `then verify initial state impression is called`()
        `then verify visitable list`()
    }

    private fun `given initial state use case capture request params`() {
        every { getInitialStateUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateData>>>().onStart()
            secondArg<Subscriber<List<InitialStateData>>>().onNext(initialStateCommonResponse)
        }
    }

    private fun `when presenter get initial state data`() {
        initialStatePresenter.getInitialStateData()
    }

    private fun `then verify initial state API is called`() {
        verify { getInitialStateUseCase.execute(any(), any()) }
    }

    private fun `then verify initial state impression is called`() {
        verify {
            initialStateView.onRecentViewImpressed(capture(slotRecentViewItemList))
            initialStateView.onRecentSearchImpressed(capture(slotRecentSearchItemList))
            initialStateView.onPopularSearchImpressed(capture(slotPopularSearchItemList))
        }
    }

    private fun `then verify visitable list`() {
        val recentViewItemList = slotRecentViewItemList.captured
        val recentSearchItemList = slotRecentSearchItemList.captured
        val popularSearchItemList = slotPopularSearchItemList.captured

        val recentSearchListResponse = getDataLayerForPromo(initialStateCommonResponse[0].items)
        val recentViewListResponse = getDataLayerForRecentView(initialStateCommonResponse[1].items)
        val popularSearchListResponse = getDataLayerForPromo(initialStateCommonResponse[2].items)

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
    fun `test initial state impression with empty items`() {
        `given initial state use case get empty data response`()
        `when presenter get initial state data`()
        `then verify initial state API is called`()
        `then verify initial state view will call showInitialStateResult behavior`()
        `then verify initial state view do nothing behavior`()
    }

    private fun `given initial state use case get empty data response`() {
        every { getInitialStateUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateData>>>().onStart()
            secondArg<Subscriber<List<InitialStateData>>>().onNext(initialStateEmptyDataResponse)
        }
    }

    private fun `then verify initial state view will call showInitialStateResult behavior`() {
        verify {
            initialStateView.showInitialStateResult(capture(slotVisitableList))
        }
    }

    private fun `then verify initial state view do nothing behavior`() {
        confirmVerified(initialStateView)
    }
}