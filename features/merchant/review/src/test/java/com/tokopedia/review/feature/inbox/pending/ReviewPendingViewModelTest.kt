package com.tokopedia.review.feature.inbox.pending

import android.accounts.NetworkErrorException
import com.tokopedia.review.common.data.Fail
import com.tokopedia.review.common.data.Success
import com.tokopedia.review.feature.inbox.common.ReviewInboxConstants
import com.tokopedia.review.feature.inbox.pending.data.ProductrevWaitForFeedbackResponse
import com.tokopedia.review.feature.inbox.pending.data.ProductrevWaitForFeedbackResponseWrapper
import com.tokopedia.review.utils.verifyErrorEquals
import com.tokopedia.review.utils.verifySuccessEquals
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Test
import java.lang.Exception

class ReviewPendingViewModelTest : ReviewPendingViewModelTestFixture() {

    @Test
    fun `when getReview should execute expected use case, update UI accordingly and get expected data`() {
        val page = ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE
        val response = ProductrevWaitForFeedbackResponseWrapper()

        onGetReview_thenReturn(response)

        viewModel.getReviewData(page)

        val expectedResponse = Success(response.productrevWaitForFeedbackWaitForFeedback, page)

        verifyGetReviewUseCaseExecuted()
        verifyReviewListEquals(expectedResponse)
    }

    @Test
    fun `when getReview fails first page should execute expected use case, update UI accordingly and fail with expected exception`() {
        val page = ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE
        val exception = NetworkErrorException()

        onGetReviewFail_thenReturn(exception)

        viewModel.getReviewData(page)

        verifyGetReviewUseCaseExecuted()
        verifyReviewListErrorEquals(Fail(exception, page))
    }

    @Test
    fun `when getReview fails next page should execute expected use case, update UI accordingly and fail with expected exception`() {
        val page = ReviewInboxConstants.REVIEW_INBOX_INITIAL_PAGE + 1
        val exception = NetworkErrorException()

        onGetReviewFail_thenReturn(exception)

        viewModel.getReviewData(page)

        verifyGetReviewUseCaseExecuted()
        verifyReviewListErrorEquals(Fail(exception, page))
    }

    private fun verifyReviewListEquals(response: Success<ProductrevWaitForFeedbackResponse>) {
        viewModel.reviewList.verifySuccessEquals(response)
    }

    private fun verifyReviewListErrorEquals(error: Fail<Any>) {
        viewModel.reviewList.verifyErrorEquals(error)
    }

    private fun verifyGetReviewUseCaseExecuted() {
        coVerify { productrevWaitForFeedbackUseCase.executeOnBackground() }
    }

    private fun onGetReview_thenReturn(response: ProductrevWaitForFeedbackResponseWrapper) {
        coEvery { productrevWaitForFeedbackUseCase.executeOnBackground() } returns response
    }

    private fun onGetReviewFail_thenReturn(exception: Exception) {
        coEvery { productrevWaitForFeedbackUseCase.executeOnBackground() } throws exception
    }
}