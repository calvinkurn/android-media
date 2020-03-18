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

    private fun verifyFilterOptionsResponse(expectedResponse: Success<FilterOptionsResponse>) {
        val actualResponse = viewModel.filterOptionsResponse.value as Success<FilterOptionsResponse>
        assertEquals(expectedResponse, actualResponse)
    }
}