package com.tokopedia.autocompletecomponent.unify

import com.tokopedia.autocompletecomponent.jsonToObject
import com.tokopedia.autocompletecomponent.unify.domain.model.InitialStateUnifyModel
import com.tokopedia.autocompletecomponent.unify.domain.model.UniverseSuggestionUnifyModel
import com.tokopedia.usecase.RequestParams
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test

class AutoCompleteInitialTest : AutoCompleteTestFixtures() {
    private lateinit var viewModel: AutoCompleteViewModel

    @Before
    fun setup() {
        viewModel = autoCompleteViewModel()
    }

    @Test
    fun `on screen initialized and query is empty should call initial state query`() {
        `When Screen Is Initialized`()
        `Then Verify Initial State Use Case Is Called`()
    }

    private fun `When Screen Is Initialized`() {
        viewModel.onScreenInitialized()
    }

    private fun `Then Verify Initial State Use Case Is Called`() {
        verify { initialStateUseCase.execute(any(), any(), any()) }
    }

    @Test
    fun `on initial state successful should show success data`() {
        val model =
            AutoCompleteInitialStateSuccessJSON.jsonToObject<InitialStateUnifyModel>().data
        val requestParamsSlot = slot<RequestParams>()
        `Given Initial Use Case Is Successful`(model, requestParamsSlot)
        `When Screen Is Initialized`()
        val state = viewModel.stateValue
        `Then Assert That State Result Data is Equals to Model Data`(state, model)
        val requestParams = requestParamsSlot.captured
        `Then assert parameter is typing should not be true`(requestParams)
    }

    private fun `Then assert parameter is typing should not be true`(requestParams: RequestParams) {
        assert(!requestParams.getBoolean("is_typing", false))
    }

    private fun `Then Assert That State Result Data is Equals to Model Data`(
        state: AutoCompleteState,
        model: UniverseSuggestionUnifyModel
    ) {
        model.data.forEachIndexed { index, suggestionUnify ->
            val dataView = state.resultList[index]
            assertThat(
                dataView,
                CoreMatchers.instanceOf(AutoCompleteUnifyDataView::class.java)
            )
            assertThat(
                state.resultList[index].domainModel.suggestionId,
                `is`(suggestionUnify.suggestionId)
            )
            assertThat(
                state.resultList[index].domainModel.title,
                `is`(suggestionUnify.title)
            )
        }
        assertThat(state.resultList.size, `is`(model.data.size))
    }
}
