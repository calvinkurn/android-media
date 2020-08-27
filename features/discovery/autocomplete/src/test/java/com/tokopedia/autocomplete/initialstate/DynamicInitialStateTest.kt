package com.tokopedia.autocomplete.initialstate

import com.tokopedia.autocomplete.initialstate.data.InitialStateUniverse
import com.tokopedia.autocomplete.initialstate.dynamic.DynamicInitialStateSearchViewModel
import com.tokopedia.autocomplete.jsonToObject
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

        `Given initial state view called showInitialStateResult behavior`()
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

        `Then verify refreshPopularSearch view behavior`()
        `Then verify visitable list after refresh dynamic initial state data`()
    }

    private fun `Then verify visitable list after refresh dynamic initial state data`() {
        val refreshVisitableList = slotRefreshVisitableList.captured
        val visitableList = slotVisitableList.captured

        Assert.assertTrue(
                (refreshVisitableList[7] as DynamicInitialStateSearchViewModel).list.size == (visitableList[7] as DynamicInitialStateSearchViewModel).list.size
        )
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

        `Given initial state view called showInitialStateResult behavior`()
        `Given refresh popular search API will return error`()
        `When presenter refresh dynamic initial state data`(ID_NEW_SECTION)
        `Then verify popularSearch API is called`()
        `Then verify initial state view do nothing behavior`()
    }

    private fun `Given refresh popular search API will return error`() {
        every { popularSearchUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<List<InitialStateItem>>>().onStart()
            secondArg<Subscriber<List<InitialStateItem>>>().onError(testException)
        }
    }
}