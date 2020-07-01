package com.tokopedia.seller.search.suggestionsearch

import com.tokopedia.seller.search.common.domain.model.SellerSearchResponse
import com.tokopedia.seller.search.feature.suggestion.domain.model.SuccessSearchResponse
import com.tokopedia.seller.search.feature.suggestion.view.model.registersearch.RegisterSearchUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString

class SuggestionSearchViewModelTest: SuggestionSearchViewModelTestFixture() {

    @Test
    fun `when get history search list should return success`() {
        runBlocking {
            onGetSellerSearch_thenReturn()
            viewModel.getSellerSearch(keyword = anyString(), shopId = anyString())

            verifySuccessGetSellerSearchUseCaseCaseCalled()
            assertTrue(viewModel.getSellerSearch.value is Success)
            assertNotNull((viewModel.getSellerSearch.value as Success).data)
        }
    }

    @Test
    fun `when insert success search suggestion should return success`() {
        runBlocking {
            onInsertSuccessSearch_thenReturn()
            viewModel.insertSearchSeller(anyString(), anyString(), anyString(), anyInt())

            verifyInsertSuccessSearchUseCaseCalled()
            val expectedValue = Success(RegisterSearchUiModel())
            assertTrue(viewModel.insertSuccessSearch.value is Success)
            viewModel.insertSuccessSearch.verifyValueEquals(expectedValue)
        }
    }

    @Test
    fun `when get suggestion search list return fail`() {
        runBlocking {
            val error = NullPointerException()
            onGetSellerSearch_thenError(error)

            viewModel.getSellerSearch(keyword = anyString(), shopId = anyString())
            val expectedResult = Fail(error)
            viewModel.getSellerSearch.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `when insert success search suggestion return fail`() {
        runBlocking {
            val error = NullPointerException()
            onInsertSuccessSearch_thenError(error)

            viewModel.insertSearchSeller(anyString(), anyString(), anyString(), anyInt())
            val expectedResult = Fail(error)
            viewModel.insertSuccessSearch.verifyErrorEquals(expectedResult)
        }
    }

    private fun onInsertSuccessSearch_thenError(exception: NullPointerException) {
        coEvery { insertSuccessSearchUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    private fun onGetSellerSearch_thenError(exception: NullPointerException) {
        coEvery { getSellerSearchUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    private fun onInsertSuccessSearch_thenReturn() {
        coEvery { insertSuccessSearchUseCase.executeOnBackground() } returns SuccessSearchResponse.SuccessSearch()
    }

    private fun onGetSellerSearch_thenReturn() {
        coEvery { getSellerSearchUseCase.executeOnBackground() } returns SellerSearchResponse.SellerSearch()
    }

    private fun verifyInsertSuccessSearchUseCaseCalled() {
        coVerify { insertSuccessSearchUseCase.executeOnBackground() }
    }

    private fun verifySuccessGetSellerSearchUseCaseCaseCalled() {
        coVerify { getSellerSearchUseCase.executeOnBackground() }
    }

}