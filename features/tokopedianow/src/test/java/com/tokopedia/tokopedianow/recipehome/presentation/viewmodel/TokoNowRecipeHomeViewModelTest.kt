package com.tokopedia.tokopedianow.recipehome.presentation.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
open class TokoNowRecipeHomeViewModelTest: TokoNowRecipeHomeViewModelTestFixture() {

    @Test
    fun `when get source page should return Home`() {
        val actualSourcePage = viewModel.sourcePage
        val expectedSourcePage = "Home"

        assertEquals(expectedSourcePage, actualSourcePage)
    }
}