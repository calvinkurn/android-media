package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.data.InitialStateUniverse
import com.tokopedia.autocomplete.jsonToObject
import io.mockk.slot
import io.mockk.verify
import org.junit.Test

private const val initialStateWithSeeMoreRecentSearch = "autocomplete/initialstate/with-show-more-recent-search.json"

internal class OnSeeMoreRecentSearchClickTest: InitialStatePresenterTestFixtures() {

    private val initialStateData = initialStateWithSeeMoreRecentSearch.jsonToObject<InitialStateUniverse>().data
    private val slotSeeMore = slot<Boolean>()

    @Test
    fun `Test click Show More Recent Search`() {

        `Given view already get initial state`(initialStateData)
        `When recent search see more clicked`(true)
        `Then verify renderRecentSearch is called`()
        `Then verify Recent Search See More is True`()
    }

    private fun `When recent search see more clicked`(seeMore: Boolean) {
        initialStatePresenter.recentSearchSeeMoreClicked(seeMore)
    }

    private fun `Then verify renderRecentSearch is called`() {
        verify { initialStateView.renderRecentSearch(capture(slotSeeMore)) }
    }

    private fun `Then verify Recent Search See More is True`() {
        val seeMore = slotSeeMore.captured
        assert(seeMore) {
            "seeMore should be True"
        }
    }

    @Test
    fun `Test click Show Less Recent Search`() {

        `Given view already get initial state`(initialStateData)
        `When recent search see more clicked`(false)
        `Then verify renderRecentSearch is called`()
        `Then verify Recent Search See More is False`()
    }

    private fun `Then verify Recent Search See More is False`() {
        val seeMore = slotSeeMore.captured
        assert(!seeMore) {
            "seeMore should be False"
        }
    }
}