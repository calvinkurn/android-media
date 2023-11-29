package com.tokopedia.autocompletecomponent.unify

import com.tokopedia.discovery.common.constants.SearchApiConst
import io.mockk.verify
import org.junit.Test

class AutoCompleteParameterUpdateTest : AutoCompleteTestFixtures() {

    @Test
    fun `screen has changed parameter from empty to filled, should call suggestion`() {
        val updatedParameter = mapOf(SearchApiConst.Q to "samsung")
        val viewModel = autoCompleteViewModel()
        `When Screen Parameter is Updated`(viewModel, updatedParameter)
        `Then Verify That Suggestion Usecase is called once`()
    }

    private fun `Then Verify That Suggestion Usecase is called once`() {
        verify(exactly = 1) { suggestionStateUseCase.execute(any(), any(), any()) }
    }

    private fun `When Screen Parameter is Updated`(
        viewModel: AutoCompleteViewModel,
        updatedParameter: Map<String, String>
    ) {
        viewModel.onScreenUpdateParameter(updatedParameter)
    }

    @Test
    fun `screen has changed parameter from filled to empty, should call initial`() {
        val updatedParameter = mapOf(SearchApiConst.Q to "")
        val viewModel = autoCompleteViewModel()
        `When Screen Parameter is Updated`(viewModel, updatedParameter)
        `Then Verify That Initial Usecase is called once`()
    }

    private fun `Then Verify That Initial Usecase is called once`() {
        verify(exactly = 1) { initialStateUseCase.execute(any(), any(), any()) }
    }

    @Test
    fun `screen has changed parameter from filled to filled, should re-call suggestion`() {
        val initialParameter = mapOf(SearchApiConst.Q to "samsung")
        val updatedParameter = mapOf(SearchApiConst.Q to "samsung2")
        val viewModel = autoCompleteViewModel(AutoCompleteState(initialParameter))
        `When Screen Parameter is Updated`(viewModel, updatedParameter)
        `Then Verify That Suggestion Usecase is called once`()
    }
}
