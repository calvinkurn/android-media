package com.tokopedia.tokopedianow.recipelist.base.viewmodel

import com.tokopedia.tokopedianow.common.model.TokoNowServerErrorUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeHeaderUiModel
import com.tokopedia.tokopedianow.recipelist.presentation.uimodel.RecipeUiModel
import com.tokopedia.tokopedianow.recipelist.util.LoadPageStatus
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class BaseTokoNowRecipeListViewModelTestPageStatus: BaseTokoNowRecipeListViewModelTestFixture() {

    @Test
    fun `given visitable list contains recipe item when getLoadPageStatus then return page status SUCCESS`() {
        val item = RecipeUiModel(
            id = "1",
            title = "Recipe",
            portion = 5,
            duration = 10,
            tags = emptyList(),
            thumbnail = "",
            isBookmarked = false
        )

        addItemToVisitableList(item)

        val expectedLoadPageStatus = LoadPageStatus.SUCCESS
        val actualLoadPageStatus = viewModel.getLoadPageStatus()

        assertEquals(expectedLoadPageStatus, actualLoadPageStatus)
    }

    @Test
    fun `given visitable list contains server error item when getLoadPageStatus then return page status ERROR`() {
        addItemToVisitableList(TokoNowServerErrorUiModel)

        val expectedLoadPageStatus = LoadPageStatus.ERROR
        val actualLoadPageStatus = viewModel.getLoadPageStatus()

        assertEquals(expectedLoadPageStatus, actualLoadPageStatus)
    }

    @Test
    fun `given visitable list does NOT contain recipe item when getLoadPageStatus then return page status EMPTY`() {
        addItemToVisitableList(RecipeHeaderUiModel)

        val expectedLoadPageStatus = LoadPageStatus.EMPTY
        val actualLoadPageStatus = viewModel.getLoadPageStatus()

        assertEquals(expectedLoadPageStatus, actualLoadPageStatus)
    }
}