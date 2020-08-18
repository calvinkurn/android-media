package com.tokopedia.product.addedit.preview.presentation.viewmodel

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.preview.data.source.api.response.Product
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.util.getOrAwaitValue
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test


@ExperimentalCoroutinesApi
class AddEditProductPreviewViewModelTest: AddEditProductPreviewViewModelTestFixture() {

    @Test
    fun `When SuccessSaveAndGetProductDraft Expect ExpectedBehaviour`() = runBlocking {
        val productDraft = ProductDraft().apply {
            draftId = 1112
            productId = 220
            completionPercent = 100
        }

        onSaveProductDraft_thenReturn(productDraft.draftId)
        viewModel.saveProductDraft(productDraft, productDraft.draftId, false)

        onGetProductDraft_thenReturn(productDraft)
        viewModel.getProductDraft(productDraft.draftId)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify { saveProductDraftUseCase.executeOnBackground() }
        coVerify { getProductDraftUseCase.executeOnBackground() }

        verifySaveProductDraftResult(Success(productDraft.draftId))
        verifyGetProductDraftResult(Success(productDraft))
    }

    @Test
    fun  `When get remote product is success Expect set product input model`() = runBlocking {
        val product: Product = Product().copy(
                productID = "01919",
                productName = "mainan",
                price = 1000.toBigInteger()
        )
        onGetProduct_thenReturn(product)
        viewModel.getProductData(product.productID)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify { getProductUseCase.executeOnBackground() }
        verifyGetProductResult(Success(product))
    }

    @Test
    fun `When FailedSaveAndGetProductDraft Expect ExpectedBehaviour`() = runBlocking {
        onSaveProductDraft_thenFailed()
        viewModel.saveProductDraft(ProductDraft(), 3, false)

        onGetProductDraft_thenFailed()
        viewModel.getProductDraft(3)

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify { saveProductDraftUseCase.executeOnBackground() }
        coVerify { getProductDraftUseCase.executeOnBackground() }

        verifySaveProductDraftFailed()
        verifyGetProductDraftFailed()
    }

    @Test
    fun `When get remote product is failed Expect fail object`() = runBlocking {
        onGetProduct_thenFailed()
        viewModel.getProductData("4")

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify { getProductUseCase.executeOnBackground() }
        verifyGetProductFailed()
    }

    @Test
    fun `When product input model is valid Expect empty string`() {
        val detailInputModel = DetailInputModel()

        assertEquals(resourceProvider.getInvalidCategoryIdErrorMessage(), viewModel.validateProductInput(detailInputModel))
        detailInputModel.categoryId = "1234"

        assertEquals(resourceProvider.getInvalidPhotoCountErrorMessage(), viewModel.validateProductInput(detailInputModel))
        detailInputModel.imageUrlOrPathList = listOf("one","two","three","four","five","six")

        assertEquals(resourceProvider.getInvalidPhotoReachErrorMessage(), viewModel.validateProductInput(detailInputModel))
        detailInputModel.imageUrlOrPathList = listOf("one","two","three")

        assertEquals("", viewModel.validateProductInput(detailInputModel))
    }

    @Test
    fun `When CheckUpdateProductInputModel Expect ExpectedBehaviour`() {
        viewModel.productInputModel.value = ProductInputModel()
        viewModel.productInputModel.getOrAwaitValue()

        viewModel.getNewProductInputModel(arrayListOf())
        viewModel.productInputModel.getOrAwaitValue()

        assertTrue(viewModel.productInputModel.value?.detailInputModel != null)
    }

    @Test
    fun `When wholesale increment and decrement Expect valid wholesale`() {
        var wholeSaleInputModel = WholeSaleInputModel().apply { quantity = "2" }
        assertEquals("3", viewModel.incrementWholeSaleMinOrder(listOf(wholeSaleInputModel)).last().quantity)

        wholeSaleInputModel = WholeSaleInputModel().apply { quantity = "2" }
        assertEquals("1", viewModel.decrementWholeSaleMinOrder(listOf(wholeSaleInputModel)).last().quantity)
    }

    @Test
    fun `When check variables on preview page Expect valid variables`() {
        viewModel.setProductId("112")
        viewModel.setDraftId("10")
        viewModel.setIsDuplicate(true)
        viewModel.hasOriginalVariantLevel = true

        viewModel.productInputModel.value = ProductInputModel().apply { detailInputModel.wholesaleList = listOf(WholeSaleInputModel()) }
        viewModel.productInputModel.getOrAwaitValue()

        assertEquals(false, viewModel.isAdding)
        assertEquals(true, viewModel.hasOriginalVariantLevel)
        assertEquals(true, viewModel.isDuplicate)
        assertEquals("112", viewModel.getProductId())
        assertEquals(10L , viewModel.getDraftId())
        assertEquals(true, viewModel.hasWholesale)
    }

    @Test
    fun `When update product photos Expect updated product photos`() {
        val pictureInputModel = PictureInputModel().apply { fileName = "apa" }
        val product = ProductInputModel().apply {
            detailInputModel.pictureList = listOf(pictureInputModel)
            detailInputModel.imageUrlOrPathList = listOf("ada", "apa")
        }
        viewModel.productInputModel.value = product
        viewModel.productInputModel.getOrAwaitValue()

        val imagePickerResult = arrayListOf("pict1","pict2","pict3")
        val originalImageUrl = arrayListOf("www.blank.com","num2","num3")
        val editted = arrayListOf(true,false,true)

        viewModel.updateProductPhotos(imagePickerResult, originalImageUrl, editted)
        viewModel.productInputModel.getOrAwaitValue()

        assertTrue(viewModel.imageUrlOrPathList.value != null)
    }

    @Test
    fun `When update product status Expect updated product status`() {
        val productVariantInputModel = ProductVariantInputModel().apply { status = "INACTIVE" }
        val product = ProductInputModel().apply {
            draftId = 109
            productId = 211
            detailInputModel.productName = "mainan"
            detailInputModel.status = 0
            variantInputModel.products = listOf(productVariantInputModel)
        }
        viewModel.productInputModel.value = product
        viewModel.productInputModel.getOrAwaitValue()
        val expectedResult = viewModel.productInputModel.value?.detailInputModel?.status

        viewModel.updateProductStatus(true)
        viewModel.productInputModel.getOrAwaitValue()
        val actualResult = viewModel.productInputModel.value?.detailInputModel?.status

        assertNotEquals(expectedResult, actualResult)
    }

    @Test
    fun `When get status stock variant Expect status stock variant`() {
        viewModel.productInputModel.value = ProductInputModel().apply { detailInputModel.stock = 0 }
        viewModel.productInputModel.getOrAwaitValue()
        assertEquals(1, viewModel.getStatusStockViewVariant())

        viewModel.productInputModel.value = ProductInputModel().apply { detailInputModel.stock = 4 }
        viewModel.productInputModel.getOrAwaitValue()
        assertEquals(2, viewModel.getStatusStockViewVariant())

        viewModel.productInputModel.value = ProductInputModel().apply { detailInputModel.status = 0 }
        viewModel.productInputModel.getOrAwaitValue()
        assertEquals(3, viewModel.getStatusStockViewVariant())
    }

    private fun onGetProductDraft_thenReturn(draft: ProductDraft) {
        coEvery { getProductDraftUseCase.executeOnBackground() } returns draft
    }

    private fun onSaveProductDraft_thenReturn(draftId: Long) {
        coEvery { saveProductDraftUseCase.executeOnBackground() } returns draftId
    }

    private fun onGetProduct_thenReturn(product: Product) {
        coEvery { getProductUseCase.executeOnBackground() } returns product
    }

    private fun onSaveProductDraft_thenFailed() {
        coEvery { saveProductDraftUseCase.executeOnBackground() } throws MessageErrorException("")
    }

    private fun onGetProductDraft_thenFailed() {
        coEvery { getProductDraftUseCase.executeOnBackground() } throws MessageErrorException("")
    }

    private fun onGetProduct_thenFailed() {
        coEvery { getProductUseCase.executeOnBackground() } throws MessageErrorException("")
    }

    private fun verifyGetProductDraftResult(expectedResult: Success<ProductDraft>) {
        val actualResult = viewModel.getProductDraftResult.value as Success<ProductDraft>
        assertEquals(expectedResult, actualResult)
    }

    private fun verifySaveProductDraftResult(expectedResult: Success<Long>) {
        val actualResult = viewModel.saveProductDraftResultLiveData.value as Success<Long>
        assertEquals(expectedResult, actualResult)
    }

    private fun verifyGetProductResult(expectedResult: Success<Product>) {
        val actualResult = viewModel.getProductResult.value as Success<Product>
        assertEquals(expectedResult, actualResult)

        viewModel.isVariantEmpty.getOrAwaitValue()
        assertEquals(true, viewModel.isVariantEmpty.value)
    }

    private fun verifySaveProductDraftFailed() {
        val result = viewModel.saveProductDraftResultLiveData.value
        assertTrue(result is Fail)
    }

    private fun verifyGetProductDraftFailed() {
        val result = viewModel.getProductDraftResult.value
        assertTrue(result is Fail)
    }

    private fun verifyGetProductFailed() {
        val result = viewModel.getProductResult.value
        assertTrue(result is Fail)

        viewModel.isVariantEmpty.getOrAwaitValue()
        assertEquals(true, viewModel.isVariantEmpty.value)
    }
}