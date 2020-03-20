package com.tokopedia.product.manage.feature.filter

import com.tokopedia.core.common.category.domain.model.CategoriesResponse
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
        viewModel.clearSelected()
        verifyAllFilterDataIsNotSelected()
    }

    @Test
    fun `when_updateShow_should_update_element_isChipsShown_accordingly`() {
        val filterViewModel = FilterUiModel("Sort", mutableListOf(), false)

        viewModel.updateData(listOf(filterViewModel))
        viewModel.updateShow(filterViewModel)

        val expectedData = FilterUiModel("Sort", mutableListOf(), true)
        verifyFilterViewModel(expectedData)
    }

    @Test
    fun `when_updateSpecific_should_update_element_at_a_given_index_accordingly`() {
        val filterViewModel = FilterUiModel("Sort", mutableListOf(), false)
        val dataToUpdate = FilterUiModel("New Data", mutableListOf(), true)

        viewModel.updateData(listOf(filterViewModel))
        viewModel.updateSpecificData(dataToUpdate, ProductManageFilterFragment.ITEM_SORT_INDEX)

        val expectedIndex = ProductManageFilterFragment.ITEM_SORT_INDEX

        verifyFilterDataAtIndex(dataToUpdate, expectedIndex)
    }

    @Test
    fun `when_updateSelect_for_Sort_should_update_filter_data_model_accordingly`() {
        val sortDataModel =  getSortDataModel()
        val sortViewModel = getSortFilterViewModel(sortDataModel)
        val etalaseViewModel = getEtalaseFilterViewModel(getEtalaseDataModel())
        val categoryViewModel = getCategoryFilterViewModel(getCategoryDataModel())
        val otherFilterViewModel = getOtherFilterFilterViewModel(getOtherFilterDataModel())

        viewModel.updateData(listOf(sortViewModel, etalaseViewModel, categoryViewModel, otherFilterViewModel))
        viewModel.updateSelect(sortDataModel, ProductManageFilterMapper.SORT_HEADER)

        val selectedDataViewModel = FilterDataUiModel("312", "Some Sort", "DESC", true)
        val expectedModel = getSortFilterViewModel(selectedDataViewModel)
        val expectedIndex = ProductManageFilterFragment.ITEM_SORT_INDEX

        verifyFilterDataAtIndex(expectedModel, expectedIndex)
    }

    @Test
    fun `when_updateSelect_for_Etalase_should_update_filter_data_model_accordingly`() {
        val sortViewModel = getSortFilterViewModel(getSortDataModel())
        val etalaseDataModel = getEtalaseDataModel()
        val etalaseViewModel = getEtalaseFilterViewModel(etalaseDataModel)
        val categoryViewModel = getCategoryFilterViewModel(getCategoryDataModel())
        val otherFilterViewModel = getOtherFilterFilterViewModel(getOtherFilterDataModel())

        viewModel.updateData(listOf(sortViewModel, etalaseViewModel, categoryViewModel, otherFilterViewModel))
        viewModel.updateSelect(etalaseDataModel, ProductManageFilterMapper.ETALASE_HEADER)

        val selectedDataViewModel = FilterDataUiModel("342", "Some Etalase", "", true)
        val expectedModel = getEtalaseFilterViewModel(selectedDataViewModel)
        val expectedIndex = ProductManageFilterFragment.ITEM_ETALASE_INDEX

        verifyFilterDataAtIndex(expectedModel, expectedIndex)
    }

    @Test
    fun `when_updateSelect_for_Category_should_update_filter_data_model_accordingly`() {
        val sortViewModel = getSortFilterViewModel(getSortDataModel())
        val etalaseViewModel = getEtalaseFilterViewModel(getEtalaseDataModel())
        val categoryDataModel = getCategoryDataModel()
        val categoryViewModel = getCategoryFilterViewModel(categoryDataModel)
        val otherFilterViewModel = getOtherFilterFilterViewModel(getOtherFilterDataModel())

        viewModel.updateData(listOf(sortViewModel, etalaseViewModel, categoryViewModel, otherFilterViewModel))
        viewModel.updateSelect(categoryDataModel)

        val selectedDataViewModel = FilterDataUiModel("1293", "Some Category", "", true)
        val expectedModel = getCategoryFilterViewModel(selectedDataViewModel)
        val expectedIndex = ProductManageFilterFragment.ITEM_CATEGORIES_INDEX

        verifyFilterDataAtIndex(expectedModel, expectedIndex)
    }

    @Test
    fun `when_updateSelect_for_Other_Filter_should_update_filter_data_model_accordingly`() {
        val sortViewModel = getSortFilterViewModel(getSortDataModel())
        val etalaseViewModel = getEtalaseFilterViewModel(getEtalaseDataModel())
        val categoryViewModel = getCategoryFilterViewModel(getCategoryDataModel())
        val otherFilterDataModel = getOtherFilterDataModel()
        val otherFilterViewModel = getOtherFilterFilterViewModel(otherFilterDataModel)

        viewModel.updateData(listOf(sortViewModel, etalaseViewModel, categoryViewModel, otherFilterViewModel))
        viewModel.updateSelect(otherFilterDataModel)

        val selectedDataViewModel = FilterDataUiModel("4183", "Some Other Filter", "", true)
        val expectedModel = getOtherFilterFilterViewModel(selectedDataViewModel)
        val expectedIndex = ProductManageFilterFragment.ITEM_OTHER_FILTER_INDEX

        verifyFilterDataAtIndex(expectedModel, expectedIndex)
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

    private fun getSortDataModel(): FilterDataUiModel {
        return FilterDataUiModel("312", "Some Sort", "DESC", false)
    }

    private fun getSortFilterViewModel(sortDataModel: FilterDataUiModel): FilterUiModel {
        return FilterUiModel(ProductManageFilterMapper.SORT_HEADER,
                mutableListOf(
                        sortDataModel,
                        FilterDataUiModel(
                                "706",
                                "Some Other Sort",
                                "ASC",
                                false)
                ), false)
    }

    private fun getEtalaseDataModel(): FilterDataUiModel {
        return FilterDataUiModel("342", "Some Etalase", "", false)
    }

    private fun getEtalaseFilterViewModel(etalaseDataModel: FilterDataUiModel): FilterUiModel {
        return FilterUiModel(ProductManageFilterMapper.ETALASE_HEADER,
                mutableListOf(
                        etalaseDataModel,
                        FilterDataUiModel(
                                "123",
                                "Some Other Etalase",
                                "",
                                false)
                ), false)
    }

    private fun getCategoryDataModel(): FilterDataUiModel {
        return FilterDataUiModel("1293", "Some Category", "", false)
    }

    private fun getCategoryFilterViewModel(categoryDataModel: FilterDataUiModel): FilterUiModel {
        return FilterUiModel(ProductManageFilterMapper.CATEGORY_HEADER,
                mutableListOf(
                        categoryDataModel,
                        FilterDataUiModel(
                                "4526",
                                "Some Other Category",
                                "",
                                false)
                ), false)
    }

    private fun getOtherFilterDataModel(): FilterDataUiModel {
        return FilterDataUiModel("4183", "Some Other Filter", "", false)
    }

    private fun getOtherFilterFilterViewModel(otherFilterDataModel: FilterDataUiModel): FilterUiModel {
        return FilterUiModel(ProductManageFilterMapper.OTHER_FILTER_HEADER,
                mutableListOf(
                        otherFilterDataModel,
                        FilterDataUiModel(
                                "6139",
                                "Some Other Filter",
                                "",
                                false)
                ), false)
    }

}