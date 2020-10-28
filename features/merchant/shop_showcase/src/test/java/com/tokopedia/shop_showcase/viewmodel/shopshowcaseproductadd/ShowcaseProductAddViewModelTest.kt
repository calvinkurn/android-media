package com.tokopedia.shop_showcase.viewmodel.shopshowcaseproductadd

import com.tokopedia.shop_showcase.shop_showcase_product_add.data.model.Product
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.mapper.ProductMapper
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GetProductListFilter
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.usecase.GetProductListUseCase
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkObject
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.ArgumentMatchers

class ShowcaseProductAddViewModelTest : ShowcaseProductAddViewModelTestFixture() {

    @Test
    fun `when update shop show case with load more is false should return success`() {
        runBlocking {
            mockkObject(GetProductListUseCase)
            onGetSelectedProductList_thenReturn()

            val getProductListFilter = GetProductListFilter()
            getProductListFilter.fkeyword = "baju"
            showcaseProductAddViewModel.getProductList(filter = getProductListFilter)

            showcaseProductAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

            verifySuccessGetSelectedProductListUseCaseCalled(getProductListFilter)

            assertTrue(showcaseProductAddViewModel.productList.value is Success)
            assertTrue(showcaseProductAddViewModel.fetchingState.value == false)
        }
    }

    @Test
    fun `when update shop show case with load more is true should return success`() {
        runBlocking {
            mockkObject(GetProductListUseCase)
            onGetSelectedProductList_thenReturn()

            val getProductListFilter = GetProductListFilter()
            getProductListFilter.fkeyword = "baju"
            showcaseProductAddViewModel.getProductList(filter = getProductListFilter, isLoadMore = true)

            showcaseProductAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

            verifySuccessGetSelectedProductListUseCaseCalled(getProductListFilter)

            assertTrue(showcaseProductAddViewModel.productList.value is Success)
            assertTrue(showcaseProductAddViewModel.loadingState.value == false)
        }
    }

    private fun onGetSelectedProductList_thenReturn() {
        val productList = listOf<Product>()
        val productMapper = ProductMapper()
        val showCaseProductList = productMapper.mapToUIModel(productList)
        coEvery { getProductListUseCase.executeOnBackground() } returns showCaseProductList
    }


    private fun verifySuccessGetSelectedProductListUseCaseCalled(getProductListFilter: GetProductListFilter) {
        verify { GetProductListUseCase.createRequestParams(ArgumentMatchers.anyString(), getProductListFilter) }
        coVerify { getProductListUseCase.executeOnBackground() }
    }
}