package com.tokopedia.product.manage.feature.filter

import com.tokopedia.core.common.category.domain.model.CategoriesResponse
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionsResponse
import com.tokopedia.product.manage.feature.filter.data.model.ProductListMetaResponse
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterViewModel
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkObject
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
        val filterViewModel = FilterViewModel("Sort", mutableListOf(), false)

        viewModel.updateData(listOf(filterViewModel))
        viewModel.updateShow(filterViewModel)

        val expectedData = FilterViewModel("Sort", mutableListOf(), true)
        verifyFilterViewModel(expectedData)
    }

    private fun onGetProductManageFilterOptions_thenReturn(filterOptionsResponse: FilterOptionsResponse) {
        coEvery { getProductManageFilterOptionsUseCase.executeOnBackground() } returns filterOptionsResponse
    }

    private fun verifyGetProductManageFilterOptionsUseCaseCalled() {
        coVerify { getProductManageFilterOptionsUseCase.executeOnBackground() }
    }

    private fun verifyFilterData(expectedData: MutableList<FilterViewModel>) {
        val actualData = viewModel.filterData.value
        assertEquals(expectedData, actualData)
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

    private fun verifyFilterViewModel(expectedModel: FilterViewModel) {
        val actualModel = viewModel.filterData.value
        actualModel?.first()?.let {
            assertEquals(expectedModel.title, it.title)
            assertEquals(expectedModel.data, it.data)
            assertEquals(expectedModel.isChipsShown, it.isChipsShown)
        }
    }
}