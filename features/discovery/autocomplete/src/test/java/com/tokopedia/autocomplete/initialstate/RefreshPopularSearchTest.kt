package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.data.InitialStateUniverse
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchTitleDataView
import com.tokopedia.autocomplete.initialstate.popularsearch.PopularSearchDataView
import com.tokopedia.autocomplete.jsonToObject
import com.tokopedia.autocomplete.shouldBe
import io.mockk.every
import org.junit.Test
import rx.Subscriber

private const val multiplePopularResponse = "autocomplete/initialstate/multiple-popular-search.json"
private const val popularSearchCommonResponse = "autocomplete/initialstate/refresh-popular-search-response.json"
private const val popularSearchNoExactIdResponse = "autocomplete/initialstate/refresh-with-no-popular-search-id-response.json"

internal class RefreshPopularSearchTest: InitialStatePresenterTestFixtures() {

    private fun `Test Refresh Popular Search`(initialStateResponse: List<InitialStateData>, refreshPopularSearchResponse: List<InitialStateData>) {
        `Given view already get initial state`(initialStateResponse)

        `Given refresh popular search API will return data`(refreshPopularSearchResponse)
        `When presenter refresh popular search`(ID_POPULAR_SEARCH)
        `Then verify popularSearch API is called`()
    }

    private fun `When presenter refresh popular search`(featureId: String) {
        initialStatePresenter.refreshPopularSearch(featureId)
    }

    @Test
    fun `Test refresh popular search data`() {
        val multiplePopularSearchData = multiplePopularResponse.jsonToObject<InitialStateUniverse>().data
        val refreshedPopularSearchData = popularSearchCommonResponse.jsonToObject<InitialStateUniverse>().data
        `Test Refresh Popular Search`(multiplePopularSearchData, refreshedPopularSearchData)

        `Then verify refreshPopularSearch view behavior`(4)
        `Then verify visitable list after refresh dynamic initial state data`(multiplePopularSearchData, refreshedPopularSearchData)
    }

    private fun `Then verify visitable list after refresh dynamic initial state data`(
            initialStateData: List<InitialStateData>,
            refreshedInitialStateData: List<InitialStateData>
    ) {
        val visitableList = slotVisitableList.captured

        val refreshedPopularSearchNewSection = refreshedInitialStateData.find { it.featureId == ID_POPULAR_SEARCH }!!
        val refreshedPopularSearchDataView = visitableList[5] as PopularSearchDataView

        refreshedPopularSearchDataView.verifyPopularSearch(refreshedPopularSearchNewSection)

        val popularSearchTitle = visitableList[6] as PopularSearchTitleDataView
        val popularSearchDataView = visitableList[7] as PopularSearchDataView
        val popularSearchData = initialStateData.find { it.featureId == popularSearchTitle.featureId }!!

        popularSearchDataView.verifyPopularSearch(popularSearchData)
    }

    private fun PopularSearchDataView.verifyPopularSearch(dynamicInitialStateData: InitialStateData) {
        list.size shouldBe dynamicInitialStateData.items.size

        list.forEachIndexed { index, dynamicInitialStateItemDataView ->
            dynamicInitialStateItemDataView.title shouldBe dynamicInitialStateData.items[index].title
            dynamicInitialStateItemDataView.subtitle shouldBe dynamicInitialStateData.items[index].subtitle
        }
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

        `Given refresh popular search API will return error`()
        `When presenter refresh popular search`(ID_POPULAR_SEARCH)
        `Then verify popularSearch API is called`()
        `Then verify initial state view behavior is correct`()
    }

    private fun `Given refresh popular search API will return error`() {
        every { refreshInitialStateUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateItem>>>().onStart()
            secondArg<Subscriber<List<InitialStateItem>>>().onError(testException)
        }
    }
}