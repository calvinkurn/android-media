package com.tokopedia.tokopedianow.categoryfilter.presentation.viewmodel

import com.tokopedia.abstraction.common.data.model.response.Header
import com.tokopedia.tokopedianow.categoryfilter.presentation.uimodel.CategoryFilterChip
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse.CategoryListResponse
import com.tokopedia.tokopedianow.common.domain.model.GetCategoryListResponse.CategoryListResponse.CategoryResponse
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*
import com.tokopedia.unit.test.ext.verifySuccessEquals
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.usecase.coroutines.Success
import org.junit.Test

class TokoNowCategoryFilterViewModelTest : TokoNowCategoryFilterViewModelTestFixture() {

    @Test
    fun `when getCategoryList success should set categoryList value and update selectedFilter`() {
        val selectedFilter = SelectedSortFilter(listOf("1"), listOf("Filter"))

        val categoryListResponse = CategoryListResponse(
            header = Header(),
            data = listOf(
                CategoryResponse(
                    id = "1",
                    name = "Title",
                    url = "https://tokopedia.com",
                    appLinks = "tokopedia://now",
                    isAdult = 0,
                    color = "#FFFFFF"
                )
            )
        )

        onGetCategoryList_thenReturn(categoryListResponse)

        viewModel.getCategoryList(selectedFilter)

        val categoryFilterList = listOf(
            CategoryFilterChip(id = "1", title = "Title")
        )
        val expectedResult = Success(categoryFilterList)

        verifyGetCategoryListCalled()

        viewModel.categoryList
            .verifySuccessEquals(expectedResult)

        viewModel.selectedFilter
            .verifyValueEquals(selectedFilter)
    }

    @Test
    fun `given selectedFilter null when getCategoryList success should set categoryList value`() {
        val selectedFilter = null

        val categoryListResponse = CategoryListResponse(
            header = Header(),
            data = listOf(
                CategoryResponse(
                    id = "1",
                    name = "Title",
                    url = "https://tokopedia.com",
                    appLinks = "tokopedia://now",
                    isAdult = 0,
                    color = "#FFFFFF"
                )
            )
        )

        onGetCategoryList_thenReturn(categoryListResponse)

        viewModel.getCategoryList(selectedFilter)

        val categoryFilterList = listOf(
            CategoryFilterChip(id = "1", title = "Title")
        )
        val expectedResult = Success(categoryFilterList)

        verifyGetCategoryListCalled()

        viewModel.categoryList
            .verifySuccessEquals(expectedResult)

        viewModel.selectedFilter
            .verifyValueEquals(selectedFilter)
    }

    @Test
    fun `when getCategoryList error should do nothing`() {
        val selectedFilter = SelectedSortFilter(listOf("1"), listOf("Filter"))
        val error = NullPointerException()

        onGetCategoryList_thenReturn(error)

        viewModel.getCategoryList(selectedFilter)

        verifyGetCategoryListCalled()

        viewModel.categoryList
            .verifyValueEquals(null)

        viewModel.selectedFilter
            .verifyValueEquals(null)
    }

    @Test
    fun `given selectedFilter contains category filter when updateSelectedFilter should remove filter from selectedFilter`() {
        val filter = CategoryFilterChip("1", "Filter 1")

        viewModel.updateSelectedFilter(filter)
        viewModel.updateSelectedFilter(filter)

        val expectedResult = null

        viewModel.selectedFilter
            .verifyValueEquals(expectedResult)
    }

    @Test
    fun `given selectedFilterIds NOT contains category filter when updateSelectedFilter should add filter to selectedFilter`() {
        val firstFilter = CategoryFilterChip("1", "Filter 1")
        val secondFilter = CategoryFilterChip("2", "Filter 2")

        viewModel.updateSelectedFilter(firstFilter)
        viewModel.updateSelectedFilter(secondFilter)

        val expectedResult = SelectedSortFilter(
            id = listOf("1", "2"),
            title = listOf("Filter 1", "Filter 2")
        )

        viewModel.selectedFilter
            .verifyValueEquals(expectedResult)
    }

    @Test
    fun `when clearSelectedFilter should set selectedFilter null`() {
        viewModel.clearSelectedFilter()

        viewModel.selectedFilter
            .verifyValueEquals(null)
    }

    @Test
    fun `when setSelectedFilter should update selectedFilter value`() {
        val selectedSortFilter = SelectedSortFilter(
            id = listOf("1"),
            title = listOf("Filter  1")
        )

        viewModel.setSelectedFilter(selectedSortFilter)

        viewModel.selectedFilter
            .verifyValueEquals(selectedSortFilter)
    }

    @Test
    fun `when applyFilter should set applyFilter value`() {
        val selectedSortFilter = SelectedSortFilter(
            id = listOf("1"),
            title = listOf("Filter  1")
        )

        viewModel.setSelectedFilter(selectedSortFilter)
        viewModel.applyFilter()

        viewModel.applyFilter
            .verifyValueEquals(selectedSortFilter)
    }
}
