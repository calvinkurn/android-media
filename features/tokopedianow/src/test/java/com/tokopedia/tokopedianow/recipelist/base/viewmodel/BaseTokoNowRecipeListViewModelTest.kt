package com.tokopedia.tokopedianow.recipelist.base.viewmodel

import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeFilterUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeHeaderUiModel
import com.tokopedia.unit.test.ext.verifyValueEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class BaseTokoNowRecipeListViewModelTest : BaseTokoNowRecipeListViewModelTestFixture() {

    @Test
    fun `given enableHeaderBackground true when onViewCreated should add header item to visitable list`() {
        viewModel.enableHeaderBackground = true

        viewModel.onViewCreated()

        val filterChip = RecipeFilterUiModel(
            selectedFiltersCount = 0
        )

        val expectedVisitableList = listOf(
            RecipeHeaderUiModel,
            filterChip
        )

        val expectedEnableHeaderBackground = true
        val actualEnableHeaderBackground = viewModel.enableHeaderBackground

        viewModel.visitableList
            .verifyValueEquals(expectedVisitableList)

        viewModel.showHeaderBackground
            .verifyValueEquals(true)

        assertEquals(expectedEnableHeaderBackground, actualEnableHeaderBackground)
    }

    @Test
    fun `given enableHeaderBackground false when onViewCreated should NOT add header item to visitable list`() {
        viewModel.enableHeaderBackground = false

        viewModel.onViewCreated()

        val filterChip = RecipeFilterUiModel(
            selectedFiltersCount = 0
        )

        val expectedVisitableList = listOf(filterChip)

        val expectedEnableHeaderBackground = false
        val actualEnableHeaderBackground = viewModel.enableHeaderBackground

        viewModel.visitableList
            .verifyValueEquals(expectedVisitableList)

        viewModel.showHeaderBackground
            .verifyValueEquals(false)

        assertEquals(expectedEnableHeaderBackground, actualEnableHeaderBackground)
    }

    @Test
    fun `when get warehouseId should return warehouseId from address data`() {
        onGetWarehouseId_thenReturn(warehouseId = 3)

        val expectedWarehouseId = "3"
        val actualWarehouseId = viewModel.warehouseId

        assertEquals(actualWarehouseId, expectedWarehouseId)
    }
}