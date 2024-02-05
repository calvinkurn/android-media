package com.tokopedia.shop_showcase.viewmodel.shopshowcaseadd

import com.tokopedia.shop_showcase.shop_showcase_add.data.model.*
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.AppendShopShowcaseProductUseCase
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.CreateShopShowcaseUseCase
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.RemoveShopShowcaseProductUseCase
import com.tokopedia.shop_showcase.shop_showcase_add.domain.usecase.UpdateShopShowcaseUseCase
import com.tokopedia.shop_showcase.shop_showcase_product_add.data.model.ProductListResponse
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.mapper.ProductMapper
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.model.GetProductListFilter
import com.tokopedia.shop_showcase.shop_showcase_product_add.domain.usecase.GetProductListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
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
    fun `Add Shop Showcase fail scenario`() {
        runBlocking {
            mockkObject(CreateShopShowcaseUseCase)
            onCreateShopShowCase_thenReturn()

            coEvery { createShopShowcaseUseCase.executeOnBackground() } throws Exception()

            shopShowCaseAddViewModel.addShopShowcase(AddShopShowcaseParam())
            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

            verifySuccessCreateShopShowCaseCalled()

            assertTrue(shopShowCaseAddViewModel.createShopShowcase.value is Fail)
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
    fun `when get selected product list should return success with load more is true`() {
        runBlocking {
            mockkObject(GetProductListUseCase)
            onGetSelectedProductList_thenReturn()

            val getProductListFilter = GetProductListFilter()
            getProductListFilter.fkeyword = "baju"
            shopShowCaseAddViewModel.getSelectedProductList(filter = getProductListFilter, isLoadMore = true)

            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

            verifySuccessGetSelectedProductListUseCaseCalled(getProductListFilter)

            assertTrue(shopShowCaseAddViewModel.selectedProductList.value is Success)
        }
    }

    @Test
    fun `Get Selected Product List Fail scenario`() {
        runBlocking {
            mockkObject(GetProductListUseCase)
            onGetSelectedProductList_thenReturn()

            coEvery { getProductListUseCase.executeOnBackground() } throws Exception()

            val getProductListFilter = GetProductListFilter()
            getProductListFilter.fkeyword = "baju"
            shopShowCaseAddViewModel.getSelectedProductList(filter = getProductListFilter)

            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

            verifySuccessGetSelectedProductListUseCaseCalled(getProductListFilter)

            assertTrue(shopShowCaseAddViewModel.selectedProductList.value is Fail)
        }
    }

    @Test
    fun `when update shop show case should return success`() {
        runBlocking {
            mockkObject(UpdateShopShowcaseUseCase)
            mockkObject(AppendShopShowcaseProductUseCase)
            mockkObject(RemoveShopShowcaseProductUseCase)
            onUpdateShopShowCase_thenReturn()
            // shopShowCaseAddViewModel.updateShopShowcase(UpdateShopShowcaseParam(), AppendShowcaseProductParam(), RemoveShowcaseProductParam())

            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

            verifySuccessUpdateShopShowCaseUseCaseCalled()

//            assertTrue(shopShowCaseAddViewModel.listOfResponse.value?.isNotEmpty() == true)
        }
    }

    // ========= Update showcase name only ========= //
    @Test
    fun `when update shop showcase name should return success`() {
        runBlocking {
            mockkObject(UpdateShopShowcaseUseCase)
            onUpdateShopShowCaseName_thenReturn()
            shopShowCaseAddViewModel.updateShowcaseName(UpdateShopShowcaseParam())

            verifySuccessUpdateShopShowCaseNameUseCaseCalled()

            assertTrue(shopShowCaseAddViewModel.listOfUpdateShowcaseNameResponse.value?.isNotEmpty() == true)
        }
    }

    @Test
    fun `update shop showcase name return Fail`() {
        runBlocking {
            mockkObject(UpdateShopShowcaseUseCase)
            onUpdateShopShowCaseName_thenReturn()

            coEvery {
                updateShopShowcaseUseCase.executeOnBackground()
            } throws Exception()

            shopShowCaseAddViewModel.updateShowcaseName(data = UpdateShopShowcaseParam())
            verifySuccessUpdateShopShowCaseNameUseCaseCalled()

            assertTrue(shopShowCaseAddViewModel.listOfUpdateShowcaseNameResponse.value?.isNotEmpty() == true)
            assertTrue(shopShowCaseAddViewModel.listOfUpdateShowcaseNameResponse.value?.get(0) as Result<UpdateShopShowcaseResponse> is Fail)
        }
    }
    // ========================================== //

    // Update showcase name & append product
    @Test
    fun `update shop showcase name and append product should return success`() {
        runBlocking {
            mockkObject(UpdateShopShowcaseUseCase)
            mockkObject(AppendShopShowcaseProductUseCase)
            onUpdateShopShowCaseNameAndAppendProduct_thenReturn()

            val mockAppendedOneProductOnly = AppendShowcaseProductParam(
                listAppended = arrayListOf(
                    AppendedProduct(
                        product_id = anyString(),
                        menu_id = anyString()
                    )
                )
//                listAppended = for (i in  )
            )

            shopShowCaseAddViewModel.updateShowcaseAppendProduct(data = UpdateShopShowcaseParam(), newAppendedProduct = mockAppendedOneProductOnly)
            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            verifySuccessUpdateShopShowCaseNameAndAppendProductUseCaseCalled(appendShowcaseProductParam = mockAppendedOneProductOnly)

            assertTrue(shopShowCaseAddViewModel.listOfAppendResponse.value?.isNotEmpty() == true)
        }
    }

    @Test
    fun `update shop showcase name and append product return Fail update showcase name`() {
        runBlocking {
            mockkObject(UpdateShopShowcaseUseCase)
            mockkObject(AppendShopShowcaseProductUseCase)
            onUpdateShopShowCaseNameAndAppendProduct_thenReturn()
            val mockAppendedOneProductOnly = AppendShowcaseProductParam(
                listAppended = arrayListOf(
                    AppendedProduct(
                        product_id = anyString(),
                        menu_id = anyString()
                    )
                )
            )

            coEvery {
                updateShopShowcaseUseCase.executeOnBackground()
            } throws Exception()

            shopShowCaseAddViewModel.updateShowcaseAppendProduct(data = UpdateShopShowcaseParam(), newAppendedProduct = mockAppendedOneProductOnly)
            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            verifySuccessUpdateShopShowCaseNameAndAppendProductUseCaseCalled(appendShowcaseProductParam = mockAppendedOneProductOnly)

            assertTrue(shopShowCaseAddViewModel.listOfAppendResponse.value?.isNotEmpty() == true)
            assertTrue(shopShowCaseAddViewModel.listOfAppendResponse.value?.get(0) as Result<UpdateShopShowcaseResponse> is Fail)
            assertTrue(shopShowCaseAddViewModel.listOfAppendResponse.value?.get(1) as Result<AppendShowcaseProductResponse> is Success)
        }
    }

    @Test
    fun `update shop showcase name and append product return Fail append product`() {
        runBlocking {
            mockkObject(UpdateShopShowcaseUseCase)
            mockkObject(AppendShopShowcaseProductUseCase)
            onUpdateShopShowCaseNameAndAppendProduct_thenReturn()
            val mockAppendedOneProductOnly = AppendShowcaseProductParam(
                listAppended = arrayListOf(
                    AppendedProduct(
                        product_id = anyString(),
                        menu_id = anyString()
                    )
                )
            )

            coEvery {
                appendShopShowcaseProductUseCase.executeOnBackground()
            } throws Exception()

            shopShowCaseAddViewModel.updateShowcaseAppendProduct(data = UpdateShopShowcaseParam(), newAppendedProduct = mockAppendedOneProductOnly)
            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            verifySuccessUpdateShopShowCaseNameAndAppendProductUseCaseCalled(appendShowcaseProductParam = mockAppendedOneProductOnly)

            assertTrue(shopShowCaseAddViewModel.listOfAppendResponse.value?.isNotEmpty() == true)
            assertTrue(shopShowCaseAddViewModel.listOfAppendResponse.value?.get(0) as Result<UpdateShopShowcaseResponse> is Success)
            assertTrue(shopShowCaseAddViewModel.listOfAppendResponse.value?.get(1) as Result<AppendShowcaseProductResponse> is Fail)
        }
    }
    // ========================================== //

    @Test
    fun `Update Shop Showcase Fail Scenario`() {
        runBlocking {
            mockkObject(UpdateShopShowcaseUseCase)
            mockkObject(AppendShopShowcaseProductUseCase)
            mockkObject(RemoveShopShowcaseProductUseCase)
            onUpdateShopShowCase_thenReturn()

            coEvery {
                updateShopShowcaseUseCase.executeOnBackground()
            } throws Exception()

            // shopShowCaseAddViewModel.updateShopShowcase(UpdateShopShowcaseParam(), AppendShowcaseProductParam(), RemoveShowcaseProductParam())
            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

            verifySuccessUpdateShopShowCaseUseCaseCalled()

//            assertTrue((shopShowCaseAddViewModel.listOfResponse.value?.get(0) as Result<UpdateShopShowcaseResponse>) is Fail)
//            assertTrue((shopShowCaseAddViewModel.listOfResponse.value?.get(1) as Result<AppendShowcaseProductResponse>) is Success)
//            assertTrue((shopShowCaseAddViewModel.listOfResponse.value?.get(2) as Result<RemoveShowcaseProductResponse>) is Success)
//            assertTrue(shopShowCaseAddViewModel.listOfResponse.value?.isNotEmpty() == true)
        }
    }

    @Test
    fun `Append new Shop Showcase Products When Update Fail Scenario`() {
        runBlocking {
            mockkObject(UpdateShopShowcaseUseCase)
            mockkObject(AppendShopShowcaseProductUseCase)
            mockkObject(RemoveShopShowcaseProductUseCase)
            onUpdateShopShowCase_thenReturn()

            coEvery {
                appendShopShowcaseProductUseCase.executeOnBackground()
            } throws Exception()

            // shopShowCaseAddViewModel.updateShopShowcase(UpdateShopShowcaseParam(), AppendShowcaseProductParam(), RemoveShowcaseProductParam())
            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

            verifySuccessUpdateShopShowCaseUseCaseCalled()

//            assertTrue((shopShowCaseAddViewModel.listOfResponse.value?.get(0) as Result<UpdateShopShowcaseResponse>) is Success)
//            assertTrue((shopShowCaseAddViewModel.listOfResponse.value?.get(1) as Result<AppendShowcaseProductResponse>) is Fail)
//            assertTrue((shopShowCaseAddViewModel.listOfResponse.value?.get(2) as Result<RemoveShowcaseProductResponse>) is Success)
//            assertTrue(shopShowCaseAddViewModel.listOfResponse.value?.isNotEmpty() == true)
        }
    }

    @Test
    fun `Remove Shop Showcase Products When Update Fail Scenario`() {
        runBlocking {
            mockkObject(UpdateShopShowcaseUseCase)
            mockkObject(AppendShopShowcaseProductUseCase)
            mockkObject(RemoveShopShowcaseProductUseCase)
            onUpdateShopShowCase_thenReturn()

            coEvery {
                removeShopShowcaseProductUseCase.executeOnBackground()
            } throws Exception()

            // shopShowCaseAddViewModel.updateShopShowcase(UpdateShopShowcaseParam(), AppendShowcaseProductParam(), RemoveShowcaseProductParam())
            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }

            verifySuccessUpdateShopShowCaseUseCaseCalled()

//            assertTrue((shopShowCaseAddViewModel.listOfResponse.value?.get(0) as Result<UpdateShopShowcaseResponse>) is Success)
//            assertTrue((shopShowCaseAddViewModel.listOfResponse.value?.get(1) as Result<AppendShowcaseProductResponse>) is Success)
//            assertTrue((shopShowCaseAddViewModel.listOfResponse.value?.get(2) as Result<RemoveShowcaseProductResponse>) is Fail)
//            assertTrue(shopShowCaseAddViewModel.listOfResponse.value?.isNotEmpty() == true)
        }
    }

    private fun onCreateShopShowCase_thenReturn() {
        coEvery { createShopShowcaseUseCase.executeOnBackground() } returns AddShopShowcaseResponse()
    }

    private fun onGetSelectedProductList_thenReturn() {
        val productList = listOf<ProductListResponse.ProductList.Data>()
        val productMapper = ProductMapper()
        val showCaseProductList = productMapper.mapToUIModel(productList)
        coEvery { getProductListUseCase.executeOnBackground() } returns showCaseProductList
    }

    private fun onUpdateShopShowCaseName_thenReturn() {
        coEvery { updateShopShowcaseUseCase.executeOnBackground() } returns UpdateShopShowcaseResponse()
    }

    private fun onUpdateShopShowCaseNameAndAppendProduct_thenReturn() {
        coEvery { updateShopShowcaseUseCase.executeOnBackground() } returns UpdateShopShowcaseResponse()
        coEvery { appendShopShowcaseProductUseCase.executeOnBackground() } returns AppendShowcaseProductResponse()
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

    private fun verifySuccessUpdateShopShowCaseNameUseCaseCalled() {
        verify { UpdateShopShowcaseUseCase.createRequestParams(UpdateShopShowcaseParam()) }
        coVerify { updateShopShowcaseUseCase.executeOnBackground() }
    }

    private fun verifySuccessUpdateShopShowCaseNameAndAppendProductUseCaseCalled(appendShowcaseProductParam: AppendShowcaseProductParam) {
        verify { UpdateShopShowcaseUseCase.createRequestParams(UpdateShopShowcaseParam()) }
        coVerify { updateShopShowcaseUseCase.executeOnBackground() }

        verify { AppendShopShowcaseProductUseCase.createRequestParams(appendShowcaseProductParam, anyString()) }
        coVerify { appendShopShowcaseProductUseCase.executeOnBackground() }
    }
}
