package com.tokopedia.seller.search.initialsearch

import com.tokopedia.seller.search.common.domain.model.SellerSearchResponse
import com.tokopedia.seller.search.feature.initialsearch.domain.model.DeleteHistoryResponse
import com.tokopedia.seller.search.feature.initialsearch.view.model.deletehistory.DeleteHistorySearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.InitialSearchUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.coVerify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString

class InitialSearchViewModelTest: InitialSearchViewModelTestFixture() {

    @Test
    fun `when get history search list should return success`() {
        runBlocking {
            onGetSellerSearch_thenReturn()
            viewModel.getSellerSearch(keyword = anyString(), shopId = anyString())

            verifySuccessGetSellerSearchUseCaseCaseCalled()
            val expectedValue = Success(InitialSearchUiModel())
            assertTrue(viewModel.getSellerSearch.value is Success)
            viewModel.getSellerSearch.verifyValueEquals(expectedValue)
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

    private fun verifySuccessGetSellerSearchUseCaseCaseCalled() {
        coVerify { getSellerSearchUseCase.executeOnBackground() }
    }
}