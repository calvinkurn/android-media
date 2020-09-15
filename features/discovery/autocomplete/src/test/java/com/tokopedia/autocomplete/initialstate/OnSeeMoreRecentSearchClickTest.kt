package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.data.InitialStateUniverse
import com.tokopedia.autocomplete.jsonToObject
import io.mockk.slot
import io.mockk.verify
import org.junit.Test

private const val initialStateWithSeeMoreRecentSearch = "autocomplete/initialstate/with-show-more-recent-search.json"

internal class OnSeeMoreRecentSearchClickTest: InitialStatePresenterTestFixtures() {

    private val initialStateData = initialStateWithSeeMoreRecentSearch.jsonToObject<InitialStateUniverse>().data

    @Test
    fun `Test click Show More Recent Search`() {

        `Given view already get initial state`(initialStateData)
        `When recent search see more clicked`()
        `Then verify renderRecentSearch is called`()
    }

    private fun `When recent search see more clicked`() {
        initialStatePresenter.recentSearchSeeMoreClicked()
    }

    private fun `Then verify renderRecentSearch is called`() {
        verify { initialStateView.renderRecentSearch() }
    }
}