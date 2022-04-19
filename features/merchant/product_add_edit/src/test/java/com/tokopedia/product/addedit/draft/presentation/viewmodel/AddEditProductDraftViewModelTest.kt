package com.tokopedia.product.addedit.draft.presentation.viewmodel

import com.tokopedia.product.addedit.util.getOrAwaitValue
import com.tokopedia.product.manage.common.feature.draft.data.model.ProductDraft
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import java.io.IOException

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
    fun `When get all product drafts error, should post error to observer`() = runBlocking {
        coEvery {
            getAllProductDraftUseCase.executeOnBackground()
        } throws IOException()

        viewModel.getAllProductDraft()

        coVerify {
            getAllProductDraftUseCase.executeOnBackground()
        }

        assert(viewModel.drafts.value is Fail)
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
    fun `When delete product draft error, should post error to observer`() = runBlocking {
        coEvery {
            deleteProductDraftUseCase.executeOnBackground()
        } throws IOException()

        viewModel.deleteProductDraft(1234)

        coVerify {
            deleteProductDraftUseCase.executeOnBackground()
        }

        assert(viewModel.deleteDraft.value is Fail)
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

    @Test
    fun `When delete all product draft error, should post error to observer`() = runBlocking {
        coEvery {
            deleteAllProductDraftUseCase.executeOnBackground()
        } throws IOException()

        viewModel.deleteAllProductDraft()

        coVerify {
            deleteAllProductDraftUseCase.executeOnBackground()
        }

        assert(viewModel.deleteAllDraft.value is Fail)
    }

    private fun verifySuccessResult(prevData: Any, currentData: Any) {
        Assert.assertEquals(Success(prevData), currentData)
    }

}