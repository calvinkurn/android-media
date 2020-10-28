package com.tokopedia.autocomplete.initialstate

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
import com.tokopedia.autocomplete.shouldBe
import com.tokopedia.autocomplete.shouldBeInstanceOf
import io.mockk.every
import org.junit.Assert
import org.junit.Test
import rx.Subscriber

private const val dynamicInitialStateResponse = "autocomplete/initialstate/dynamic-initial-state-response.json"
private const val refreshDynamicInitialStateResponse = "autocomplete/initialstate/refresh-dynamic-initial-state-response.json"
private const val refreshDynamicInitialStateWithNoExactIdResponse = "autocomplete/initialstate/refresh-dynamic-initial-state-with-different-id-response.json"

internal class DynamicInitialStateTest: InitialStatePresenterTestFixtures() {

    private fun `Test Dynamic Initial State`(initialStateResponse: List<InitialStateData>, refreshedDynamicInitialStateData: List<InitialStateData>) {
        `Given view already get initial state`(initialStateResponse)

        `Given refresh popular search API will return data`(refreshedDynamicInitialStateData)
        `When presenter refresh dynamic initial state data`(ID_NEW_SECTION)
        `Then verify popularSearch API is called`()
    }

    private fun `When presenter refresh dynamic initial state data`(featureId: String) {
        initialStatePresenter.refreshDynamicSection(featureId)
    }

    @Test
    fun `Test dynamic initial state`() {
        val dynamicInitialStateData = dynamicInitialStateResponse.jsonToObject<InitialStateUniverse>().data
        val refreshedDynamicInitialStateData = refreshDynamicInitialStateResponse.jsonToObject<InitialStateUniverse>().data
        `Test Dynamic Initial State`(dynamicInitialStateData, refreshedDynamicInitialStateData)

        `Then verify refreshPopularSearch view behavior`(8)
        `Then verify visitable list after refresh dynamic initial state data`(dynamicInitialStateData, refreshedDynamicInitialStateData)
    }

    private fun `Then verify visitable list after refresh dynamic initial state data`(
            dynamicInitialStateData: List<InitialStateData>,
            refreshedDynamicInitialStateData: List<InitialStateData>
    ) {
        val visitableList = slotVisitableList.captured

        val refreshedDynamicInitialStateDataNewSection = refreshedDynamicInitialStateData.find { it.featureId == ID_NEW_SECTION }!!
        val refreshedDynamicInitialStateViewModel = visitableList[8] as DynamicInitialStateSearchViewModel

        refreshedDynamicInitialStateViewModel.verifyDynamicInitialState(refreshedDynamicInitialStateDataNewSection)

        val dynamicInitialStateTitle = visitableList[9] as DynamicInitialStateTitleViewModel
        val dynamicInitialStateViewModel = visitableList[10] as DynamicInitialStateSearchViewModel
        val dynamicInitialStateDataNewSection = dynamicInitialStateData.find { it.featureId == dynamicInitialStateTitle.featureId }!!

        dynamicInitialStateViewModel.verifyDynamicInitialState(dynamicInitialStateDataNewSection)
    }

    private fun DynamicInitialStateSearchViewModel.verifyDynamicInitialState(dynamicInitialStateData: InitialStateData) {
        list.size shouldBe dynamicInitialStateData.items.size

        list.forEachIndexed { index, dynamicInitialStateItemViewModel ->
            dynamicInitialStateItemViewModel.title shouldBe dynamicInitialStateData.items[index].title
            dynamicInitialStateItemViewModel.subtitle shouldBe dynamicInitialStateData.items[index].subtitle
        }
    }

    @Test
    fun `Test refresh dynamic initial state data with no exact id`() {
        val dynamicInitialStateData = dynamicInitialStateResponse.jsonToObject<InitialStateUniverse>().data
        val refreshedDynamicInitialStateData = refreshDynamicInitialStateWithNoExactIdResponse.jsonToObject<InitialStateUniverse>().data
        `Test Dynamic Initial State`(dynamicInitialStateData, refreshedDynamicInitialStateData)

        `Then verify refreshPopularSearch view behavior with no new refreshed data`()
    }

    @Test
    fun `Test fail to get dynamic initial state data`() {
        `Given view already get initial state`(initialStateCommonData)

        `Given refresh popular search API will return error`()
        `When presenter refresh dynamic initial state data`(ID_NEW_SECTION)
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