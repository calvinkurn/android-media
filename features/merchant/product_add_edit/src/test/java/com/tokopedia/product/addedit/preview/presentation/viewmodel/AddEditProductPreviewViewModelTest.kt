package com.tokopedia.product.addedit.preview.presentation.viewmodel

import androidx.lifecycle.MediatorLiveData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.detail.presentation.model.DetailInputModel
import com.tokopedia.product.addedit.detail.presentation.model.PictureInputModel
import com.tokopedia.product.addedit.detail.presentation.model.WholeSaleInputModel
import com.tokopedia.product.addedit.preview.data.source.api.response.Product
import com.tokopedia.product.addedit.preview.presentation.model.ProductInputModel
import com.tokopedia.product.addedit.util.getOrAwaitValue
import com.tokopedia.product.addedit.variant.presentation.model.ProductVariantInputModel
import com.tokopedia.product.manage.common.feature.draft.data.model.ProductDraft
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class AddEditProductPreviewViewModelTest: AddEditProductPreviewViewModelTestFixture() {

    @Test
    fun `When save and get product draft are success Expect can be saved and retrieved data draft`() = runBlocking {
        val productDraft = ProductDraft().apply {
            draftId = 1112
            productId = 220
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
    fun `When save and get product draft are fail Expect can't be saved and retrieved data draft`() = runBlocking {
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
    fun `When get remote product is fail Expect fail object`() = runBlocking {
        onGetProduct_thenFailed()
        viewModel.getProductData("4")

        viewModel.coroutineContext[Job]?.children?.forEach { it.join() }

        coVerify { getProductUseCase.executeOnBackground() }
        verifyGetProductFailed()
    }

    @Test
    fun `When validate product input model Expect return error message`() {
        val detailInputModel = DetailInputModel()

        detailInputModel.categoryId = "123"
        detailInputModel.imageUrlOrPathList = listOf()
        assertEquals(resourceProvider.getInvalidCategoryIdErrorMessage(), viewModel.validateProductInput(detailInputModel))

        detailInputModel.categoryId = "0"
        assertEquals(resourceProvider.getInvalidCategoryIdErrorMessage(), viewModel.validateProductInput(detailInputModel))

        detailInputModel.categoryId = ""
        assertEquals(resourceProvider.getInvalidCategoryIdErrorMessage(), viewModel.validateProductInput(detailInputModel))

        detailInputModel.imageUrlOrPathList = listOf("one","two","three","four","five","six")
        assertEquals(resourceProvider.getInvalidPhotoCountErrorMessage(), viewModel.validateProductInput(detailInputModel))

        every {
            resourceProvider.getInvalidCategoryIdErrorMessage()
        } returns null

        every {
            resourceProvider.getInvalidPhotoCountErrorMessage()
        } returns null

        every {
            resourceProvider.getInvalidPhotoReachErrorMessage()
        } returns null

        detailInputModel.categoryId = "123"
        detailInputModel.imageUrlOrPathList = listOf()
        assertEquals("", viewModel.validateProductInput(detailInputModel))

        detailInputModel.categoryId = "0"
        assertEquals("", viewModel.validateProductInput(detailInputModel))

        detailInputModel.categoryId = ""
        assertEquals("", viewModel.validateProductInput(detailInputModel))

        detailInputModel.imageUrlOrPathList = listOf("one","two","three","four","five","six")
        assertEquals("", viewModel.validateProductInput(detailInputModel))
    }

    @Test
    fun `When set image url path list Expect should set image url path list`() {
        viewModel.productInputModel.value = ProductInputModel()
        viewModel.productInputModel.getOrAwaitValue()

        viewModel.getNewProductInputModel(arrayListOf())
        viewModel.productInputModel.getOrAwaitValue()

        assertTrue(viewModel.productInputModel.value?.detailInputModel != null)
    }

    @Test
    fun `When increase or decrease wholesale Expect should increase or decrease wholesale`() {
        var wholeSaleInputModel = WholeSaleInputModel().apply { quantity = "2" }
        assertEquals("3", viewModel.incrementWholeSaleMinOrder(listOf(wholeSaleInputModel)).last().quantity)

        wholeSaleInputModel = WholeSaleInputModel().apply { quantity = "2" }
        assertEquals("1", viewModel.decrementWholeSaleMinOrder(listOf(wholeSaleInputModel)).last().quantity)
    }

    @Test
    fun `When check product id and is adding Expect should return expected result`() {
        assertEquals("", viewModel.getProductId())
        assertEquals(true , viewModel.isAdding)

        viewModel.setProductId("112")
        assertEquals("112", viewModel.getProductId())
    }

    @Test
    fun `When check draft id Expect should return expected result`() {
        viewModel.setDraftId("10")
        assertEquals(10L , viewModel.getDraftId())

        viewModel.setDraftId("")
        assertEquals(0 , viewModel.getDraftId())
    }

    @Test
    fun `When check is duplicate Expect should return expected result`() {
        viewModel.setIsDuplicate(true)
        assertEquals(true, viewModel.isDuplicate)

        viewModel.isDuplicate = true
        assertEquals(true, viewModel.isDuplicate)
    }

    @Test
    fun `When check is editing Expect should return expected result`() {
        viewModel.setProductId("112")

        viewModel.isEditing.getOrAwaitValue()
        assertEquals(true, viewModel.isEditing.value)
    }

    @Test
    fun `When check product domain Expect should return expected result`() {
        val product = Product(
                productID = "12312",
                productName = "Rice"
        )
        viewModel.productDomain = product
        assertEquals(product, viewModel.productDomain)
    }

    @Test
    fun `When check product input model Expect should return expected result`() {
        val product = MediatorLiveData<ProductInputModel>()
        product.value = ProductInputModel()

        viewModel.productInputModel = product
        assertEquals(product, viewModel.productInputModel)
    }

    @Test
    fun `When check has original variant level Expect should return expected result`() {
        viewModel.hasOriginalVariantLevel = true
        assertEquals(true, viewModel.hasOriginalVariantLevel)
    }

    @Test
    fun `When update product photos Expect updated product photos`() {
        var pictureInputModel = PictureInputModel().apply {
            urlOriginal = "www.blank.com"
            fileName = "apa"
        }
        var product = ProductInputModel().apply {
            detailInputModel.pictureList = listOf(pictureInputModel, pictureInputModel)
            detailInputModel.imageUrlOrPathList = listOf("ada", "apa")
        }
        viewModel.productInputModel.value = product
        viewModel.productInputModel.getOrAwaitValue()

        var imagePickerResult = arrayListOf("pict1","pict2","pict3")
        var originalImageUrl = arrayListOf("www.blank.com","num2","num3")
        var editted = arrayListOf(true,false,true)

        viewModel.updateProductPhotos(imagePickerResult, originalImageUrl, editted)
        viewModel.productInputModel.getOrAwaitValue()

        assertTrue(viewModel.imageUrlOrPathList.value != null)

        pictureInputModel = PictureInputModel().apply {
            urlOriginal = ""
            fileName = "apa"
        }
        product = ProductInputModel().apply {
            detailInputModel.pictureList = listOf(pictureInputModel, pictureInputModel)
            detailInputModel.imageUrlOrPathList = listOf("ada", "apa")
        }
        viewModel.productInputModel.value = product
        viewModel.productInputModel.getOrAwaitValue()

        imagePickerResult = arrayListOf("pict1","pict2","pict3")
        originalImageUrl = arrayListOf("www.blank.com","num2","num3")
        editted = arrayListOf(true,false,true)

        viewModel.updateProductPhotos(imagePickerResult, originalImageUrl, editted)
        viewModel.productInputModel.getOrAwaitValue()

        assertTrue(viewModel.imageUrlOrPathList.value != null)
    }

    @Test
    fun `When update product status Expect should update product status`() {
        val product = MediatorLiveData<ProductInputModel>()
        product.value = null
        viewModel.productInputModel = product
        viewModel.updateProductStatus(true)
        assertEquals(null, viewModel.productInputModel.value)

        val productVariantInputModel = ProductVariantInputModel().apply { status = "INACTIVE" }
        viewModel.productInputModel.value = ProductInputModel().apply {
            draftId = 109
            productId = 211
            detailInputModel.productName = "mainan"
            detailInputModel.status = 0
            variantInputModel.products = listOf(productVariantInputModel)
        }
        viewModel.productInputModel.getOrAwaitValue()

        viewModel.updateProductStatus(true)
        viewModel.productInputModel.getOrAwaitValue()
        var actualResult = viewModel.productInputModel.value?.detailInputModel?.status
        assertNotEquals(true, actualResult)

        viewModel.updateProductStatus(false)
        viewModel.productInputModel.getOrAwaitValue()
        actualResult = viewModel.productInputModel.value?.detailInputModel?.status
        assertNotEquals(false, actualResult)
    }

    @Test
    fun `When productInputModel data changed Expect `() {
        viewModel.productInputModel.value = ProductInputModel()
        viewModel.productInputModel.getOrAwaitValue()

        viewModel.setIsDataChanged(true)
        assertTrue(viewModel.getIsDataChanged())

        viewModel.setIsDataChanged(false)
        assertFalse(viewModel.getIsDataChanged())
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
        viewModel.isLoading.getOrAwaitValue()
        assertEquals(true, viewModel.isVariantEmpty.value)
        assertEquals(false, viewModel.isLoading.value)
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