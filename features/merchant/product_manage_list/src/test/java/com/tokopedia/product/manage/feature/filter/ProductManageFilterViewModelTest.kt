package com.tokopedia.product.manage.feature.filter

import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.product.manage.data.*
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionsResponse
import com.tokopedia.product.manage.feature.filter.data.model.ProductListMetaResponse
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterDataUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterUiModel
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import org.junit.Test

class ProductManageFilterViewModelTest: ProductManageFilterViewModelTextFixture() {

    @Test
    fun `getCombined should execute expected use case`() {
        val filterOptionsResponse = FilterOptionsResponse(
                productListMetaResponse = ProductListMetaResponse(),
                shopEtalase =  arrayListOf(ShopEtalaseModel()),
                categoriesResponse = CategoriesResponse()
        )
        onGetProductManageFilterOptions_thenReturn(filterOptionsResponse)

        viewModel.getData("0")

        val expectedResponse = Success(filterOptionsResponse)

        verifyGetProductManageFilterOptionsUseCaseCalled()
        verifyFilterOptionsResponse(expectedResponse)
    }

    @Test
    fun `when_clearSelected_should_make_all_elements_unselected`() {
        val sortViewModel = getSortFilterViewModel()
        val etalaseViewModel = getEtalaseFilterViewModel()
        val categoryViewModel = getCategoryFilterViewModel()
        val otherFilterViewModel = getOtherFilterFilterViewModel()

        viewModel.updateData(listOf(sortViewModel, etalaseViewModel, categoryViewModel, otherFilterViewModel))
        viewModel.clearSelected()

        verifyAllFilterDataIsNotSelected()
    }

    @Test
    fun `when_updateShow_should_update_element_isChipsShown_accordingly`() {
        val sortViewModel = getSortFilterViewModel()
        val etalaseViewModel = getEtalaseFilterViewModel()
        val categoryViewModel = getCategoryFilterViewModel()
        val otherFilterViewModel = getOtherFilterFilterViewModel()

        viewModel.updateData(listOf(sortViewModel, etalaseViewModel, categoryViewModel, otherFilterViewModel))
        viewModel.updateShow(sortViewModel)

        val expectedData = FilterUiModel(ProductManageFilterMapper.SORT_HEADER,
                mutableListOf(
                        getSortDataModel(),
                        FilterDataUiModel("706", "Some Other Sort", "ASC", false),
                        FilterDataUiModel("707", "Some Other Sort", "ASC", false),
                        FilterDataUiModel("708", "Some Other Sort", "ASC", false),
                        FilterDataUiModel("709", "Some Other Sort", "ASC", false),
                        FilterDataUiModel("710", "Some Other Sort", "ASC", false)
                ), true)
        verifyFilterViewModel(expectedData)
    }

    @Test
    fun `when_updateSpecific_should_update_element_at_a_given_index_accordingly`() {
        val sortViewModel = getSortFilterViewModel()
        val etalaseViewModel = getEtalaseFilterViewModel()
        val categoryViewModel = getCategoryFilterViewModel()
        val otherFilterViewModel = getOtherFilterFilterViewModel()
        val dataToUpdate = FilterUiModel("New Data", mutableListOf(), true)

        viewModel.updateData(listOf(sortViewModel, etalaseViewModel, categoryViewModel, otherFilterViewModel))
        viewModel.updateSpecificData(dataToUpdate, ProductManageFilterFragment.ITEM_SORT_INDEX)

        val expectedIndex = ProductManageFilterFragment.ITEM_SORT_INDEX

        verifyFilterDataAtIndex(dataToUpdate, expectedIndex)
    }

    @Test
    fun `when_updateSelect_for_Sort_and_Etalase_should_update_filter_data_model_accordingly`() {
        val sortViewModel = getSortFilterViewModel()
        val etalaseViewModel = getEtalaseFilterViewModel()
        val categoryViewModel = getCategoryFilterViewModel()
        val otherFilterViewModel = getOtherFilterFilterViewModel()

        viewModel.updateData(listOf(sortViewModel, etalaseViewModel, categoryViewModel, otherFilterViewModel))
        viewModel.updateSelect(getSortDataModel(), ProductManageFilterMapper.SORT_HEADER)
        viewModel.updateSelect(getEtalaseDataModel(), ProductManageFilterMapper.ETALASE_HEADER)

        val selectedSortDataViewModel = FilterDataUiModel("312", "Some Sort", "DESC", true)
        val expectedSortModel = getSortFilterViewModel(selectedSortDataViewModel)
        val expectedSortIndex = ProductManageFilterFragment.ITEM_SORT_INDEX
        val selectedEtalaseDataViewModel = FilterDataUiModel("342", "Some Etalase", "", true)
        val expectedEtalaseModel = getEtalaseFilterViewModel(selectedEtalaseDataViewModel)
        val expectedEtalaseIndex = ProductManageFilterFragment.ITEM_ETALASE_INDEX

        verifyFilterDataAtIndex(expectedSortModel, expectedSortIndex)
        verifyFilterDataAtIndex(expectedEtalaseModel, expectedEtalaseIndex)
    }

    @Test
    fun `when_updateSelect_for_Category_and_Other_Filter_should_update_filter_data_model_accordingly`() {
        val sortViewModel = getSortFilterViewModel()
        val etalaseViewModel = getEtalaseFilterViewModel()
        val categoryViewModel = getCategoryFilterViewModel()
        val otherFilterViewModel = getOtherFilterFilterViewModel()

        viewModel.updateData(listOf(sortViewModel, etalaseViewModel, categoryViewModel, otherFilterViewModel))
        viewModel.updateSelect(getCategoryDataModel())
        viewModel.updateSelect(getOtherFilterDataModel())

        val selectedCategoryDataViewModel = FilterDataUiModel("293", "Some Category", "", true)
        val expectedCategoryModel = getCategoryFilterViewModel(selectedCategoryDataViewModel)
        val expectedCategoryIndex = ProductManageFilterFragment.ITEM_CATEGORIES_INDEX
        val selectedOtherFilterDataViewModel = FilterDataUiModel("4183", "Some Other Filter", "", true)
        val expectedOtherFilterModel = getOtherFilterFilterViewModel(selectedOtherFilterDataViewModel)
        val expectedOtherFilterIndex = ProductManageFilterFragment.ITEM_OTHER_FILTER_INDEX

        verifyFilterDataAtIndex(expectedCategoryModel, expectedCategoryIndex)
        verifyFilterDataAtIndex(expectedOtherFilterModel, expectedOtherFilterIndex)
    }

    @Test
    fun `when_updateSelect_for_Category_and_Other_Filter_last_element_should_update_filter_data_model_accordingly`() {
        val sortViewModel = getSortFilterViewModel()
        val etalaseViewModel = getEtalaseFilterViewModel()
        val categoryViewModel = getCategoryFilterViewModel()
        val otherFilterViewModel = getOtherFilterFilterViewModel()

        viewModel.updateData(listOf(sortViewModel, etalaseViewModel, categoryViewModel, otherFilterViewModel))
        viewModel.updateSelect(categoryViewModel.data.last())
        viewModel.updateSelect(otherFilterViewModel.data.last())

        val expectedCategoryModel = FilterUiModel(ProductManageFilterMapper.CATEGORY_HEADER,
                mutableListOf(
                        FilterDataUiModel("456", "Some Other Category", "", true),
                        getCategoryDataModel(),
                        FilterDataUiModel("452", "Some Other Category", "", false),
                        FilterDataUiModel("453", "Some Other Category", "", false),
                        FilterDataUiModel("454", "Some Other Category", "", false),
                        FilterDataUiModel("455", "Some Other Category", "", false)
                ), false)
        val expectedCategoryIndex = ProductManageFilterFragment.ITEM_CATEGORIES_INDEX
        val expectedOtherFilterModel = FilterUiModel(ProductManageFilterMapper.OTHER_FILTER_HEADER,
                mutableListOf(
                        FilterDataUiModel("615", "Some Other Filter", "", true),
                        getOtherFilterDataModel(),
                        FilterDataUiModel("611", "Some Other Filter", "", false),
                        FilterDataUiModel("612", "Some Other Filter", "", false),
                        FilterDataUiModel("613", "Some Other Filter", "", false),
                        FilterDataUiModel("614", "Some Other Filter", "", false)
                ), false)
        val expectedOtherFilterIndex = ProductManageFilterFragment.ITEM_OTHER_FILTER_INDEX

        verifyFilterDataAtIndex(expectedCategoryModel, expectedCategoryIndex)
        verifyFilterDataAtIndex(expectedOtherFilterModel, expectedOtherFilterIndex)
    }

    @Test
    fun `when_updateShow_should_update_filter_ui_model_show_sate_accordingly`() {
        val desiredShow = listOf(true, true, false ,true)
        val sortViewModel = getSortFilterViewModel(getSortDataModel())
        val etalaseViewModel = getEtalaseFilterViewModel(getEtalaseDataModel())
        val categoryViewModel = getCategoryFilterViewModel(getCategoryDataModel())
        val otherFilterViewModel = getOtherFilterFilterViewModel(getOtherFilterDataModel())

        viewModel.updateData(listOf(sortViewModel, etalaseViewModel, categoryViewModel, otherFilterViewModel))
        viewModel.updateShow(desiredShow)

        verifyChipsShownStateEquals(desiredShow)
    }

    @Test
    fun `when_there_is_selected_sort_or_etalase_should_unselect_old_and_select_new_sort_or_etalase`() {
        val selectedSort = getSelectedSortData()
        val selectedEtalase = getSelectedEtalaseData()
        val categoryViewModel = getCategoryFilterViewModel(getCategoryDataModel())
        val otherFilterViewModel = getOtherFilterFilterViewModel(getOtherFilterDataModel())

        viewModel.updateData(listOf(selectedSort, selectedEtalase, categoryViewModel, otherFilterViewModel))
        viewModel.updateSelect(getSortDataModel(), ProductManageFilterMapper.SORT_HEADER)
        viewModel.updateSelect(getEtalaseDataModel(), ProductManageFilterMapper.ETALASE_HEADER)

        val selectedSortDataViewModel = FilterDataUiModel("312", "Some Sort", "DESC", true)
        val expectedSortModel = getSortFilterViewModel(selectedSortDataViewModel)
        val expectedSortIndex = ProductManageFilterFragment.ITEM_SORT_INDEX
        val selectedEtalaseDataViewModel = FilterDataUiModel("342", "Some Etalase", "", true)
        val expectedEtalaseModel = getEtalaseFilterViewModel(selectedEtalaseDataViewModel)
        val expectedEtalaseIndex = ProductManageFilterFragment.ITEM_ETALASE_INDEX

        verifyFilterDataAtIndex(expectedSortModel, expectedSortIndex)
        verifyFilterDataAtIndex(expectedEtalaseModel, expectedEtalaseIndex)
    }

    @Test
    fun `when_select_selected_sort_or_etalase_should_unselect_selected_value`() {
        val selectedSort = getSelectedSortData()
        val selectedEtalase = getSelectedEtalaseData()
        val categoryViewModel = getCategoryFilterViewModel(getCategoryDataModel())
        val otherFilterViewModel = getOtherFilterFilterViewModel(getOtherFilterDataModel())

        viewModel.updateData(listOf(selectedSort, selectedEtalase, categoryViewModel, otherFilterViewModel))
        viewModel.updateSelect(selectedSort.data.last(), ProductManageFilterMapper.SORT_HEADER)
        viewModel.updateSelect(selectedSort.data.last(), ProductManageFilterMapper.ETALASE_HEADER)

        val expectedSortModel = FilterUiModel(ProductManageFilterMapper.SORT_HEADER,
                mutableListOf(
                        FilterDataUiModel("710", "Some Other Sort", "ASC", true),
                        getSortDataModel(),
                        FilterDataUiModel("706", "Some Other Sort", "ASC", false),
                        FilterDataUiModel("707", "Some Other Sort", "ASC", false),
                        FilterDataUiModel("708", "Some Other Sort", "ASC", false),
                        FilterDataUiModel("709", "Some Other Sort", "ASC", false)
                ), false)
        val expectedSortIndex = ProductManageFilterFragment.ITEM_SORT_INDEX
        val expectedEtalaseModel = getEtalaseFilterViewModel()
        val expectedEtalaseIndex = ProductManageFilterFragment.ITEM_ETALASE_INDEX

        verifyFilterDataAtIndex(expectedSortModel, expectedSortIndex)
        verifyFilterDataAtIndex(expectedEtalaseModel, expectedEtalaseIndex)
    }

    @Test
    fun `when_data_is_null_all_operations_should_do_nothing`() {
        val desiredShow = listOf(true, true, false ,true)
        val sortDataModel = getSortDataModel()
        val filterUiModel = getSortFilterViewModel(sortDataModel)

        viewModel.updateShow(desiredShow)
        viewModel.clearSelected()
        viewModel.updateShow(filterUiModel)
        viewModel.updateSpecificData(filterUiModel, ProductManageFilterFragment.ITEM_SORT_INDEX)
        viewModel.updateSelect(sortDataModel)
        viewModel.updateSelect(sortDataModel, ProductManageFilterMapper.SORT_HEADER)

        verifyDataIsNull()
    }

    private fun onGetProductManageFilterOptions_thenReturn(filterOptionsResponse: FilterOptionsResponse) {
        coEvery { getProductManageFilterOptionsUseCase.executeOnBackground() } returns filterOptionsResponse
    }

    private fun verifyGetProductManageFilterOptionsUseCaseCalled() {
        coVerify { getProductManageFilterOptionsUseCase.executeOnBackground() }
    }

    private fun verifyAllFilterDataIsNotSelected() {
        val actualData = viewModel.filterData.value
        actualData?.forEach {
            it.data.forEach { data ->
                assertFalse(data.select)
            }
        }
    }

    private fun verifyFilterOptionsResponse(expectedResponse: Success<FilterOptionsResponse>) {
        val actualResponse = viewModel.filterOptionsResponse.value as Success<FilterOptionsResponse>
        assertEquals(expectedResponse, actualResponse)
    }

    private fun verifyFilterViewModel(expectedModel: FilterUiModel) {
        val actualData = viewModel.filterData.value
        actualData?.first()?.let {
            assertEquals(expectedModel.title, it.title)
            assertEquals(expectedModel.data, it.data)
            assertEquals(expectedModel.isChipsShown, it.isChipsShown)
        }
    }

    private fun verifyFilterDataAtIndex(expectedModel: FilterUiModel, index: Int) {
        val actualData = viewModel.filterData.value
        actualData?.let {
            val dataToVerify = it[index]
            assertEquals(expectedModel.title, dataToVerify.title)
            expectedModel.data.forEachIndexed { index, filterDataViewModel ->
                assertEquals(filterDataViewModel, dataToVerify.data[index])
            }
            assertEquals(expectedModel.isChipsShown, dataToVerify.isChipsShown)
        }
    }

    private fun verifyChipsShownStateEquals(expectedShowStates: List<Boolean>) {
        val actualData = viewModel.filterData.value
        actualData?.forEachIndexed { index, filterUiModel ->
            assertEquals(expectedShowStates[index], filterUiModel.isChipsShown)
        }
    }

    private fun verifyDataIsNull() {
        val actualData = viewModel.filterData.value
        assertEquals(null, actualData)
    }
}