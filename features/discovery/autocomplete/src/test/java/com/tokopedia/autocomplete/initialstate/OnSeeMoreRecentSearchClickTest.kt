package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.data.InitialStateUniverse
import com.tokopedia.autocomplete.initialstate.recentsearch.RecentSearchViewModel
import com.tokopedia.autocomplete.jsonToObject
import io.mockk.slot
import io.mockk.verify
import org.junit.Test

private const val initialStateWithSeeMoreRecentSearch = "autocomplete/initialstate/with-show-more-recent-search.json"

internal class OnSeeMoreRecentSearchClickTest: InitialStatePresenterTestFixtures() {

    private val initialStateData = initialStateWithSeeMoreRecentSearch.jsonToObject<InitialStateUniverse>().data
    private val slotRecentSearchViewModel = slot<RecentSearchViewModel>()

    @Test
    fun `Test click Show More Recent Search`() {
        `Given view already get initial state`(initialStateData)
        `Then verify initial state view will call showInitialStateResult behavior`()
        `Then verify RecentSearchViewModel size is 3 and has RecentSearchSeeMoreViewModel`()

        `When recent search see more button is clicked`()
        `Then verify renderRecentSearch is called`()
        `Then verify recent search data shown increased from 3 to 5`()
    }

    private fun `Then verify initial state view will call showInitialStateResult behavior`() {
        verify {
            initialStateView.showInitialStateResult(capture(slotVisitableList))
        }
    }

    private fun `Then verify RecentSearchViewModel size is 3 and has RecentSearchSeeMoreViewModel`() {
        val recentSearchVisitable = slotVisitableList.captured.find { it is RecentSearchViewModel } as RecentSearchViewModel
        assert(recentSearchVisitable.list.size == 3) {
            "Actual size is ${recentSearchVisitable.list.size}, Expected size is 3"
        }
    }

    private fun `When recent search see more button is clicked`() {
        initialStatePresenter.recentSearchSeeMoreClicked()
    }

    private fun `Then verify renderRecentSearch is called`() {
        verify {
            initialStateView.dropKeyBoard()
            initialStateView.renderRecentSearch(capture(slotRecentSearchViewModel))
        }
    }

    private fun `Then verify recent search data shown increased from 3 to 5`() {
        val recentSearchViewModel = slotRecentSearchViewModel.captured
        assert(recentSearchViewModel.list.size == 5) {
            "Actual size is ${recentSearchViewModel.list.size}, Expected size is 5"
        }
    }
}