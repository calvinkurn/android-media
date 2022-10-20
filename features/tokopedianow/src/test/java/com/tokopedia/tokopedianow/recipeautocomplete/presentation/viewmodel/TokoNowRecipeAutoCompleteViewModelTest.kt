package com.tokopedia.tokopedianow.recipeautocomplete.presentation.viewmodel

import com.tokopedia.unit.test.ext.verifyValueEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class TokoNowRecipeAutoCompleteViewModelTest : TokoNowRecipeAutoCompleteViewModelTestFixture() {

    @Test
    fun `given title when submit search should set recipeListParam title`() {
        val title = "Lorem ipsum"

        viewModel.submitSearch(title)

        val expectedParams = "?title=Lorem ipsum"

        viewModel.recipeListParam
            .verifyValueEquals(expectedParams)
    }

    @Test
    fun `given blank title when submit search should set recipeListParam with blank title`() {
        val title = ""

        viewModel.submitSearch(title)

        val expectedParams = "?"

        viewModel.recipeListParam
            .verifyValueEquals(expectedParams)
    }
}