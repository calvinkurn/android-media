package com.tokopedia.review.feature.inbox.history

import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.feature.inbox.history.data.ProductrevFeedbackHistoryResponse
import com.tokopedia.review.feature.inbox.history.data.ProductrevFeedbackHistoryResponseWrapper
import com.tokopedia.review.utils.verifyErrorEquals
import com.tokopedia.review.utils.verifySuccessEquals
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test

class ReviewHistoryViewModelTest : ReviewHistoryViewModelTestFixture() {

    @Test
    fun `when updatePage should get data with expected page`() {
        val page = 1
        val expectedSearchQuery = ""
        val expectedNetworkResponse = ProductrevFeedbackHistoryResponseWrapper()
        val expectedViewState = Success(expectedNetworkResponse.productrevFeedbackHistoryResponse, page, expectedSearchQuery)

        onGetReviewData_thenReturn(expectedNetworkResponse)

        viewModel.updatePage(page)

        verifyGetReviewHistoryUseCaseExecuted()
        verifyReviewListSuccessEquals(expectedViewState)
    }

    @Test
    fun `when updatePage but network fails, should fail with expected page`() {
        val page = 1
        val expectedSearchQuery = ""
        val expectedNetworkResponse = Throwable()
        val expectedViewState = Fail<ProductrevFeedbackHistoryResponse>(expectedNetworkResponse, page, expectedSearchQuery)

        onGetReviewDataFail_thenReturn(expectedNetworkResponse)

        viewModel.updatePage(page)

        verifyGetReviewHistoryUseCaseExecuted()
        verifyReviewListErrorEquals(expectedViewState)
    }

    @Test
    fun `when updateKeyWord should get data with expected keyword and reset page`() {
        val expectedSearchQuery = "baju"
        val expectedPage = 0
        val expectedNetworkResponse = ProductrevFeedbackHistoryResponseWrapper()
        val expectedViewState = Success(expectedNetworkResponse.productrevFeedbackHistoryResponse, expectedPage, expectedSearchQuery)

        onGetReviewData_thenReturn(expectedNetworkResponse)

        viewModel.updateKeyWord(expectedSearchQuery)

        verifyGetReviewHistoryUseCaseExecuted()
        verifyReviewListSuccessEquals(expectedViewState)
    }

    @Test
    fun `when updateKeyWord but network fails, should fail data with expected keyword and reset page`() {
        val expectedSearchQuery = "baju"
        val expectedPage = 0
        val expectedNetworkResponse = Throwable()
        val expectedViewState = Fail<ProductrevFeedbackHistoryResponse>(expectedNetworkResponse, expectedPage, expectedSearchQuery)

        onGetReviewDataFail_thenReturn(expectedNetworkResponse)

        viewModel.updateKeyWord(expectedSearchQuery)

        verifyGetReviewHistoryUseCaseExecuted()
        verifyReviewListErrorEquals(expectedViewState)
    }

    private fun verifyReviewListSuccessEquals(response: Success<ProductrevFeedbackHistoryResponse>) {
        viewModel.reviewList.verifySuccessEquals(response)
    }

    private fun verifyReviewListErrorEquals(response: Fail<ProductrevFeedbackHistoryResponse>) {
        viewModel.reviewList.verifyErrorEquals(response)
    }

    private fun verifyGetReviewHistoryUseCaseExecuted() {
        coVerify { productrevFeedbackHistoryUseCase.executeOnBackground() }
    }

    private fun onGetReviewData_thenReturn(response: ProductrevFeedbackHistoryResponseWrapper) {
        coEvery { productrevFeedbackHistoryUseCase.executeOnBackground() } returns response
    }

    private fun onGetReviewDataFail_thenReturn(throwable: Throwable) {
        coEvery { productrevFeedbackHistoryUseCase.executeOnBackground() } throws throwable
    }
}