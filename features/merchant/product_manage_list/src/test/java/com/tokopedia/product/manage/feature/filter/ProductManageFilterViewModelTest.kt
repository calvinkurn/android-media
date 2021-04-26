package com.tokopedia.product.manage.feature.filter

import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.product.manage.data.*
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionsResponse
import com.tokopedia.product.manage.common.feature.list.data.model.filter.ProductListMetaResponse
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterDataUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterUiModel
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
    fun `when clearSelected should make all elements unselected`() {
        val sortViewModel = getSortFilterViewModel()
        val etalaseViewModel = getEtalaseFilterViewModel()
        val categoryViewModel = getCategoryFilterViewModel()
        val otherFilterViewModel = getOtherFilterFilterViewModel()

        viewModel.updateData(listOf(sortViewModel, etalaseViewModel, categoryViewModel, otherFilterViewModel))
        viewModel.clearSelected()

        verifyAllFilterDataIsNotSelected()
    }

    @Test
    fun `given isChipsShown false when updateShow should update element isChipsShown true`() {
        val isChipsShown = false
        val sortViewModel = getSortFilterViewModel(isChipsShown = isChipsShown)
        val etalaseViewModel = getEtalaseFilterViewModel(isChipsShown = isChipsShown)
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
    fun `given isChipsShown true when updateShow should update element isChipsShown false`() {
        val isChipsShown = true
        val sortViewModel = getSortFilterViewModel(isChipsShown = isChipsShown)
        val etalaseViewModel = getEtalaseFilterViewModel(isChipsShown = isChipsShown)
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
            ), false)
        verifyFilterViewModel(expectedData)
    }

    @Test
    fun `when updateSpecific should update element at a given index accordingly`() {
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
    fun `given isSelected false when updateSelect for Sort and Etalase should update filter data model isSelected true`() {
        val sortDataModel = getSortDataModel(isSelected = false)
        val etalaseDataModel = getEtalaseDataModel(isSelected = false)

        val sortViewModel = getSortFilterViewModel(sortDataModel)
        val etalaseViewModel = getEtalaseFilterViewModel(etalaseDataModel)
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
    fun `given isSelected true when updateSelect for Sort and Etalase should update filter data model isSelected false`() {
        val sortDataModel = getSortDataModel(isSelected = true)
        val etalaseDataModel = getEtalaseDataModel(isSelected = true)

        val sortViewModel = getSortFilterViewModel(sortDataModel)
        val etalaseViewModel = getEtalaseFilterViewModel(etalaseDataModel)
        val categoryViewModel = getCategoryFilterViewModel()
        val otherFilterViewModel = getOtherFilterFilterViewModel()

        viewModel.updateData(listOf(sortViewModel, etalaseViewModel, categoryViewModel, otherFilterViewModel))
        viewModel.updateSelect(getSortDataModel(), ProductManageFilterMapper.SORT_HEADER)
        viewModel.updateSelect(getEtalaseDataModel(), ProductManageFilterMapper.ETALASE_HEADER)

        val selectedSortDataViewModel = FilterDataUiModel("312", "Some Sort", "DESC", false)
        val expectedSortModel = getSortFilterViewModel(selectedSortDataViewModel)
        val expectedSortIndex = ProductManageFilterFragment.ITEM_SORT_INDEX
        val selectedEtalaseDataViewModel = FilterDataUiModel("342", "Some Etalase", "", false)
        val expectedEtalaseModel = getEtalaseFilterViewModel(selectedEtalaseDataViewModel)
        val expectedEtalaseIndex = ProductManageFilterFragment.ITEM_ETALASE_INDEX

        verifyFilterDataAtIndex(expectedSortModel, expectedSortIndex)
        verifyFilterDataAtIndex(expectedEtalaseModel, expectedEtalaseIndex)
    }

    @Test
    fun `given selected sort or etalase not found when updateSelect should NOT update filter data model`() {
        val selectedSort = getSortDataModel(id = "312", sort = "ASC")
        val selectedEtalase = getEtalaseDataModel(id = "111", sort = "ASC")

        val sortDataModel = getSortDataModel(isSelected = true)
        val etalaseDataModel = getEtalaseDataModel(isSelected = true)

        val sortViewModel = getSortFilterViewModel(sortDataModel)
        val etalaseViewModel = getEtalaseFilterViewModel(etalaseDataModel)
        val categoryViewModel = getCategoryFilterViewModel()
        val otherFilterViewModel = getOtherFilterFilterViewModel()

        viewModel.updateData(listOf(sortViewModel, etalaseViewModel, categoryViewModel, otherFilterViewModel))
        viewModel.updateSelect(selectedSort, ProductManageFilterMapper.SORT_HEADER)
        viewModel.updateSelect(selectedEtalase, ProductManageFilterMapper.ETALASE_HEADER)

        val selectedSortDataViewModel = FilterDataUiModel("312", "Some Sort", "DESC", false)
        val expectedSortModel = getSortFilterViewModel(selectedSortDataViewModel)
        val expectedSortIndex = ProductManageFilterFragment.ITEM_SORT_INDEX
        val selectedEtalaseDataViewModel = FilterDataUiModel("342", "Some Etalase", "", false)
        val expectedEtalaseModel = getEtalaseFilterViewModel(selectedEtalaseDataViewModel)
        val expectedEtalaseIndex = ProductManageFilterFragment.ITEM_ETALASE_INDEX

        verifyFilterDataAtIndex(expectedSortModel, expectedSortIndex)
        verifyFilterDataAtIndex(expectedEtalaseModel, expectedEtalaseIndex)
    }

    @Test
    fun `given selected etalase filter index more than 4 when updateSelect should sort etalase filter`() {
        val selectedEtalase = FilterDataUiModel("127", "Some Other Etalase", "", false)
        val etalaseViewModel = FilterUiModel(ProductManageFilterMapper.ETALASE_HEADER,
            mutableListOf(
                FilterDataUiModel("122", "Some Etalase", "", false),
                FilterDataUiModel("123", "Some Other Etalase", "", false),
                FilterDataUiModel("124", "Some Other Etalase", "", false),
                FilterDataUiModel("125", "Some Other Etalase", "", false),
                FilterDataUiModel("126", "Some Other Etalase", "", false),
                FilterDataUiModel("127", "Some Other Etalase", "", false),
            ), false)
        val sortViewModel = getSortFilterViewModel()
        val categoryViewModel = getCategoryFilterViewModel()
        val otherFilterViewModel = getOtherFilterFilterViewModel()

        viewModel.updateData(listOf(sortViewModel, etalaseViewModel, categoryViewModel, otherFilterViewModel))
        viewModel.updateSelect(selectedEtalase, ProductManageFilterMapper.ETALASE_HEADER)

        val selectedSortDataViewModel = FilterDataUiModel("312", "Some Sort", "DESC", false)
        val expectedSortModel = getSortFilterViewModel(selectedSortDataViewModel)
        val expectedSortIndex = ProductManageFilterFragment.ITEM_SORT_INDEX
        val selectedEtalaseDataViewModel = FilterDataUiModel("127", "Some Other Etalase", "", true)
        val expectedEtalaseModel = FilterUiModel(ProductManageFilterMapper.ETALASE_HEADER,
            mutableListOf(
                selectedEtalaseDataViewModel,
                FilterDataUiModel("122", "Some Etalase", "", false),
                FilterDataUiModel("123", "Some Other Etalase", "", false),
                FilterDataUiModel("124", "Some Other Etalase", "", false),
                FilterDataUiModel("125", "Some Other Etalase", "", false),
                FilterDataUiModel("126", "Some Other Etalase", "", false)
            ), false)
        val expectedEtalaseIndex = ProductManageFilterFragment.ITEM_ETALASE_INDEX

        verifyFilterDataAtIndex(expectedSortModel, expectedSortIndex)
        verifyFilterDataAtIndex(expectedEtalaseModel, expectedEtalaseIndex)
    }

    @Test
    fun `when updateSelect for Category and Other Filter should update filter data model accordingly`() {
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
    fun `given selected category or other filter not found when updateSelect should NOT update filter data model`() {
        val sortViewModel = getSortFilterViewModel()
        val etalaseViewModel = getEtalaseFilterViewModel()
        val categoryViewModel = getCategoryFilterViewModel()
        val otherFilterViewModel = getOtherFilterFilterViewModel()

        viewModel.updateData(listOf(sortViewModel, etalaseViewModel, categoryViewModel, otherFilterViewModel))
        viewModel.updateSelect(getCategoryDataModel(id = "9090"))
        viewModel.updateSelect(getOtherFilterDataModel(id = "7070"))

        val selectedCategoryDataViewModel = FilterDataUiModel("293", "Some Category", "", false)
        val expectedCategoryModel = getCategoryFilterViewModel(selectedCategoryDataViewModel)
        val expectedCategoryIndex = ProductManageFilterFragment.ITEM_CATEGORIES_INDEX
        val selectedOtherFilterDataViewModel = FilterDataUiModel("4183", "Some Other Filter", "", false)
        val expectedOtherFilterModel = getOtherFilterFilterViewModel(selectedOtherFilterDataViewModel)
        val expectedOtherFilterIndex = ProductManageFilterFragment.ITEM_OTHER_FILTER_INDEX

        verifyFilterDataAtIndex(expectedCategoryModel, expectedCategoryIndex)
        verifyFilterDataAtIndex(expectedOtherFilterModel, expectedOtherFilterIndex)
    }

    @Test
    fun `given filter order is wrong when updateSelect should not update filter data model`() {
        val sortViewModel = getSortFilterViewModel()
        val etalaseViewModel = getEtalaseFilterViewModel()
        val categoryViewModel = getCategoryFilterViewModel()
        val otherFilterViewModel = getOtherFilterFilterViewModel()

        //Expected filter order (sortViewModel, etalaseViewModel, categoryViewModel, otherFilterViewModel)
        val filterList = listOf(categoryViewModel, otherFilterViewModel, sortViewModel, etalaseViewModel)

        viewModel.updateData(filterList)
        viewModel.updateSelect(categoryViewModel.data.last())
        viewModel.updateSelect(otherFilterViewModel.data.last())

        val selectedCategoryDataViewModel = FilterDataUiModel("293", "Some Category", "", false)
        val expectedCategoryModel = getCategoryFilterViewModel(selectedCategoryDataViewModel)
        val selectedOtherFilterDataViewModel = FilterDataUiModel("4183", "Some Other Filter", "", false)
        val expectedOtherFilterModel = getOtherFilterFilterViewModel(selectedOtherFilterDataViewModel)

        verifyFilterDataAtIndex(expectedCategoryModel, 0)
        verifyFilterDataAtIndex(expectedOtherFilterModel, 1)
    }

    @Test
    fun `when updateSelect for Category and Other Filter last element should update filter data model accordingly`() {
        val sortViewModel = getSortFilterViewModel()
        val etalaseViewModel = getEtalaseFilterViewModel()
        val categoryViewModel = getCategoryFilterViewModel()
        val otherFilterViewModel = getOtherFilterFilterViewModel()

        viewModel.updateData(listOf(sortViewModel, etalaseViewModel, categoryViewModel, otherFilterViewModel))
        viewModel.updateSelect(categoryViewModel.data.last())
        viewModel.updateSelect(otherFilterViewModel.data.last())

        val expectedCategoryModel = FilterUiModel(ProductManageFilterMapper.CATEGORY_HEADER,
                mutableListOf(
                        FilterDataUiModel("458", "Some Other Category", "", true),
                        getCategoryDataModel(),
                        FilterDataUiModel("452", "Some Other Category", "", false),
                        FilterDataUiModel("453", "Some Other Category", "", false),
                        FilterDataUiModel("454", "Some Other Category", "", false),
                        FilterDataUiModel("455", "Some Other Category", "", false),
                        FilterDataUiModel("456", "Some Other Category", "", false),
                        FilterDataUiModel("457", "Some Other Category", "", false)
                ), false)
        val expectedCategoryIndex = ProductManageFilterFragment.ITEM_CATEGORIES_INDEX
        val expectedOtherFilterModel = FilterUiModel(ProductManageFilterMapper.OTHER_FILTER_HEADER,
                mutableListOf(
                        FilterDataUiModel("617", "Some Other Filter", "", true),
                        getOtherFilterDataModel(),
                        FilterDataUiModel("611", "Some Other Filter", "", false),
                        FilterDataUiModel("612", "Some Other Filter", "", false),
                        FilterDataUiModel("613", "Some Other Filter", "", false),
                        FilterDataUiModel("614", "Some Other Filter", "", false),
                        FilterDataUiModel("615", "Some Other Filter", "", false),
                        FilterDataUiModel("616", "Some Other Filter", "", false)
                ), false)
        val expectedOtherFilterIndex = ProductManageFilterFragment.ITEM_OTHER_FILTER_INDEX

        verifyFilterDataAtIndex(expectedCategoryModel, expectedCategoryIndex)
        verifyFilterDataAtIndex(expectedOtherFilterModel, expectedOtherFilterIndex)
    }

    @Test
    fun `when updateShow should update filter ui model show sate accordingly`() {
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
    fun `when there is selected sort or etalase should unselect old and select new sort or etalase`() {
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
    fun `when select selected sort or etalase should unselect selected value and select new value`() {
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
    fun `when select selected sort or etalase should unselect selected value`() {
        val selectedSort = getSelectedSortData()
        val selectedEtalase = getSelectedEtalaseData()
        val categoryViewModel = getCategoryFilterViewModel(getCategoryDataModel())
        val otherFilterViewModel = getOtherFilterFilterViewModel(getOtherFilterDataModel())

        viewModel.updateData(listOf(selectedSort, selectedEtalase, categoryViewModel, otherFilterViewModel))
        viewModel.updateSelect(selectedSort.data[1], ProductManageFilterMapper.SORT_HEADER)
        viewModel.updateSelect(selectedSort.data[1], ProductManageFilterMapper.ETALASE_HEADER)

        val expectedSortModel = getSortFilterViewModel()
        val expectedSortIndex = ProductManageFilterFragment.ITEM_SORT_INDEX
        val expectedEtalaseModel = getEtalaseFilterViewModel()
        val expectedEtalaseIndex = ProductManageFilterFragment.ITEM_ETALASE_INDEX

        verifyFilterDataAtIndex(expectedSortModel, expectedSortIndex)
        verifyFilterDataAtIndex(expectedEtalaseModel, expectedEtalaseIndex)
    }

    @Test
    fun `when data is null all operations should do nothing`() {
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

    @Test
    fun `given filterData is null when getSelectedFilters should return empty list`() {
        viewModel.getSelectedFilters()

        val expectedResult = emptyList<FilterDataUiModel>()
        val actualResult = viewModel.getSelectedFilters()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `given filterData is not null when getSelectedFilters should return list of selected filter`() {
        val filterList = listOf(
            FilterUiModel(title = "Filter A", data = mutableListOf(
                FilterDataUiModel(id = "1", name = "Filter A", select = true),
                FilterDataUiModel(id = "2", name = "Filter B", select = false)
            ), isChipsShown = true),
            FilterUiModel(title = "Filter B", data = mutableListOf(
                FilterDataUiModel(id = "3", name = "Filter C", select = true),
                FilterDataUiModel(id = "4", name = "Filter D", select = false)
            ), isChipsShown = true),
            FilterUiModel(title = "Filter C", data = mutableListOf(), isChipsShown = true)
        )

        viewModel.updateData(filterList)
        viewModel.getSelectedFilters()

        val expectedResult = listOf(
            FilterDataUiModel(id = "1", name = "Filter A", select = true),
            FilterDataUiModel(id = "3", name = "Filter C", select = true)
        )
        val actualResult = viewModel.getSelectedFilters()

        assertEquals(expectedResult, actualResult)
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