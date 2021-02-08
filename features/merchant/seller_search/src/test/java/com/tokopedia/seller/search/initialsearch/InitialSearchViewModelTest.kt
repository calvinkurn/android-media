package com.tokopedia.seller.search.initialsearch

import com.tokopedia.seller.search.common.domain.model.SellerSearchResponse
import com.tokopedia.seller.search.feature.initialsearch.domain.model.DeleteHistoryResponse
import com.tokopedia.seller.search.feature.initialsearch.view.model.deletehistory.DeleteHistorySearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.InitialSearchUiModel
import com.tokopedia.seller.search.feature.suggestion.domain.model.SuccessSearchResponse
import com.tokopedia.seller.search.feature.suggestion.view.model.registersearch.RegisterSearchUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.anyString

class InitialSearchViewModelTest: InitialSearchViewModelTestFixture() {

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
            viewModel.insertSearchSeller(anyString(), anyString(), anyString(), ArgumentMatchers.anyInt())

            verifyInsertSuccessSearchUseCaseCalled()
            val expectedValue = Success(RegisterSearchUiModel())
            assertTrue(viewModel.insertSuccessSearch.value is Success)
            viewModel.insertSuccessSearch.verifyValueEquals(expectedValue)
        }
    }

    @Test
    fun `when delete all history suggestion should return success`() {
        runBlocking {
            onDeleteSuggestionHistory_thenReturn()
            viewModel.deleteSuggestionSearch(listOf(anyString(), anyString(), anyString()))

            verifySuccessDeleteSuggestionHistoryUseCaseCalled()
            val expectedValue = Success(DeleteHistorySearchUiModel())
            assertTrue(viewModel.deleteHistorySearch.value is Success)
            viewModel.deleteHistorySearch.verifyValueEquals(expectedValue)
        }
    }

    @Test
    fun `when delete item history suggestion should return success`() {
        runBlocking {
            onDeleteSuggestionHistory_thenReturn()
            viewModel.deleteSuggestionSearch(listOf(anyString()))

            verifySuccessDeleteSuggestionHistoryUseCaseCalled()
            val expectedValue = Success(DeleteHistorySearchUiModel())
            assertTrue(viewModel.deleteHistorySearch.value is Success)
            viewModel.deleteHistorySearch.verifyValueEquals(expectedValue)
        }
    }

    @Test
    fun `when get history search list return fail`() {
        runBlocking {
            val error = NullPointerException()
            onGetSellerSearch_thenError(error)

            viewModel.getSellerSearch(keyword = anyString(), shopId = anyString())
            val expectedResult = Fail(error)
            viewModel.getSellerSearch.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `when delete all history suggestion return fail`() {
        runBlocking {
            val error = NullPointerException()
            onDeleteSuggestionHistory_thenError(error)

            viewModel.deleteSuggestionSearch(listOf(anyString(), anyString(), anyString()))
            val expectedResult = Fail(error)
            viewModel.deleteHistorySearch.verifyErrorEquals(expectedResult)
        }
    }

    @Test
    fun `when delete item history suggestion return fail`() {
        runBlocking {
            val error = NullPointerException()
            onDeleteSuggestionHistory_thenError(error)

            viewModel.deleteSuggestionSearch(listOf(anyString()))
            val expectedResult = Fail(error)
            viewModel.deleteHistorySearch.verifyErrorEquals(expectedResult)
        }
    }

    private fun onDeleteSuggestionHistory_thenError(exception: NullPointerException) {
        coEvery { deleteSuggestionHistoryUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    private fun onGetSellerSearch_thenError(exception: NullPointerException) {
        coEvery { getSellerSearchUseCase.executeOnBackground() } coAnswers { throw exception }
    }

    private fun onDeleteSuggestionHistory_thenReturn() {
        coEvery { deleteSuggestionHistoryUseCase.executeOnBackground() } returns DeleteHistoryResponse.DeleteHistory()
    }

    private fun onGetSellerSearch_thenReturn() {
        coEvery { getSellerSearchUseCase.executeOnBackground() } returns SellerSearchResponse.SellerSearch()
    }

    private fun verifySuccessDeleteSuggestionHistoryUseCaseCalled() {
        coVerify { deleteSuggestionHistoryUseCase.executeOnBackground() }
    }

    private fun onInsertSuccessSearch_thenReturn() {
        coEvery { insertSellerSearchUseCase.executeOnBackground() } returns SuccessSearchResponse.SuccessSearch()
    }

    private fun verifySuccessGetSellerSearchUseCaseCaseCalled() {
        coVerify { getSellerSearchUseCase.executeOnBackground() }
    }

    private fun verifyInsertSuccessSearchUseCaseCalled() {
        coVerify { insertSellerSearchUseCase.executeOnBackground() }
    }
}