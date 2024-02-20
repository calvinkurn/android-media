package com.tokopedia.autocompletecomponent.unify

import com.tokopedia.autocompletecomponent.jsonToObject
import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnifyModel
import com.tokopedia.autocompletecomponent.unify.domain.model.UniverseSuggestionUnifyModel
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.usecase.RequestParams
import io.mockk.CapturingSlot
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class AutoCompleteSuggestionTest : AutoCompleteTestFixtures() {
    private val mpsParameters = mapOf(
        SearchApiConst.Q1 to "samsung",
        SearchApiConst.Q2 to "xiaomi",
        SearchApiConst.Q3 to "iphone",
        SearchApiConst.Q to "samsung"
    )

    private val parameters = mapOf(
        SearchApiConst.Q to "samsung"
    )

    @Test
    fun `on screen initialized and query is not empty should call suggestion state query with parameter`() {
        val viewModel = autoCompleteViewModel(AutoCompleteState(parameters))
        val requestParamsSlot = slot<RequestParams>()
        `Given Suggestion Use Case Is Successful`(
            UniverseSuggestionUnifyModel(),
            requestParamsSlot = requestParamsSlot
        )

        `When Screen is Initialized`(viewModel)
        parameters.forEach {
            assertThat(requestParamsSlot.captured.parameters[it.key], `is`(it.value))
        }
    }

    private fun `Then Verify Use Case is Called with correct parameter`(
        requestParamsSlot: CapturingSlot<RequestParams>,
        params: Map<String, String>
    ) {
        verify { suggestionStateUseCase.execute(any(), any(), any()) }

        assertThat(requestParamsSlot.captured.parameters, `is`(params))
    }

    private fun `When Screen is Initialized`(
        viewModel: AutoCompleteViewModel
    ) {
        viewModel.onScreenInitialized()
    }

    @Test
    fun `on suggestion should show screen data`() {
        val viewModel = autoCompleteViewModel(AutoCompleteState(parameters))
        val model = AutoCompleteSuggestionSuccessJSON.jsonToObject<SuggestionUnifyModel>()
        `Given Suggestion Use Case Is Successful`(model.data)
        `When Screen is Initialized`(viewModel)
        val state = viewModel.stateValue
        `Then Assert State Result List is Similar to Model Result`(model.data, state)
    }

    private fun `Then Assert State Result List is Similar to Model Result`(
        model: UniverseSuggestionUnifyModel,
        state: AutoCompleteState
    ) {
        model.data.forEachIndexed { index, suggestionUnify ->
            val dataView = state.resultList[index]
            assertThat(
                dataView,
                instanceOf(AutoCompleteUnifyDataView::class.java)
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
