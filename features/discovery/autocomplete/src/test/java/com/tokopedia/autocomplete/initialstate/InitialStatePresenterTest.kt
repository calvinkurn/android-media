package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.curatedcampaign.CuratedCampaignViewModel
import com.tokopedia.autocomplete.initialstate.data.InitialStateUniverse
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateSearchViewModel
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchSeeMoreViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchViewModel
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewTitleViewModel
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewViewModel
import com.tokopedia.autocomplete.jsonToObject
import io.mockk.every
import io.mockk.verify
import org.junit.Assert
import org.junit.Test
import rx.Subscriber

private const val initialStateWithSeeMoreRecentSearch = "autocomplete/initialstate/with-5-data-show-more-recent-search.json"

internal class InitialStatePresenterTest: InitialStatePresenterTestFixtures() {

    private fun `Test Initial State Data`(list: List<InitialStateData>) {
        `Given initial state use case capture request params`(list)
        `When presenter get initial state data`()
        `Then verify initial state API is called`()
    }

    private fun `When presenter get initial state data`() {
        initialStatePresenter.getInitialStateData()
    }

    private fun `Then verify initial state API is called`() {
        verify { getInitialStateUseCase.execute(any(), any()) }
    }

    @Test
    fun `Test get initial state data without see more button`() {
        `Test Initial State Data`(initialStateCommonData)

        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify visitable list`()
    }

    private fun `Then verify initial state view will call showInitialStateResult behavior`() {
        verify {
            initialStateView.showInitialStateResult(capture(slotVisitableList))
        }
    }

    private fun `Then verify visitable list`() {
        val visitableList = slotVisitableList.captured

        Assert.assertTrue(visitableList[0] is RecentViewTitleViewModel)
        Assert.assertTrue(visitableList[1] is RecentViewViewModel)
        Assert.assertTrue(visitableList[2] is RecentSearchTitleViewModel)
        Assert.assertTrue(visitableList[3] is RecentSearchViewModel)
        Assert.assertTrue(visitableList[4] is PopularSearchTitleViewModel)
        Assert.assertTrue(visitableList[5] is PopularSearchViewModel)
        Assert.assertTrue(visitableList[6] is DynamicInitialStateTitleViewModel)
        Assert.assertTrue(visitableList[7] is DynamicInitialStateSearchViewModel)
        Assert.assertTrue(visitableList.size == 8)

        `Then verify RecentSearchViewModel only have n items`(3, visitableList[3] as RecentSearchViewModel)
    }

    private fun `Then verify RecentSearchViewModel only have n items`(expectedRecentSearchSize: Int, viewModel: RecentSearchViewModel) {
        assert(viewModel.list.size == expectedRecentSearchSize) {
            "RecentSearchViewModel should only have $expectedRecentSearchSize items, actual size is ${viewModel.list.size}"
        }
    }

    @Test
    fun `Test get initial state data with see more button`() {
        val initialStateData = initialStateWithSeeMoreRecentSearch.jsonToObject<InitialStateUniverse>().data
        `Test Initial State Data`(initialStateData)

        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify visitable list has SeeMoreViewModel`()
    }

    private fun `Then verify visitable list has SeeMoreViewModel`() {
        val visitableList = slotVisitableList.captured

        Assert.assertTrue(visitableList[0] is CuratedCampaignViewModel)
        Assert.assertTrue(visitableList[1] is RecentViewTitleViewModel)
        Assert.assertTrue(visitableList[2] is RecentViewViewModel)
        Assert.assertTrue(visitableList[3] is RecentSearchTitleViewModel)
        Assert.assertTrue(visitableList[4] is RecentSearchViewModel)
        Assert.assertTrue(visitableList[5] is RecentSearchSeeMoreViewModel)
        Assert.assertTrue(visitableList[6] is PopularSearchTitleViewModel)
        Assert.assertTrue(visitableList[7] is PopularSearchViewModel)
        Assert.assertTrue(visitableList[8] is DynamicInitialStateTitleViewModel)
        Assert.assertTrue(visitableList[9] is DynamicInitialStateSearchViewModel)
        Assert.assertTrue(visitableList.size == 10)

        `Then verify RecentSearchViewModel only have n items`(3, visitableList[4] as RecentSearchViewModel)
    }

    @Test
    fun `Test fail to get initial state data`() {
        `Given initial state API will return error`()
        `When presenter get initial state data`()
        `Then verify initial state API is called`()
        `Then verify initial state view do nothing behavior`()
    }

    private fun `Given initial state API will return error`() {
        every { getInitialStateUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateData>>>().onStart()
            secondArg<Subscriber<List<InitialStateData>>>().onError(testException)
        }
    }
}