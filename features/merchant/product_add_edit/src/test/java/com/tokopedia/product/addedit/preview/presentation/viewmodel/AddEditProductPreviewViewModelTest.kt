package com.tokopedia.product.addedit.preview.presentation.viewmodel

import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class AddEditProductPreviewViewModelTest: AddEditProductPreviewViewModelTestFixture() {

    @Test
    fun `when save and get product draft should return result`() {
        runBlocking {
            val productDraft = ProductDraft().apply {
                draftId = 1112
                productId = 220
                completionPercent = 100
                detailInputModel.apply {
                    catalogId = "212"
                    categoryId = "1233"
                    categoryName = "Kelontong"
                    condition = "Great"
                    minOrder = 10
                    productName = "lontong"
                }
                variantInputModel = "variant"
                descriptionInputModel.apply {
                    productDescription = "lontong adalah makanan"
                }
                shipmentInputModel.apply {
                    isMustInsurance = true
                    weight = 100
                    weightUnit = 1
                }
            }

            onSaveProductDraft_thenReturn(productDraft.draftId)
            viewModel.saveProductDraft(productDraft, productDraft.draftId, false)

            onGetProductDraft_thenReturn(productDraft)
            viewModel.getProductDraft(productDraft.draftId)

            val expectedSaveResult = Success(productDraft.draftId)
            val expectedGetResult = Success(productDraft)

            verifySaveProductDraftUseCaseCalled()
            verifyGetProductDraftUseCaseCalled()
            verifySaveProductDraftResult(expectedSaveResult)
            verifyGetProductDraftResult(expectedGetResult)
        }
    }

    @Test
    fun `failed get product draft`() {
        onGetProductDraft_thenReturn()
        viewModel.getProductDraft(3)

        verifyGetProductDraftUseCaseCalled()
        verifyGetProductDraftFailed()
    }

    @Test
    fun `failed save product draft`() {
        onSaveProductDraft_thenReturn()
        viewModel.saveProductDraft(ProductDraft(), 3, false)

        verifySaveProductDraftUseCaseCalled()
        verifySaveProductDraftFailed()
    }

    private suspend fun onGetProductDraft_thenReturn(getProductDraft: ProductDraft) {
        coEvery { getProductDraftUseCase.executeOnBackground() } returns getProductDraft
    }

    private suspend fun onSaveProductDraft_thenReturn(draftId: Long) {
        coEvery { saveProductDraftUseCase.executeOnBackground() } returns draftId
    }

    private fun onGetProductDraft_thenReturn() {
        coEvery { getProductDraftUseCase.executeOnBackground() } throws MessageErrorException("")
    }

    private fun onSaveProductDraft_thenReturn() {
        coEvery { saveProductDraftUseCase.executeOnBackground() } throws MessageErrorException("")
    }

    private fun verifyGetProductDraftUseCaseCalled() {
        coVerify { getProductDraftUseCase.executeOnBackground() }
    }

    private fun verifySaveProductDraftUseCaseCalled() {
        coVerify { saveProductDraftUseCase.executeOnBackground() }
    }

    private fun verifyGetProductDraftResult(expectedResult: Success<ProductDraft>) {
        val actualResult = viewModel.getProductDraftResult.value as Success<ProductDraft>
        Assert.assertEquals(expectedResult, actualResult)
    }

    private fun verifySaveProductDraftResult(expectedResult: Success<Long>) {
        val actualResult = viewModel.saveProductDraftResultLiveData.value as Success<Long>
        Assert.assertEquals(expectedResult, actualResult)
    }

    private fun verifyGetProductDraftFailed() {
        val result = viewModel.getProductDraftResult.value
        Assert.assertTrue(result is Fail)
    }

    private fun verifySaveProductDraftFailed() {
        val result = viewModel.saveProductDraftResultLiveData.value
        Assert.assertTrue(result is Fail)
    }
}