package com.tokopedia.autocompletecomponent.unify

import com.tokopedia.autocompletecomponent.jsonToObject
import com.tokopedia.autocompletecomponent.unify.domain.model.InitialStateUnifyModel
import com.tokopedia.autocompletecomponent.unify.domain.model.UniverseSuggestionUnifyModel
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
        `Given Initial Use Case Is Successful`(model)
        `When Screen Is Initialized`()
        val state = viewModel.stateValue
        `Then Assert That State Result Data is Equals to Model Data`(state, model)
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
