package com.tokopedia.shop_showcase.viewmodel.shopshowcaseadd

import com.tokopedia.shop_showcase.shop_showcase_add.data.model.*
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.AppendShopShowcaseProductUseCase
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.CreateShopShowcaseUseCase
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.RemoveShopShowcaseProductUseCase
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.UpdateShopShowcaseUseCase
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
import org.mockito.ArgumentMatchers.anyString

class ShopShowCaseAddViewModelTest : ShopShowCaseAddViewModelTestFixture() {

    @Test
    fun `when add shop showcase should return success`() {
        runBlocking {
            mockkObject(CreateShopShowcaseUseCase)
            onCreateShopShowCase_thenReturn()
            shopShowCaseAddViewModel.addShopShowcase(AddShopShowcaseParam())

            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

            verifySuccessCreateShopShowCaseCalled()

            val expectedValue = Success(AddShopShowcaseResponse())
            assertTrue(shopShowCaseAddViewModel.createShopShowcase.value is Success)
            shopShowCaseAddViewModel.createShopShowcase.verifyValueEquals(expectedValue)
        }
    }

    @Test
    fun `when get selected product list should return success`() {
        runBlocking {
            mockkObject(GetProductListUseCase)
            onGetSelectedProductList_thenReturn()

            val getProductListFilter = GetProductListFilter()
            getProductListFilter.fkeyword = "baju"
            shopShowCaseAddViewModel.getSelectedProductList(filter = getProductListFilter)

            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

            verifySuccessGetSelectedProductListUseCaseCalled(getProductListFilter)

            assertTrue(shopShowCaseAddViewModel.selectedProductList.value is Success)
            assertTrue(shopShowCaseAddViewModel.loaderState.value == false)
        }
    }

    @Test
    fun `when update shop show case should return success`() {
        runBlocking {
            mockkObject(UpdateShopShowcaseUseCase)
            mockkObject(AppendShopShowcaseProductUseCase)
            mockkObject(RemoveShopShowcaseProductUseCase)
            onUpdateShopShowCase_thenReturn()
            shopShowCaseAddViewModel.updateShopShowcase(UpdateShopShowcaseParam(), AppendShowcaseProductParam(), RemoveShowcaseProductParam())

            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

            verifySuccessUpdateShopShowCaseUseCaseCalled()

            assertTrue(shopShowCaseAddViewModel.listOfResponse.value?.isNotEmpty() == true)
        }
    }

    private fun onCreateShopShowCase_thenReturn() {
        coEvery { createShopShowcaseUseCase.executeOnBackground() } returns AddShopShowcaseResponse()
    }

    private fun onGetSelectedProductList_thenReturn() {
        val productList = listOf<Product>()
        val productMapper = ProductMapper()
        val showCaseProductList = productMapper.mapToUIModel(productList)
        coEvery { getProductListUseCase.executeOnBackground() } returns showCaseProductList
    }

    private fun onUpdateShopShowCase_thenReturn() {
        coEvery { updateShopShowcaseUseCase.executeOnBackground() } returns UpdateShopShowcaseResponse()
        coEvery { appendShopShowcaseProductUseCase.executeOnBackground() } returns AppendShowcaseProductResponse()
        coEvery { removeShopShowcaseProductUseCase.executeOnBackground() } returns RemoveShowcaseProductResponse()
    }

    private fun verifySuccessCreateShopShowCaseCalled() {
        verify { CreateShopShowcaseUseCase.createRequestParams(AddShopShowcaseParam()) }
        coVerify { createShopShowcaseUseCase.executeOnBackground() }
    }

    private fun verifySuccessGetSelectedProductListUseCaseCalled(getProductListFilter: GetProductListFilter) {
        verify { GetProductListUseCase.createRequestParams(anyString(), getProductListFilter) }
        coVerify { getProductListUseCase.executeOnBackground() }
    }

    private fun verifySuccessUpdateShopShowCaseUseCaseCalled() {
        verify { UpdateShopShowcaseUseCase.createRequestParams(UpdateShopShowcaseParam()) }
        coVerify { updateShopShowcaseUseCase.executeOnBackground() }

        verify { AppendShopShowcaseProductUseCase.createRequestParams(AppendShowcaseProductParam(), anyString()) }
        coVerify { appendShopShowcaseProductUseCase.executeOnBackground() }

        verify { RemoveShopShowcaseProductUseCase.createRequestParams(RemoveShowcaseProductParam(), anyString()) }
        coVerify { removeShopShowcaseProductUseCase.executeOnBackground() }
    }
}