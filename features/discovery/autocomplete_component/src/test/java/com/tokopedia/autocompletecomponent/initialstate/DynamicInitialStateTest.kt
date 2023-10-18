package com.tokopedia.autocompletecomponent.initialstate

import com.tokopedia.autocompletecomponent.initialstate.data.InitialStateUniverse
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateData
import com.tokopedia.autocompletecomponent.initialstate.domain.InitialStateItem
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateSearchDataView
import com.tokopedia.autocompletecomponent.initialstate.dynamic.DynamicInitialStateTitleDataView
import com.tokopedia.autocompletecomponent.jsonToObject
import com.tokopedia.autocompletecomponent.shouldBe
import io.mockk.every
import org.junit.Test
import rx.Subscriber

private const val dynamicInitialStateResponse = "autocomplete/initialstate/dynamic-initial-state-response.json"
private const val refreshDynamicInitialStateResponse = "autocomplete/initialstate/refresh-dynamic-initial-state-response.json"
private const val refreshDynamicInitialStateWithNoExactIdResponse = "autocomplete/initialstate/refresh-dynamic-initial-state-with-different-id-response.json"

internal class DynamicInitialStateTest: InitialStatePresenterTestFixtures() {

    private fun `Test Dynamic Initial State`(
        initialStateUniverse: InitialStateUniverse,
        refreshedDynamicInitialStateData: List<InitialStateData>,
    ) {
        `Given view already get initial state`(initialStateUniverse)

        `Given refresh popular search API will return data`(refreshedDynamicInitialStateData)
        `When presenter refresh dynamic initial state data`(ID_NEW_SECTION)
        `Then verify popularSearch API is called`()
    }

    private fun `When presenter refresh dynamic initial state data`(featureId: String) {
        initialStatePresenter.refreshDynamicSection(featureId)
    }

    @Test
    fun `Test dynamic initial state`() {
        val dynamicInitialStateData = dynamicInitialStateResponse.jsonToObject<InitialStateUniverse>()
        val refreshedDynamicInitialStateData = refreshDynamicInitialStateResponse.jsonToObject<InitialStateUniverse>().data
        `Given rollance is off`()
        `Test Dynamic Initial State`(dynamicInitialStateData, refreshedDynamicInitialStateData)

        `Then verify refreshPopularSearch view behavior`(7)
        `Then verify visitable list after refresh dynamic initial state data`(dynamicInitialStateData, refreshedDynamicInitialStateData)
    }

    private fun `Then verify visitable list after refresh dynamic initial state data`(
        dynamicInitialStateData: InitialStateUniverse,
        refreshedDynamicInitialStateData: List<InitialStateData>
    ) {
        val visitableList = slotVisitableList.captured

        val refreshedDynamicInitialStateDataNewSection = refreshedDynamicInitialStateData.find { it.featureId == ID_NEW_SECTION }!!
        val refreshedDynamicInitialStateDataView = visitableList[8] as DynamicInitialStateSearchDataView

        refreshedDynamicInitialStateDataView.verifyDynamicInitialState(refreshedDynamicInitialStateDataNewSection)

        val dynamicInitialStateTitle = visitableList[9] as DynamicInitialStateTitleDataView
        val dynamicInitialStateDataView = visitableList[10] as DynamicInitialStateSearchDataView
        val dynamicInitialStateDataNewSection = dynamicInitialStateData.data.find {
            it.featureId == dynamicInitialStateTitle.featureId
        }!!

        dynamicInitialStateDataView.verifyDynamicInitialState(dynamicInitialStateDataNewSection)
    }

    private fun DynamicInitialStateSearchDataView.verifyDynamicInitialState(dynamicInitialStateData: InitialStateData) {
        list.size shouldBe dynamicInitialStateData.items.size

        list.forEachIndexed { index, dynamicInitialStateItemDataView ->
            dynamicInitialStateItemDataView.title shouldBe dynamicInitialStateData.items[index].title
            dynamicInitialStateItemDataView.subtitle shouldBe dynamicInitialStateData.items[index].subtitle
        }
    }

    @Test
    fun `Test refresh dynamic initial state data with no exact id`() {
        val dynamicInitialStateData = dynamicInitialStateResponse.jsonToObject<InitialStateUniverse>()
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
        `Then verify initial state view behavior for failed refresh dynamic`()
    }

    private fun `Given refresh popular search API will return error`() {
        every { refreshInitialStateUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateItem>>>().onStart()
            secondArg<Subscriber<List<InitialStateItem>>>().onError(testException)
        }
    }
}
