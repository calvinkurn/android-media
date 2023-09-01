package com.tokopedia.autocompletecomponent.initialstate

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.autocompletecomponent.initialstate.data.InitialStateUniverse
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchSeeMoreDataView
import com.tokopedia.autocompletecomponent.initialstate.recentsearch.RecentSearchTitleDataView
import com.tokopedia.autocompletecomponent.initialstate.recentview.RecentViewTitleDataView
import com.tokopedia.autocompletecomponent.jsonToObject
import com.tokopedia.autocompletecomponent.shouldBe
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchConstant
import io.mockk.verify
import org.junit.Test

private const val initialStateWithoutHeader = "autocomplete/initialstate/without-header-reimagine.json"

internal class InitialStateSearchReimagineTest: InitialStatePresenterTestFixtures() {

    @Test
    fun `Test initial state has no recent header`() {
        val initialStateData = initialStateWithoutHeader.jsonToObject<InitialStateUniverse>()
        `Given rollance is on`()
        `Test Initial State Data`(initialStateData)
        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify visitable list has no recent search title`()
    }

    @Test
    fun `Test initial state has no see more`() {
        val initialStateData = initialStateWithoutHeader.jsonToObject<InitialStateUniverse>()
        `Given rollance is on`()
        `Test Initial State Data`(initialStateData)
        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify RecentSearchSeeMoreDataView has been removed`()
    }

    private fun `Then verify visitable list has no recent search title`() {
        val visitableList = slotVisitableList.captured
        `Then verify no RecentSearchTitleDataView`(visitableList)
    }

    private fun `Then verify no RecentSearchTitleDataView`(
        actualData: List<Visitable<*>>,
    ) {
        actualData.none { it is RecentSearchTitleDataView } shouldBe true
    }

    private fun `Test Initial State Data`(
        initialStateUniverse: InitialStateUniverse,
        searchParameter: Map<String, String> = mapOf(
            SearchApiConst.Q to keyword
        ),
    ) {
        `Given initial state use case will be successful`(initialStateUniverse)
        `When presenter get initial state data`(searchParameter)
        `Then verify initial state API is called`()
    }

    private fun `Then verify initial state view will call showInitialStateResult behavior`() {
        verify {
            initialStateView.showInitialStateResult(capture(slotVisitableList))
        }
    }

    private fun `When presenter get initial state data`(
        searchParameter: Map<String, String> = mapOf(
            SearchApiConst.Q to keyword
        ),
    ) {
        initialStatePresenter.showInitialState(searchParameter)
    }

    private fun `Then verify initial state API is called`() {
        verify { getInitialStateUseCase.execute(any(), any()) }
    }


    private fun `Then verify RecentSearchSeeMoreDataView has been removed`() {
        val recentSearchSeeMoreDataView = slotVisitableList.captured.find { it is RecentSearchSeeMoreDataView }
        assert(recentSearchSeeMoreDataView == null) {
            "There should be no RecentSearchSeeMoreDataView in visitable list"
        }
    }

}
