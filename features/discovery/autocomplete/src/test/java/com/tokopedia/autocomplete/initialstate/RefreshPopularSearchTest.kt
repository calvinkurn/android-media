package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.data.InitialStateUniverse
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchViewModel
import com.tokopedia.autocomplete.jsonToObject
import io.mockk.every
import org.junit.Assert
import org.junit.Test
import rx.Subscriber

private const val popularSearchCommonResponse = "autocomplete/initialstate/refresh-popular-search-response.json"
private const val popularSearchNoExactIdResponse = "autocomplete/initialstate/refresh-with-no-popular-search-id-response.json"

internal class RefreshPopularSearchTest: InitialStatePresenterTestFixtures() {

    private fun `Test Refresh Popular Search`(initialStateResponse: List<InitialStateData>, refreshPopularSearchResponse: List<InitialStateData>) {
        `Given view already get initial state`(initialStateResponse)

        `Given initial state view called showInitialStateResult behavior`()
        `Given refresh popular search API will return data`(refreshPopularSearchResponse)
        `When presenter refresh popular search`(ID_POPULAR_SEARCH)
        `Then verify popularSearch API is called`()
    }

    private fun `When presenter refresh popular search`(featureId: String) {
        initialStatePresenter.refreshPopularSearch(featureId)
    }

    @Test
    fun `Test refresh popular search data`() {
        val refreshedPopularSearchData = popularSearchCommonResponse.jsonToObject<InitialStateUniverse>().data
        `Test Refresh Popular Search`(initialStateCommonData, refreshedPopularSearchData)

        `Then verify refreshPopularSearch view behavior`()
        `Then verify visitable list after refresh popular search`()
    }

    private fun `Then verify visitable list after refresh popular search`() {
        val refreshVisitableList = slotRefreshVisitableList.captured
        val visitableList = slotVisitableList.captured

        Assert.assertTrue(
                (refreshVisitableList[5] as PopularSearchViewModel).list.size == (visitableList[5] as PopularSearchViewModel).list.size
        )
    }

    @Test
    fun `Test refresh popular search with no exact id`() {
        val refreshedPopularSearchData = popularSearchNoExactIdResponse.jsonToObject<InitialStateUniverse>().data
        `Test Refresh Popular Search`(initialStateCommonData, refreshedPopularSearchData)

        `Then verify refreshPopularSearch view behavior with no new refreshed data`()
    }

    @Test
    fun `Test fail to get refresh popular search data`() {
        `Given view already get initial state`(initialStateCommonData)

        `Given initial state view called showInitialStateResult behavior`()
        `Given refresh popular search API will return error`()
        `When presenter refresh popular search`(ID_POPULAR_SEARCH)
        `Then verify popularSearch API is called`()
        `Then verify initial state view do nothing behavior`()
    }

    private fun `Given refresh popular search API will return error`() {
        every { refreshInitialStateUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateItem>>>().onStart()
            secondArg<Subscriber<List<InitialStateItem>>>().onError(testException)
        }
    }
}