package com.tokopedia.product.addedit.draft.presentation.viewmodel

import com.tokopedia.product.addedit.util.getOrAwaitValue
import com.tokopedia.product.manage.common.feature.draft.data.model.ProductDraft
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@ExperimentalCoroutinesApi
class AddEditProductDraftViewModelTest: AddEditProductDraftViewModelTestFixture() {

    @Test
    fun  `When get all product draft should be successful`(){
        runBlocking {
            val mockData = listOf(ProductDraft())

            coEvery {
                getAllProductDraftUseCase.executeOnBackground()
            } returns mockData

            viewModel.getAllProductDraft()

            viewModel.drafts.getOrAwaitValue()

            coVerify {
                getAllProductDraftUseCase.executeOnBackground()
            }

            viewModel.drafts.value?.let {
                verifySuccessResult(mockData, it)
            }
        }
    }

    @Test
    fun  `When delete product draft should be successful`(){
        runBlocking {
            val mockData = true

            coEvery {
                deleteProductDraftUseCase.executeOnBackground()
            } returns mockData

            viewModel.deleteProductDraft(1234)

            viewModel.deleteDraft.getOrAwaitValue()

            coVerify {
                deleteProductDraftUseCase.executeOnBackground()
            }

            viewModel.deleteDraft.value?.let {
                verifySuccessResult(mockData, it)
            }
        }
    }

    @Test
    fun  `When delete all product drafts should be successful`(){
        runBlocking {
            val mockData = true

            coEvery {
                deleteAllProductDraftUseCase.executeOnBackground()
            } returns mockData

            viewModel.deleteAllProductDraft()

            viewModel.deleteAllDraft.getOrAwaitValue()

            coVerify {
                deleteAllProductDraftUseCase.executeOnBackground()
            }

            viewModel.deleteAllDraft.value?.let {
                verifySuccessResult(mockData, it)
            }
        }
    }

    private fun verifySuccessResult(prevData: Any, currentData: Any) {
        Assert.assertEquals(Success(prevData), currentData)
    }

}