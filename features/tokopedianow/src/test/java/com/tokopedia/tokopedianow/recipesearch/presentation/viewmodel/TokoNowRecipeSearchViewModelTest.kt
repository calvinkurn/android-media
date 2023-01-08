package com.tokopedia.tokopedianow.recipesearch.presentation.viewmodel

import com.tokopedia.unit.test.ext.verifyValueEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class TokoNowRecipeSearchViewModelTest: TokoNowRecipeSearchViewModelTestFixture() {

    @Test
    fun `given query param title when searchRecipe should set keyword empty`() {
        val query = "title=hello world&tag_ids=4,5,6&ingredient_ids=1,2,3"

        viewModel.searchRecipe(query)

        val expectedSearchKeyword = "hello world"

        viewModel.searchKeyword
            .verifyValueEquals(expectedSearchKeyword)
    }

    @Test
    fun `given null query when searchRecipe should set keyword empty`() {
        val query = null

        viewModel.searchRecipe(query)

        val expectedSearchKeyword = ""

        viewModel.searchKeyword
            .verifyValueEquals(expectedSearchKeyword)
    }

    @Test
    fun `given empty query when searchRecipe should set keyword empty`() {
        val query = ""

        viewModel.searchRecipe(query)

        val expectedSearchKeyword = ""

        viewModel.searchKeyword
            .verifyValueEquals(expectedSearchKeyword)
    }
}