package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.curatedcampaign.CuratedCampaignDataView
import com.tokopedia.autocomplete.initialstate.data.InitialStateUniverse
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateSearchDataView
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateTitleDataView
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleDataView
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchDataView
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchSeeMoreDataView
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchTitleDataView
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchDataView
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewTitleDataView
import com.tokopedia.autocomplete.initialstate.recentview.RecentViewDataView
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

        Assert.assertTrue(visitableList[0] is RecentViewTitleDataView)
        Assert.assertTrue(visitableList[1] is RecentViewDataView)
        Assert.assertTrue(visitableList[2] is RecentSearchTitleDataView)
        Assert.assertTrue(visitableList[3] is RecentSearchDataView)
        Assert.assertTrue(visitableList[4] is PopularSearchTitleDataView)
        Assert.assertTrue(visitableList[5] is PopularSearchDataView)
        Assert.assertTrue(visitableList[6] is DynamicInitialStateTitleDataView)
        Assert.assertTrue(visitableList[7] is DynamicInitialStateSearchDataView)
        Assert.assertTrue(visitableList.size == 8)

        `Then verify RecentSearchDataView only have n items`(3, visitableList[3] as RecentSearchDataView)
    }

    private fun `Then verify RecentSearchDataView only have n items`(expectedRecentSearchSize: Int, dataView: RecentSearchDataView) {
        assert(dataView.list.size == expectedRecentSearchSize) {
            "RecentSearchDataView should only have $expectedRecentSearchSize items, actual size is ${dataView.list.size}"
        }
    }

    @Test
    fun `Test get initial state data with see more button`() {
        val initialStateData = initialStateWithSeeMoreRecentSearch.jsonToObject<InitialStateUniverse>().data
        `Test Initial State Data`(initialStateData)

        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify visitable list has RecentSearchSeeMoreDataView`()
    }

    private fun `Then verify visitable list has RecentSearchSeeMoreDataView`() {
        val visitableList = slotVisitableList.captured

        Assert.assertTrue(visitableList[0] is CuratedCampaignDataView)
        Assert.assertTrue(visitableList[1] is RecentViewTitleDataView)
        Assert.assertTrue(visitableList[2] is RecentViewDataView)
        Assert.assertTrue(visitableList[3] is RecentSearchTitleDataView)
        Assert.assertTrue(visitableList[4] is RecentSearchDataView)
        Assert.assertTrue(visitableList[5] is RecentSearchSeeMoreDataView)
        Assert.assertTrue(visitableList[6] is PopularSearchTitleDataView)
        Assert.assertTrue(visitableList[7] is PopularSearchDataView)
        Assert.assertTrue(visitableList[8] is DynamicInitialStateTitleDataView)
        Assert.assertTrue(visitableList[9] is DynamicInitialStateSearchDataView)
        /*
            Backend not ready, will be implemented after the new field is already in production.
            Estimated release on 3.124
        */
//        Assert.assertTrue(visitableList[10] is InitialStateProductLineTitleDataView)
//        Assert.assertTrue(visitableList[11] is InitialStateProductListDataView)
        Assert.assertTrue(visitableList.size == 10)

        `Then verify RecentSearchDataView only have n items`(3, visitableList[4] as RecentSearchDataView)
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