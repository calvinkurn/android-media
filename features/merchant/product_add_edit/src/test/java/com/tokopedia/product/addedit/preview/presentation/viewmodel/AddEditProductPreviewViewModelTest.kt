package com.tokopedia.product.addedit.preview.presentation.viewmodel

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantByCatModel
import com.tokopedia.product.addedit.preview.data.source.api.response.Product
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class AddEditProductPreviewViewModelTest: AddEditProductPreviewViewModelTestFixture() {

    @Test
    fun `success get product variant`() {
        val productVariant = ProductVariantByCatModel().apply {
            this.name = "hello"
            this.variantId = 3
        }

        onGetProductVariant_thenReturn(productVariant)
        viewModel.getVariantList("")

        coVerify { getProductVariantUseCase.executeOnBackground() }
        verifyGetProductVariantResult(Success(listOf(productVariant)))
    }

    @Test
    fun `success save and get product draft`() {
        val productDraft = ProductDraft().apply {
            draftId = 1112
            productId = 220
            completionPercent = 100
        }

        onSaveProductDraft_thenReturn(productDraft.draftId)
        viewModel.saveProductDraft(productDraft, productDraft.draftId, false)

        onGetProductDraft_thenReturn(productDraft)
        viewModel.getProductDraft(productDraft.draftId)

        coVerify { saveProductDraftUseCase.executeOnBackground() }
        coVerify { getProductDraftUseCase.executeOnBackground() }

        verifySaveProductDraftResult(Success(productDraft.draftId))
        verifyGetProductDraftResult(Success(productDraft))
    }

    @Test
    fun  `success get product`() {
        val product: Product = Product().copy(
                productID = "01919",
                productName = "mainan",
                price = 1000.toBigInteger()
        )
        onGetProduct_thenReturn(product)
        viewModel.getProduct(product.productID)

        coVerify { getProductUseCase.executeOnBackground() }
        verifyGetProductResult(Success(product))
    }

    @Test
    fun `failed save and get product draft`() {
        onGetProductDraft_thenFailed()
        viewModel.saveProductDraft(ProductDraft(), 3, false)

        onSaveProductDraft_thenFailed()
        viewModel.getProductDraft(3)

        coVerify { saveProductDraftUseCase.executeOnBackground() }
        coVerify { getProductDraftUseCase.executeOnBackground() }

        verifySaveProductDraftFailed()
        verifyGetProductDraftFailed()
    }

    private fun onGetProduct_thenReturn(product: Product) {
        coEvery { getProductUseCase.executeOnBackground() } returns product
    }

    private fun onGetProductVariant_thenReturn(productVariant: ProductVariantByCatModel) {
        coEvery { getProductVariantUseCase.executeOnBackground() } returns listOf(productVariant)
    }

    private fun onGetProductDraft_thenReturn(draft: ProductDraft) {
        coEvery { getProductDraftUseCase.executeOnBackground() } returns draft
    }

    private fun onSaveProductDraft_thenReturn(draftId: Long) {
        coEvery { saveProductDraftUseCase.executeOnBackground() } returns draftId
    }

    private fun onGetProductDraft_thenFailed() {
        coEvery { saveProductDraftUseCase.executeOnBackground() } coAnswers { throw MessageErrorException("")}
    }

    private fun onSaveProductDraft_thenFailed() {
        coEvery { getProductDraftUseCase.executeOnBackground() } coAnswers { throw MessageErrorException("")}
    }

    private fun verifyGetProductResult(expectedResult: Success<Product>) {
        val actualResult = viewModel.getProductResult.value as Success<Product>
        Assert.assertEquals(expectedResult, actualResult)
    }

    private fun verifyGetProductDraftResult(expectedResult: Success<ProductDraft>) {
        val actualResult = viewModel.getProductDraftResult.value as Success<ProductDraft>
        Assert.assertEquals(expectedResult, actualResult)
    }

    private fun verifySaveProductDraftResult(expectedResult: Success<Long>) {
        val actualResult = viewModel.saveProductDraftResultLiveData.value as Success<Long>
        Assert.assertEquals(expectedResult, actualResult)
    }

    private fun verifyGetProductVariantResult(expectedResult: Success<List<ProductVariantByCatModel>>) {
        val actualResult = viewModel.productVariantList.value as Success<List<ProductVariantByCatModel>>
        Assert.assertEquals(expectedResult, actualResult)
    }

    private fun verifySaveProductDraftFailed() {
        val resultSave = viewModel.saveProductDraftResultLiveData.value
        Assert.assertTrue(resultSave is Fail)
    }

    private fun verifyGetProductDraftFailed() {
        val resultGet = viewModel.getProductDraftResult.value
        Assert.assertTrue(resultGet is Fail)
    }
}