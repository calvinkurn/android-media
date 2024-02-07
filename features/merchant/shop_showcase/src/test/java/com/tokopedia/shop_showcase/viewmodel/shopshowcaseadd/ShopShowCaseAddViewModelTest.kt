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
    fun `update shop showcase name and append 1 product should return Success update showcase name and Success append product`() {
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

            shopShowCaseAddViewModel.updateShowcaseAppendProduct(data = UpdateShopShowcaseParam(), newAppendedProduct = mockAppendedOneProductOnly)
            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            verifySuccessUpdateShopShowCaseNameAndAppendProductUseCaseCalled(appendShowcaseProductParam = mockAppendedOneProductOnly)

            assertTrue(shopShowCaseAddViewModel.listOfAppendResponse.value?.isNotEmpty() == true)
            assertTrue(shopShowCaseAddViewModel.listOfAppendResponse.value?.size == 2)
        }
    }

    @Test
    fun `update shop showcase name and append 1 product return Fail update showcase name and Success append product`() {
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
    fun `update shop showcase name and append 1 product return Success update showcase name and Fail append product`() {
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

    @Test
    fun `update shop showcase name and append 0 product return Success update showcase name`() {
        runBlocking {
            mockkObject(UpdateShopShowcaseUseCase)
            mockkObject(AppendShopShowcaseProductUseCase)
            onUpdateShopShowCaseNameAndAppendProduct_thenReturn()
            val mockAppendProduct = AppendShowcaseProductParam()

            shopShowCaseAddViewModel.updateShowcaseAppendProduct(data = UpdateShopShowcaseParam(), newAppendedProduct = mockAppendProduct)
            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            verifySuccessUpdateShopShowCaseNameUseCaseCalled()

            assertTrue(shopShowCaseAddViewModel.listOfAppendResponse.value?.isNotEmpty() == true)
            assertTrue(shopShowCaseAddViewModel.listOfAppendResponse.value?.get(0) as Result<UpdateShopShowcaseResponse> is Success)
        }
    }

    @Test
    fun `update shop showcase name and append 0 product return Fail update showcase name`() {
        runBlocking {
            mockkObject(UpdateShopShowcaseUseCase)
            mockkObject(AppendShopShowcaseProductUseCase)
            onUpdateShopShowCaseNameAndAppendProduct_thenReturn()
            val mockAppendProduct = AppendShowcaseProductParam()

            coEvery {
                updateShopShowcaseUseCase.executeOnBackground()
            } throws Exception()

            shopShowCaseAddViewModel.updateShowcaseAppendProduct(data = UpdateShopShowcaseParam(), newAppendedProduct = mockAppendProduct)
            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            verifySuccessUpdateShopShowCaseNameUseCaseCalled()

            assertTrue(shopShowCaseAddViewModel.listOfAppendResponse.value?.isNotEmpty() == true)
            assertTrue(shopShowCaseAddViewModel.listOfAppendResponse.value?.get(0) as Result<UpdateShopShowcaseResponse> is Fail)
        }
    }
    // ========================================== //

    // Update showcase name & remove product
    @Test
    fun `update shop showcase name and remove 1 product return Success update showcase name and Success remove product`() {
        runBlocking {
            mockkObject(UpdateShopShowcaseUseCase)
            mockkObject(RemoveShopShowcaseProductUseCase)
            onUpdateShopShowcaseNameAndRemoveProduct_thenReturn()
            val mockRemovedOneProductOnly = RemoveShowcaseProductParam(
                listRemoved = arrayListOf(
                    RemovedProduct(
                        product_id = anyString(),
                        menu_id = anyString()
                    )
                )
            )

            shopShowCaseAddViewModel.updateShowcaseRemoveProduct(data = UpdateShopShowcaseParam(), removedProduct = mockRemovedOneProductOnly)
            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            verifySuccessUpdateShopShowcaseNameAndRemoveProductUseCaseCalled(removedProductParam = mockRemovedOneProductOnly)

            assertTrue(shopShowCaseAddViewModel.listOfRemoveResponse.value?.isNotEmpty() == true)
            assertTrue(shopShowCaseAddViewModel.listOfRemoveResponse.value?.size == 2)
            assertTrue(shopShowCaseAddViewModel.listOfRemoveResponse.value?.get(0) as Result<UpdateShopShowcaseResponse> is Success)
            assertTrue(shopShowCaseAddViewModel.listOfRemoveResponse.value?.get(1) as Result<RemoveShowcaseProductResponse> is Success)
        }
    }

    @Test
    fun `update shop showcase name and remove 1 product return Fail update showcase name and Success remove product`() {
        runBlocking {
            mockkObject(UpdateShopShowcaseUseCase)
            mockkObject(RemoveShopShowcaseProductUseCase)
            onUpdateShopShowcaseNameAndRemoveProduct_thenReturn()
            val mockRemovedOneProductOnly = RemoveShowcaseProductParam(
                listRemoved = arrayListOf(
                    RemovedProduct(
                        product_id = anyString(),
                        menu_id = anyString()
                    )
                )
            )

            coEvery {
                updateShopShowcaseUseCase.executeOnBackground()
            } throws Exception()

            shopShowCaseAddViewModel.updateShowcaseRemoveProduct(data = UpdateShopShowcaseParam(), removedProduct = mockRemovedOneProductOnly)
            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            verifySuccessUpdateShopShowcaseNameAndRemoveProductUseCaseCalled(removedProductParam = mockRemovedOneProductOnly)

            assertTrue(shopShowCaseAddViewModel.listOfRemoveResponse.value?.isNotEmpty() == true)
            assertTrue(shopShowCaseAddViewModel.listOfRemoveResponse.value?.get(0) as Result<UpdateShopShowcaseResponse> is Fail)
            assertTrue(shopShowCaseAddViewModel.listOfRemoveResponse.value?.get(1) as Result<RemoveShowcaseProductResponse> is Success)
        }
    }

    @Test
    fun `update shop showcase name and remove 1 product return Success update showcase name and Fail remove product`() {
        runBlocking {
            mockkObject(UpdateShopShowcaseUseCase)
            mockkObject(RemoveShopShowcaseProductUseCase)
            onUpdateShopShowcaseNameAndRemoveProduct_thenReturn()
            val mockRemovedOneProductOnly = RemoveShowcaseProductParam(
                listRemoved = arrayListOf(
                    RemovedProduct(
                        product_id = anyString(),
                        menu_id = anyString()
                    )
                )
            )

            coEvery {
                removeShopShowcaseProductUseCase.executeOnBackground()
            } throws Exception()

            shopShowCaseAddViewModel.updateShowcaseRemoveProduct(data = UpdateShopShowcaseParam(), removedProduct = mockRemovedOneProductOnly)
            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            verifySuccessUpdateShopShowcaseNameAndRemoveProductUseCaseCalled(removedProductParam = mockRemovedOneProductOnly)

            assertTrue(shopShowCaseAddViewModel.listOfRemoveResponse.value?.isNotEmpty() == true)
            assertTrue(shopShowCaseAddViewModel.listOfRemoveResponse.value?.get(0) as Result<UpdateShopShowcaseResponse> is Success)
            assertTrue(shopShowCaseAddViewModel.listOfRemoveResponse.value?.get(1) as Result<RemoveShowcaseProductResponse> is Fail)
        }
    }

    @Test
    fun `update shop showcase name and remove 0 product return Success update showcase name`() {
        runBlocking {
            mockkObject(UpdateShopShowcaseUseCase)
            mockkObject(RemoveShopShowcaseProductUseCase)
            onUpdateShopShowcaseNameAndRemoveProduct_thenReturn()
            val mockRemovedProduct = RemoveShowcaseProductParam(listRemoved = arrayListOf())

            shopShowCaseAddViewModel.updateShowcaseRemoveProduct(data = UpdateShopShowcaseParam(), removedProduct = mockRemovedProduct)
            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            verifySuccessUpdateShopShowCaseNameUseCaseCalled()

            assertTrue(shopShowCaseAddViewModel.listOfRemoveResponse.value?.isNotEmpty() == true)
            assertTrue(shopShowCaseAddViewModel.listOfRemoveResponse.value?.get(0) as Result<UpdateShopShowcaseResponse> is Success)
        }
    }

    @Test
    fun `update shop showcase name and remove 0 product return Fail update showcase name`() {
        runBlocking {
            mockkObject(UpdateShopShowcaseUseCase)
            mockkObject(RemoveShopShowcaseProductUseCase)
            onUpdateShopShowcaseNameAndRemoveProduct_thenReturn()
            val mockRemovedProduct = RemoveShowcaseProductParam()

            coEvery {
                updateShopShowcaseUseCase.executeOnBackground()
            } throws Exception()

            shopShowCaseAddViewModel.updateShowcaseRemoveProduct(data = UpdateShopShowcaseParam(), removedProduct = mockRemovedProduct)
            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            verifySuccessUpdateShopShowCaseNameUseCaseCalled()

            assertTrue(shopShowCaseAddViewModel.listOfRemoveResponse.value?.isNotEmpty() == true)
            assertTrue(shopShowCaseAddViewModel.listOfRemoveResponse.value?.get(0) as Result<UpdateShopShowcaseResponse> is Fail)
        }
    }
    // ========================================== //

    // Update showcase name, append 1 product & remove 1 product
    @Test
    fun `update shop showcase name, append 1 product and remove 1 product return Success update showcase name, Success append product and Success remove product`() {
        runBlocking {
            mockkObject(UpdateShopShowcaseUseCase)
            mockkObject(AppendShopShowcaseProductUseCase)
            mockkObject(RemoveShopShowcaseProductUseCase)
            onUpdateShopShowcaseNameAppendAndRemoveProduct_thenReturn()
            val mockAppendProduct = AppendShowcaseProductParam(
                listAppended = arrayListOf(
                    AppendedProduct(product_id = anyString(), menu_id = anyString())
                )
            )
            val mockRemovedProduct = RemoveShowcaseProductParam(
                listRemoved = arrayListOf(
                    RemovedProduct(product_id = anyString(), menu_id = anyString())
                )
            )

            shopShowCaseAddViewModel.updateShowcaseAppendAndRemoveProduct(data = UpdateShopShowcaseParam(), newAppendedProduct = mockAppendProduct, removedProduct = mockRemovedProduct)
            shopShowCaseAddViewModel.coroutineContext[Job]?.children?.forEach { it.join() }
            verifySuccessUpdateShopShowCaseNameAppendAndRemoveProductUseCaseCalled(appendShowcaseProductParam = mockAppendProduct, removedProductParam = mockRemovedProduct)

            assertTrue(shopShowCaseAddViewModel.listOfAppendAndRemoveResponse.value?.isNotEmpty() == true)
            assertTrue(shopShowCaseAddViewModel.listOfAppendAndRemoveResponse.value?.get(0) as Result<UpdateShopShowcaseResponse> is Success)
            assertTrue(shopShowCaseAddViewModel.listOfAppendAndRemoveResponse.value?.get(1) as Result<AppendShowcaseProductResponse> is Success)
            assertTrue(shopShowCaseAddViewModel.listOfAppendAndRemoveResponse.value?.get(1) as Result<RemoveShowcaseProductResponse> is Success)
        }
    }

    @Test
    fun `update shop showcase name, append 1 product and remove 1 product return Fail update showcase name, Success append product and Success remove product`() {
    }

    @Test
    fun `update shop showcase name, append 1 product and remove 1 product return Success update showcase name, Fail append product and Success remove product`() {
    }

    @Test
    fun `update shop showcase name, append 1 product and remove 1 product return Success update showcase name, Success append product and Fail remove product`() {
    }
    // ========================================== //

    // Update showcase name, append 0 product & remove 1 product
    @Test
    fun `update shop showcase name, append 0 product and remove 1 product return Success update showcase name and Success remove product`() {
    }

    @Test
    fun `update shop showcase name, append 0 product and remove 1 product return Fail update showcase name and Success remove product`() {
    }

    @Test
    fun `update shop showcase name, append 0 product and remove 1 product return Success update showcase name and Fail remove product`() {
    }
    // ========================================== //

    // Update showcase name, append 1 product & remove 0 product
    @Test
    fun `update shop showcase name, append 1 product and remove 0 product return Success update showcase name and Success append product`() {
    }

    @Test
    fun `update shop showcase name, append 1 product and remove 0 product return Fail update showcase name and Success append product`() {
    }

    @Test
    fun `update shop showcase name, append 1 product and remove 0 product return Success update showcase name and Fail append product`() {
    }
    // ========================================== //

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

    private fun onUpdateShopShowcaseNameAndRemoveProduct_thenReturn() {
        coEvery { updateShopShowcaseUseCase.executeOnBackground() } returns UpdateShopShowcaseResponse()
        coEvery { removeShopShowcaseProductUseCase.executeOnBackground() } returns RemoveShowcaseProductResponse()
    }

    private fun onUpdateShopShowcaseNameAppendAndRemoveProduct_thenReturn() {
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

    private fun verifySuccessUpdateShopShowCaseNameUseCaseCalled() {
        verify { UpdateShopShowcaseUseCase.createRequestParams(UpdateShopShowcaseParam()) }
        coVerify { updateShopShowcaseUseCase.executeOnBackground() }
    }

    private fun verifySuccessUpdateShopShowCaseNameAppendAndRemoveProductUseCaseCalled(appendShowcaseProductParam: AppendShowcaseProductParam, removedProductParam: RemoveShowcaseProductParam) {
        verify { UpdateShopShowcaseUseCase.createRequestParams(UpdateShopShowcaseParam()) }
        coVerify { updateShopShowcaseUseCase.executeOnBackground() }

        verify { AppendShopShowcaseProductUseCase.createRequestParams(appendShowcaseProductParam, anyString()) }
        coVerify { appendShopShowcaseProductUseCase.executeOnBackground() }

        verify { RemoveShopShowcaseProductUseCase.createRequestParams(removedProductParam, anyString()) }
        coVerify { removeShopShowcaseProductUseCase.executeOnBackground() }
    }

    private fun verifySuccessUpdateShopShowCaseNameAndAppendProductUseCaseCalled(appendShowcaseProductParam: AppendShowcaseProductParam) {
        verify { UpdateShopShowcaseUseCase.createRequestParams(UpdateShopShowcaseParam()) }
        coVerify { updateShopShowcaseUseCase.executeOnBackground() }

        verify { AppendShopShowcaseProductUseCase.createRequestParams(appendShowcaseProductParam, anyString()) }
        coVerify { appendShopShowcaseProductUseCase.executeOnBackground() }
    }

    private fun verifySuccessUpdateShopShowcaseNameAndRemoveProductUseCaseCalled(removedProductParam: RemoveShowcaseProductParam) {
        verify { UpdateShopShowcaseUseCase.createRequestParams(UpdateShopShowcaseParam()) }
        coVerify { updateShopShowcaseUseCase.executeOnBackground() }

        verify { RemoveShopShowcaseProductUseCase.createRequestParams(removedProductParam, anyString()) }
        coVerify { removeShopShowcaseProductUseCase.executeOnBackground() }
    }
}
